package YuGiOh.controller.menu;

import YuGiOh.utils.RoutingException;
import YuGiOh.view.BaseMenuView;
import lombok.Getter;

public abstract class BaseMenuController {
    abstract public void exitMenu() throws RoutingException;
    abstract public BaseMenuController getNavigatingMenuObject(Class<? extends BaseMenuController> menu) throws RoutingException;

    @Getter
    protected BaseMenuView view;

    public void control() {
        this.view.runNextCommand();
    }
}