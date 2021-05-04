package controller;

import model.User;
import model.card.Card;

public class ShopMenu extends BaseMenu {
    public static void buyCard(Card card) {
        User user = User.getCurrentUser();
        if (user.getBalance() < card.getPrice()) {
            System.out.println("not enough money");
            return;
        }
        user.buy(card);
    }
    public static void shopAll() {
        for (Card card : Card.getAllCardsLexicographically())
            System.out.println(card);
    }
    protected static void showCurrentMenu() {

    }
    protected static void navigateToMenu(String menu) {

    }
    protected static void exit() {

    }
    private static void start(User user) {

    }
    public static void programControl() {

    }
}
