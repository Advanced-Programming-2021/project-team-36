package model.card.spells;

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
    protected AdvancedRitualArt(String name, String description, int price, Icon icon, Status status) {
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
                        String.format("choose the ritual monster you want to ritual summon. It's level should be at most %d.", sumOwnerMonstersInHandAndInZone()),
                        1,
                        Conditions.getPlayerRitualMonsterFromHand(this.owner, sumOwnerMonstersInHandAndInZone())
                )[0];
                Monster[] tributeMonsters = playerController.chooseKSumLevelMonsters(
                        String.format("choose a monsters from your hand or board for ritual summon. Sum of your selected cards hasn't reach the limit yet. You can select one of your selected card to deselect it", ritualMonster.getLevel()),
                        ritualMonster.getLevel(),
                        Conditions.getPlayerMonsterFromMonsterZoneOrHand(this.owner)
                );
                for (Monster monster : tributeMonsters)
                    monster.tryToSendToGraveYardOfMe();
                MonsterState monsterState;
                if (((DuelMenuView) DuelMenuController.getInstance().getView()).askUser("You want to summon your monster in attacking position or defending position? (attacking/defending)", "attacking", "defending"))
                    monsterState = MonsterState.OFFENSIVE_OCCUPIED;
                else
                    monsterState = MonsterState.DEFENSIVE_OCCUPIED;
                // TODO : summon should be different
                Game game = GameController.getInstance().getGame();
                Board board = game.getCurrentPlayer().getBoard();
                for (int i = 1; i <= 5; i++) {
                    if (board.getMonsterCardZone().get(i) == null) {
                        board.addCardToBoard(ritualMonster, new CardAddress(ZoneType.MONSTER, i, false));
                        board.getCardsOnHand().remove(ritualMonster);
                        ritualMonster.setMonsterState(monsterState);
                        break;
                    }
                }
                try {
                    playerController.summon(ritualMonster, 0, monsterState);
                } catch (LogicException logicException) {
                    CustomPrinter.println("this shouldn't happens", Color.Default);
                }
            } catch (ResistToChooseCard resistToChooseCard) {
                CustomPrinter.println("ritual monster cancelled", Color.Default);
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
        return minimumMonsterRitualLevelOnHand <= sumOwnerMonstersInHandAndInZone() &&
                !player.getBoard().isMonsterCardZoneFull();
    }
}
