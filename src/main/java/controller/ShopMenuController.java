package controller;

import model.ModelException;
import model.User;
import model.card.Card;
import model.card.Utils;
import view.Context;

import java.util.Arrays;

public class ShopMenuController {
    public static void buyCard(Context context, Card card) throws ModelException {
        User user = context.getUser();
        if(user.getBalance() < card.getPrice())
            throw new ModelException("not enough money");
        user.buy(card.clone());
    }
    public static void showAll(Context context) {
        Arrays.stream(Utils.getAllCards()).sorted().forEach(System.out::println);
    }
}
