package YuGiOh.view.cardSelector;

import YuGiOh.controller.GameController;
import YuGiOh.model.Game;
import YuGiOh.model.Player.Player;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Magic;
import YuGiOh.model.card.Monster;
import YuGiOh.model.enums.MonsterCardType;
import YuGiOh.model.enums.MonsterState;
import YuGiOh.model.enums.ZoneType;

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
        return (Card card) -> {
            if (card instanceof Magic)
                return false;
            Monster monster = (Monster) card;
            return monster.getMonsterCardType().equals(MonsterCardType.RITUAL) && player.hasInHand(card) && monster.getLevel() <= maximumLevelLimit;
        };
    }

    public static SelectCondition getPlayerMonsterFromMonsterZoneOrHand(Player player, Monster notAllowed) {
        return (Card card) ->
                card instanceof Monster && card != notAllowed && (player.hasInHand(card) || player.getBoard().getMonsterCardZone().containsValue(card));
    }

    public static SelectCondition getPlayerMonsterFromMonsterZone(Player player) {
        return (Card card) ->
                card instanceof Monster &&  player.getBoard().getMonsterCardZone().containsValue(card);
    }

    public static SelectCondition getMonsterFromGraveYard() {
        return (Card card) -> {
            if (!(card instanceof Monster))
                return false;
            Player firstPlayer = GameController.getInstance().getGame().getFirstPlayer();
            Player secondPlayer = GameController.getInstance().getGame().getSecondPlayer();
            return firstPlayer.hasInGraveYard(card) || secondPlayer.hasInGraveYard(card);
        };
    }
}
