package controller.cardSelector;

import controller.GameController;
import model.Game;
import model.Player.Player;
import model.card.Card;
import model.card.Monster;
import model.enums.MonsterCardType;
import model.enums.MonsterState;
import model.enums.ZoneType;

public class Conditions {
    public static final SelectCondition noCondition = (Card card) -> true;

    private static Game getGame() {
        return GameController.getInstance().getGame();
    }

    private static ZoneType getCardZoneType(Card card) {
        return getGame().getCardZoneType(card);
    }

    public static SelectCondition getInPlayersHandCondition(Player player) {
        return player::hasInHand;
    }

    public static SelectCondition getInPlayerGraveYardMonster(Player player, int levelLimit) {
        return (Card card) -> {
            return player.getBoard().getGraveYard().contains(card) &&
                    card instanceof Monster &&
                    ((Monster) card).getLevel() >= levelLimit;
        };
    }

    public static final SelectCondition inMonsterZone = (Card card) ->
            getCardZoneType(card).equals(ZoneType.MONSTER);

    public static final SelectCondition flippableInMonsterZone = (Card card) ->
            getCardZoneType(card).equals(ZoneType.MONSTER) &&
                    !((Monster) card).getMonsterState().equals(MonsterState.DEFENSIVE_HIDDEN);

    public static final SelectCondition OpponentMonsterFromGraveYard = (Card card) ->
            getGame().getOpponentPlayer().getBoard().getGraveYard().contains(card) && card instanceof Monster;


    public static SelectCondition myMonsterFromMyMonsterZone(Player player) {
        return (Card card) ->
                card instanceof Monster && player.getBoard().getMonsterCardZone().containsValue((Monster) card);
    }

    public static SelectCondition myMonsterFromMyHand(Player player) {
        return (Card card) ->
                card instanceof Monster && player.getBoard().getMonsterCardZone().containsValue((Monster) card);
    }

    public static SelectCondition getPlayerRitualMonsterFromHand(Player player, int maximumLevelLimit) {
        return (Card card) ->
                card instanceof Monster && ((Monster) card).getMonsterCardType().equals(MonsterCardType.RITUAL) &&
                        player.hasInHand(card) && ((Monster) card).getLevel() <= maximumLevelLimit;
    }

    public static SelectCondition getPlayerMonsterFromMonsterZoneOrHand(Player player) {
        return (Card card) ->
                card instanceof Monster && (player.hasInHand(card) || player.getBoard().getMonsterCardZone().containsValue(card));
    }
}
