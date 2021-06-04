package edu.sharif.nameless.in.seattle.yugioh.controller.menu;

import edu.sharif.nameless.in.seattle.yugioh.utils.RoutingException;
import edu.sharif.nameless.in.seattle.yugioh.view.BaseMenuView;
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
