package edu.sharif.nameless.in.seattle.yugioh.controller;


import edu.sharif.nameless.in.seattle.yugioh.controller.menu.LoginMenuController;
import edu.sharif.nameless.in.seattle.yugioh.utils.DatabaseHandler;
import edu.sharif.nameless.in.seattle.yugioh.utils.RoutingException;
import edu.sharif.nameless.in.seattle.yugioh.controller.menu.BaseMenuController;
import lombok.Getter;
import edu.sharif.nameless.in.seattle.yugioh.utils.Debugger;

public class ProgramController {
    @Getter
    private static ProgramController instance;
    @Getter
    private BaseMenuController currentController;

    public ProgramController() {
        DatabaseHandler.importFromDatabase();
        currentController = new LoginMenuController();
        instance = this;
    }

    public void menuExit() throws RoutingException {
        this.currentController.exitMenu();
    }

    public void programExit() {
        if (Debugger.getCaptureMode()) {
            try {
                Debugger.setCaptureMode("off");
            } catch (Exception ignored) {
            }
        }

        if (Debugger.getAutomaticSave())
            DatabaseHandler.exportToDatabase();
        System.exit(0);
    }

    public void navigateToMenu(Class<? extends BaseMenuController> menu) throws RoutingException {
        currentController = currentController.getNavigatingMenuObject(menu);
    }

    public void navigateToMenu(BaseMenuController menuController) {
        currentController = menuController;
    }

    public void control() {
        while (true)
            currentController.control();
    }
}
