package YuGiOh.model.card.magicCards.spells;

import YuGiOh.controller.GameController;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.Player.Player;
import YuGiOh.model.card.action.SummonAction;
import YuGiOh.model.card.event.SummonEvent;
import YuGiOh.model.enums.*;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.archive.view.cardSelector.SelectConditions;
import YuGiOh.archive.view.cardSelector.ResistToChooseCard;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.action.Effect;

public class AdvancedRitualArt extends Spell {
    public AdvancedRitualArt(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    int sumOwnerMonstersInHandAndInZone() {
        Player player = this.getOwner();
        int sumMonstersLevel = 0;
        for (Monster monster : player.getBoard().getMonsterCardZone().values())
            sumMonstersLevel += monster.getLevel();
        for (Card card : player.getBoard().getCardsOnHand())
            if (card instanceof Monster)
                sumMonstersLevel += ((Monster) card).getLevel();
        return sumMonstersLevel;
    }

    private Monster ritualMonster;
    private Monster[] tributeMonsters;

    private void preprocess() throws ResistToChooseCard {
        PlayerController playerController = GameController.getInstance().getPlayerControllerByPlayer(this.getOwner());
        ritualMonster = (Monster) playerController.chooseKCards(
                "choose the ritual monster you want to ritual summon.",
                1,
                SelectConditions.getPlayerRitualMonsterFromHand(this.getOwner(), sumOwnerMonstersInHandAndInZone())
        )[0];
        tributeMonsters = playerController.chooseKSumLevelMonsters(
                "choose a monsters from your hand or board for ritual summon. Sum of your selected cards hasn't reach the limit yet. You can select one of your selected card to deselect it",
                ritualMonster.getLevel(),
                SelectConditions.getPlayerMonsterFromMonsterZoneOrHand(this.getOwner(), ritualMonster)
        );
    }

    @Override
    protected Effect getEffect() {
        return () -> {
            try {
                preprocess();
                for (Monster monster : tributeMonsters)
                    GameController.getInstance().moveCardToGraveYard(monster);
                SummonAction action = new SummonAction(
                        new SummonEvent(this.getOwner(), ritualMonster, SummonType.RITUAL, 0, SelectConditions.noCondition)
                );
                action.runEffect();
                GameController.getInstance().moveCardToGraveYard(this);
                CustomPrinter.println(String.format("<%s> ritual summoned <%s> in <%s> position successfully", this.getOwner().getUser().getUsername(), ritualMonster.getName(), ritualMonster.getMonsterState()), Color.Yellow);
            } catch (ResistToChooseCard resistToChooseCard) {
                CustomPrinter.println(this.getOwner().getUser().getUsername() + " cancelled ritual monster", Color.Green);
            }
        };
    }

    @Override
    public boolean canActivateEffect() {
        Player player = GameController.getInstance().getPlayerControllerByPlayer(this.getOwner()).getPlayer();
        int minimumMonsterRitualLevelOnHand = 1000;
        for (Card card : player.getBoard().getCardsOnHand()) {
            if (card instanceof Monster) {
                Monster monster = (Monster) card;
                if (monster.getMonsterCardType().equals(MonsterCardType.RITUAL))
                    minimumMonsterRitualLevelOnHand = Math.min(minimumMonsterRitualLevelOnHand, monster.getLevel());
            }
        }

        return minimumMonsterRitualLevelOnHand <= sumOwnerMonstersInHandAndInZone() - minimumMonsterRitualLevelOnHand &&
                !player.getBoard().isMonsterCardZoneFull();
    }
}
