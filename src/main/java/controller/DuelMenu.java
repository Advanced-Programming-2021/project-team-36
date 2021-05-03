package controller;

import model.CardAddress;
import model.Game;
import model.card.*;
import model.enums.Phase;

import java.util.List;

public class DuelMenu extends BaseMenu {
    private static Game game;

    private static void selectCard(CardAddress cardAddress) {
        // TODO : should check cardAddress is valid or not

        if (game.getCardByCardAddress(cardAddress) == null)
            System.out.println("no card found in the given position");
        else {
            game.selectCard(cardAddress);
            System.out.println("card selected");
        }

    }

    private static void deselectCard() {
        if (game.getSelectedCardAddress() == null)
            System.out.println("no card is selected");
        else {
            game.unselectCard();
            System.out.println("card deselected");
        }
    }

    private static void printCurrentPhase() {
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

    private static void goNextPhase() {
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

    private static void summonCard() {

    }

    private static void setCard(Card card) {

    }

    private static void changeCardPosition(Card card) {

    }

    private static void flipSummon(Card card) {

    }

    private static void ritualSummon(Card card) {

    }

    private static void attack(Card card, int id) {

    }

    private static void directAttack(Card card) {

    }

    private static void activateEffect(Card card) {

    }

    private static void showGraveYard() {
        List<Card> graveYard = game.getCurrentPlayer().getBoard().getGraveYard();
        if (graveYard.isEmpty())
            System.out.println("graveyard empty");
        for (int i = 0; i < graveYard.size(); i++)
            System.out.println((i + 1) + ". " + graveYard.get(i).toString());
    }

    private static void showBoard() {
        System.out.println(game.getOpponentPlayer().getUser().getNickname() + ":" + game.getOpponentPlayer().getLifePoint());
        System.out.println(game.getOpponentPlayer().getBoard().toString()); // TODO it should rotate 180 degree
        System.out.println();
        System.out.println("--------------------------");
        System.out.println();
        System.out.println();
        System.out.println(game.getCurrentPlayer().getBoard().toString());
        System.out.println(game.getCurrentPlayer().getUser().getNickname() + ":" + game.getCurrentPlayer().getLifePoint());
    }

    private static void surrender() {

    }

    protected static void showCurrentMenu() {

    }

    protected static void navigateToMenu(String menu) {

    }

    protected static void exit() {

    }

    private static void start(Game game) {

    }

    public static void programControl() {

    }
}
