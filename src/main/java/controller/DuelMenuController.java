package controller;

import model.CardAddress;
import model.Game;
import model.card.*;
import model.enums.Phase;
import model.enums.State;
import view.Context;

import java.util.List;

public class DuelMenuController {
    public static void selectCard(Context context, CardAddress cardAddress) throws LogicException {
        // TODO : should check cardAddress is valid or not
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

    public static void goNextPhase(Context context) {
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
    }

    public static void summonCard(Context context, Card card) {
        Game game = context.getGame();
    }

    public static void setCard(Context context, Card card) {
        Game game = context.getGame();
    }

    public static void changeCardPosition(Context context, Card card, State state) {
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

    private static void showBoard(Context context) {
        Game game = context.getGame();
        System.out.println(game.getOpponentPlayer().getUser().getNickname() + ":" + game.getOpponentPlayer().getLifePoint());
        System.out.println(game.getOpponentPlayer().getBoard().toString()); // TODO it should rotate 180 degree
        System.out.println();
        System.out.println("--------------------------");
        System.out.println();
        System.out.println();
        System.out.println(game.getCurrentPlayer().getBoard().toString());
        System.out.println(game.getCurrentPlayer().getUser().getNickname() + ":" + game.getCurrentPlayer().getLifePoint());
    }

    public static void showCard(Context context, Card card){
        Game game = context.getGame();
    }

    private static void surrender(Context context) {
        Game game = context.getGame();
    }
}
