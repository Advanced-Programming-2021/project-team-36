package controller.menu;

import Utils.RoutingException;
import view.*;

public abstract class BaseMenuController {
    abstract public void exitMenu() throws RoutingException;
    abstract public BaseMenuController getNavigatingMenuObject(Class<? extends BaseMenuController> menu) throws RoutingException;

    protected BaseMenuView view;

    public void control(){
        this.view.runNextCommand();
    }
}
