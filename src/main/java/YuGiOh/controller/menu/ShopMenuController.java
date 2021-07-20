package YuGiOh.controller.menu;

import YuGiOh.model.User;
import YuGiOh.model.card.Card;
import YuGiOh.model.exception.ModelException;
import YuGiOh.network.packet.Request;

public class ShopMenuController extends BaseMenuController {
    public void buy(Request request, Card card) throws ModelException {
        User user = request.getUser();
        if (user.getBalance() < card.getPrice())
            throw new ModelException("You don't have enough money!");
        user.buy(card);
    }
}
