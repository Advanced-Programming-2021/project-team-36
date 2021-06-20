package YuGiOh.graphicController;

import YuGiOh.utils.DatabaseHandler;
import YuGiOh.utils.Debugger;
import YuGiOh.utils.RoutingException;
import lombok.Getter;

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

    /*public void control() {
        while (true)
            currentController.control();
    }*/
}
