package controller;

import model.Board;
import model.CardAddress;
import model.Game;
import model.Player;
import model.card.*;
import model.enums.Phase;
import model.enums.MonsterState;
import model.enums.ZoneType;
import view.Context;

import java.util.List;

public class DuelMenuController {
    public static void selectCard(Context context, CardAddress cardAddress) throws LogicException {
        Game game = context.getGame();
        if (game.getCardByCardAddress(cardAddress) == null)
            throw new LogicException("no card found in the given position");
        game.selectCard(cardAddress);
        System.out.println("card selected");
    }

    public static void deselectCard(Context context) throws LogicException {
        Game game = context.getGame();
        if (game.getSelectedCardAddress() == null)
            throw new LogicException("no card is selected");
        game.unselectCard();
        System.out.println("card deselected");
    }

    public static Card getSelectedCard(Context context) throws LogicException {
        Game game = context.getGame();
        if (!game.isAnyCardSelected())
            throw new LogicException("no card is selected");
        return game.getCardByCardAddress(game.getSelectedCardAddress());
    }

    public static void printCurrentPhase(Context context) {
        Game game = context.getGame();
        if (game.getPhase() == Phase.DRAWPHASE)
            System.out.println("phase: draw phase");
        else if (game.getPhase() == Phase.STANDBYPHASE)
            System.out.println("phase: standby phase");
        else if (game.getPhase() == Phase.MAINPHASE1)
            System.out.println("phase: main phase 1");
        else if (game.getPhase() == Phase.BATTLEPHASE)
            System.out.println("phase: battle phase");
        else if (game.getPhase() == Phase.MAINPHASE2)
            System.out.println("phase: main phase 2");
        else
            System.out.println("phase: end phase");
    }

    public static void goNextPhase(Context context) throws LogicException {
        Game game = context.getGame();
        if (game.getPhase() == Phase.DRAWPHASE)
            game.setPhase(Phase.STANDBYPHASE);
        else if (game.getPhase() == Phase.STANDBYPHASE)
            game.setPhase(Phase.MAINPHASE1);
        else if (game.getPhase() == Phase.MAINPHASE1)
            game.setPhase(Phase.BATTLEPHASE);
        else if (game.getPhase() == Phase.BATTLEPHASE)
            game.setPhase(Phase.MAINPHASE2);
        else if (game.getPhase() == Phase.MAINPHASE2)
            game.setPhase(Phase.ENDPHASE);
        else
            game.setPhase(Phase.DRAWPHASE);

        if (game.isFirstTurn() && game.getPhase().equals(Phase.BATTLEPHASE))
            game.setPhase(Phase.MAINPHASE2);

        printCurrentPhase(context);
        if (game.getPhase() == Phase.ENDPHASE) {
            changeTurn(context);
            game.setPhase(Phase.DRAWPHASE);
            printCurrentPhase(context);
            drawCard(context);
        }
    }

    public static void drawCard(Context context) throws LogicException {
        Game game = context.getGame();
        Card card = game.getCurrentPlayer().getMainDeck().getTopCard();
        if (card == null) {
            endGame(context, game.getOpponentPlayer(), game.getCurrentPlayer());
            return;
        }
        game.getCurrentPlayer().getBoard().drawCardFromDeck();
        System.out.println(String.format("new card added to the hand : %s", card.getName()));
    }

    public static void summonCard(Context context) throws LogicException {
        Game game = context.getGame();
        if (game.getSelectedCardAddress() == null)
            throw new LogicException("no card is selected yet");

        CardAddress cardAddress = game.getSelectedCardAddress();
        if (!cardAddress.isInHand() || getSelectedCard(context) instanceof Magic)
            throw new LogicException("you can't summon this card");

        if (!game.getPhase().equals(Phase.MAINPHASE1) && !game.getPhase().equals(Phase.MAINPHASE2))
            throw new LogicException("action not allowed in this phase");
        if (game.getCurrentPlayer().getBoard().isMonsterCardZoneFull())
            throw new LogicException("monster card zone is full");
        if (game.isSummonedInThisTurn())
            throw new LogicException("you already summoned/set on this turn");
        // TODO : monster with higher level than 4
        game.setSummonedInThisTurn(true);
        System.out.println("summoned successfully");
        Board board = game.getCurrentPlayer().getBoard();
        for (int i = 1; i <= 5; i++) {
            if (board.getMonsterCardZone().get(i) == null) {
                board.addCardToBoard(getSelectedCard(context), new CardAddress(ZoneType.HAND, i, false));
                board.getCardsOnHand().remove(getSelectedCard(context));
                game.unselectCard();
                break;
            }
        }
    }

    public static void setCard(Context context, Card card) {
        Game game = context.getGame();
    }

    public static void changeCardPosition(Context context, Card card, MonsterState monsterState) {
        Game game = context.getGame();
    }

    public static void flipSummon(Context context, Card card) {
        Game game = context.getGame();
    }

    private static void ritualSummon(Context context, Card card) {
        Game game = context.getGame();
    }

    public static void attack(Context context, Card card, int id) {
        Game game = context.getGame();
    }

    public static void directAttack(Context context, Card card) {
        Game game = context.getGame();
    }

    public static void activateEffect(Context context, Card card) {
        Game game = context.getGame();
    }

    public static void showGraveYard(Context context) {
        Game game = context.getGame();
        List<Card> graveYard = game.getCurrentPlayer().getBoard().getGraveYard();
        if (graveYard.isEmpty())
            System.out.println("graveyard empty");
        for (int i = 0; i < graveYard.size(); i++)
            System.out.println((i + 1) + ". " + graveYard.get(i).toString());
    }

    public static void showBoard(Context context) {
        Game game = context.getGame();
        System.out.println(game.getOpponentPlayer().getUser().getNickname() + ":" + game.getOpponentPlayer().getLifePoint());
        System.out.println(game.getOpponentPlayer().getBoard().toString()); // TODO it should rotate 180 degree
        System.out.println();
        System.out.println("--------------------------");
        System.out.println();
        System.out.println(game.getCurrentPlayer().getBoard().toString());
        System.out.println(game.getCurrentPlayer().getUser().getNickname() + ":" + game.getCurrentPlayer().getLifePoint());
    }

    public static void showSelectedCard(Context context) throws LogicException {
        Game game = context.getGame();
        Card card = getSelectedCard(context);
        System.out.println(card.toString());
    }

    public static void showHand(Context context) {
        Game game = context.getGame();
        List<Card> cards = game.getCurrentPlayer().getBoard().getCardsOnHand();
        for (int i = 0; i < cards.size(); i++)
            System.out.println(String.format("%d. %s", i + 1, cards.get(i).toString()));
    }

    private static void surrender(Context context) {
        Game game = context.getGame();
    }

    private static void changeTurn(Context context) {
        Game game = context.getGame();
        game.changeTurn();
        System.out.println(String.format("its %s's turn", game.getCurrentPlayer().getUser().getNickname()));
        game.setSummonedInThisTurn(false);
        game.unselectCard();
    }

    private static void endGame(Context context, Player winner, Player loser) {

    }
}
