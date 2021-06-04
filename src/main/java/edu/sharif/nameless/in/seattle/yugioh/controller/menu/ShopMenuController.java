package edu.sharif.nameless.in.seattle.yugioh.controller.menu;

import edu.sharif.nameless.in.seattle.yugioh.controller.ProgramController;
import edu.sharif.nameless.in.seattle.yugioh.model.ModelException;
import edu.sharif.nameless.in.seattle.yugioh.model.User;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Card;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.Color;
import edu.sharif.nameless.in.seattle.yugioh.utils.CustomPrinter;
import edu.sharif.nameless.in.seattle.yugioh.utils.RoutingException;
import edu.sharif.nameless.in.seattle.yugioh.view.ShopMenuView;
import lombok.Getter;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Utils;

import java.util.Arrays;

public class ShopMenuController extends BaseMenuController {
    @Getter
    public static ShopMenuController instance;
    private final User user;

    public ShopMenuController(User user){
        this.view = new ShopMenuView();
        this.user = user;
        instance = this;
    }

    public void buyCard(Card card) throws ModelException {
        if(user.getBalance() < card.getPrice())
            throw new ModelException("not enough money");
        user.buy(card);
        // CustomPrinter.println(String.format("you bought %s successfully", card.getName()));
    }

    public void showAll() {
        Arrays.stream(Utils.getAllCards()).forEach(o -> CustomPrinter.println(o, Color.Default));
    }

    @Override
    public void exitMenu() throws RoutingException {
        ProgramController.getInstance().navigateToMenu(MainMenuController.class);
    }

    @Override
    public BaseMenuController getNavigatingMenuObject(Class<? extends BaseMenuController> menu) throws RoutingException {
        if (menu.equals(this.getClass()))
            throw new RoutingException("can't navigate to your current menu!");
        if (menu.equals(LoginMenuController.class))
            throw new RoutingException("you must logout for that!");
        if (menu.equals(MainMenuController.class))
            return MainMenuController.getInstance();
        throw new RoutingException("menu navigation is not possible");
    }
}
