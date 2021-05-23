package model.card.magicCards.spells;

import controller.GameController;
import controller.LogicException;
import controller.cardSelector.Conditions;
import controller.cardSelector.ResistToChooseCard;
import controller.menu.DuelMenuController;
import controller.player.PlayerController;
import model.Board;
import model.CardAddress;
import model.Game;
import model.Player.Player;
import model.card.Card;
import model.card.Monster;
import model.card.Spell;
import model.card.action.Effect;
import model.enums.*;
import utils.CustomPrinter;
import view.DuelMenuView;

public class AdvancedRitualArt extends Spell {
    public AdvancedRitualArt(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    int sumOwnerMonstersInHandAndInZone() {
        Player player = this.owner;
        int sumMonstersLevel = 0;
        for (Monster monster : player.getBoard().getMonsterCardZone().values())
            sumMonstersLevel += monster.getLevel();
        for (Card card : player.getBoard().getCardsOnHand())
            if (card instanceof Monster)
                sumMonstersLevel += ((Monster) card).getLevel();
        return sumMonstersLevel;
    }

    @Override
    public Effect activateEffect() {
        assert canActivateEffect();
        return () -> {
            try {
                PlayerController playerController = GameController.getInstance().getPlayerControllerByPlayer(this.owner);
                Monster ritualMonster = (Monster) playerController.chooseKCards(
                        "choose the ritual monster you want to ritual summon.",
                        1,
                        Conditions.getPlayerRitualMonsterFromHand(this.owner, sumOwnerMonstersInHandAndInZone())
                )[0];
                Monster[] tributeMonsters = playerController.chooseKSumLevelMonsters(
                        String.format("choose a monsters from your hand or board for ritual summon. Sum of your selected cards hasn't reach the limit yet. You can select one of your selected card to deselect it", ritualMonster.getLevel()),
                        ritualMonster.getLevel(),
                        Conditions.getPlayerMonsterFromMonsterZoneOrHand(this.owner, ritualMonster)
                );
                for (Monster monster : tributeMonsters)
                    monster.tryToSendToGraveYardOfMe();
                MonsterState monsterState;
                if (playerController.askRespondToQuestion("You want to summon your monster in attacking position or defending position?", "attacking", "defending"))
                    monsterState = MonsterState.OFFENSIVE_OCCUPIED;
                else
                    monsterState = MonsterState.DEFENSIVE_OCCUPIED;
                try {
                    playerController.summon(ritualMonster, 0, monsterState);
                    this.owner.getBoard().getCardsOnHand().remove(ritualMonster);
                    GameController.getInstance().getPlayerControllerByPlayer(this.owner).moveCardToGraveYard(this);
                    CustomPrinter.println(String.format("<%s> ritual summoned <%s> in <%s> position successfully", this.owner.getUser().getUsername(), ritualMonster.getName(), monsterState), Color.Green);
                } catch (LogicException logicException) {
                    CustomPrinter.println("this shouldn't happens", Color.Red);
                }
            } catch (ResistToChooseCard resistToChooseCard) {
                CustomPrinter.println("ritual monster cancelled", Color.Green);
            }
        };
    }

    @Override
    public boolean canActivateEffect() {
        Player player = GameController.getInstance().getPlayerControllerByPlayer(this.owner).getPlayer();
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
