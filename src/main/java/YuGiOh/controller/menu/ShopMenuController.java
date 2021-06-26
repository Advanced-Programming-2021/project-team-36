package YuGiOh.controller.menu;

import YuGiOh.controller.ProgramController;
import YuGiOh.model.ModelException;
import YuGiOh.model.User;
import YuGiOh.model.card.Card;
import YuGiOh.model.enums.Color;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.utils.RoutingException;
import YuGiOh.view.ShopMenuView;
import lombok.Getter;
import YuGiOh.model.card.Utils;

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
