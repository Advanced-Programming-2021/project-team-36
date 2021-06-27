package YuGiOh.controller.menu;

import YuGiOh.utils.RoutingException;
import YuGiOh.view.BaseMenuView;
import lombok.Getter;

import java.util.List;

public abstract class BaseMenuController {
    abstract public void exitMenu() throws RoutingException;
    abstract public BaseMenuController getNavigatingMenuObject(Class<? extends BaseMenuController> menu) throws RoutingException;
    abstract public String[] possibleNavigates();

    @Getter
    protected BaseMenuView view;

    public void control() {
        this.view.runNextCommand();
    }
}
