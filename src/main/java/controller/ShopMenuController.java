package controller;

import model.User;
import model.card.Card;
import view.Context;

public class ShopMenuController {
    public static void buyCard(Context context, Card card) {
        User user = context.getUser();
        if (user.getBalance() < card.getPrice()) {
            System.out.println("not enough money");
            return;
        }
        user.buy(card);
    }

    public static void showAll(Context context) {
        for (Card card : Card.getAllCardsLexicographically())
            System.out.println(card);
    }
}
