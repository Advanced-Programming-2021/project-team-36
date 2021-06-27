package YuGiOh.controller;


import YuGiOh.controller.menu.BaseMenuController;
import YuGiOh.controller.menu.LoginMenuController;
import YuGiOh.model.enums.Color;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.utils.DatabaseHandler;
import YuGiOh.utils.RoutingException;
import lombok.Getter;
import YuGiOh.utils.Debugger;

import java.util.List;

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
        CustomPrinter.println("you entered " + currentController.getView().getMenuName(), Color.Blue);
    }

    public void navigateToMenu(BaseMenuController menuController) {
        currentController = menuController;
        CustomPrinter.println("you entered " + currentController.getView().getMenuName(), Color.Blue);
    }

    public void control() {
        while (true)
            currentController.control();
    }
}
