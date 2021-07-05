package YuGiOh.model.card.magicCards.spells;

import YuGiOh.controller.GameController;
import YuGiOh.controller.LogicException;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.Player.Player;
import YuGiOh.model.enums.*;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.view.cardSelector.SelectConditions;
import YuGiOh.view.cardSelector.ResistToChooseCard;
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

    @Override
    protected Effect getEffect() {
        assert canActivateEffect();
        return () -> {
            try {
                PlayerController playerController = GameController.getInstance().getPlayerControllerByPlayer(this.getOwner());
                Monster ritualMonster = (Monster) playerController.chooseKCards(
                        "choose the ritual monster you want to ritual summon.",
                        1,
                        SelectConditions.getPlayerRitualMonsterFromHand(this.getOwner(), sumOwnerMonstersInHandAndInZone())
                )[0];
                Monster[] tributeMonsters = playerController.chooseKSumLevelMonsters(
                        "choose a monsters from your hand or board for ritual summon. Sum of your selected cards hasn't reach the limit yet. You can select one of your selected card to deselect it",
                        ritualMonster.getLevel(),
                        SelectConditions.getPlayerMonsterFromMonsterZoneOrHand(this.getOwner(), ritualMonster)
                );

                for (Monster monster : tributeMonsters)
                    monster.tryToSendToGraveYardOfMe();

                try {
                    playerController.summon(ritualMonster, 0, true);
                    GameController.getInstance().moveCardToGraveYard(this);
                    CustomPrinter.println(String.format("<%s> ritual summoned <%s> in <%s> position successfully", this.getOwner().getUser().getUsername(), ritualMonster.getName(), ritualMonster.getMonsterState()), Color.Yellow);
                } catch (LogicException logicException) {
                    CustomPrinter.println("this shouldn't happens", Color.Red);
                }
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
