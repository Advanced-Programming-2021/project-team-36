package controller.menu;

import controller.events.GameEvent;
import utils.RoutingException;
import lombok.Getter;
import view.*;

public abstract class BaseMenuController {
    abstract public void exitMenu() throws RoutingException;
    abstract public BaseMenuController getNavigatingMenuObject(Class<? extends BaseMenuController> menu) throws RoutingException;

    @Getter
    protected BaseMenuView view;

    public void control() {
        this.view.runNextCommand();
    }
}
