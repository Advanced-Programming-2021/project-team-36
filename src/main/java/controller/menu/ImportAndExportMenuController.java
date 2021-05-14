package controller.menu;

import Utils.RoutingException;
import controller.ProgramController;
import lombok.Getter;
import model.User;
import view.ImportAndExportMenuView;

public class ImportAndExportMenuController extends BaseMenuController {
    @Getter
    public static ImportAndExportMenuController instance;

    public ImportAndExportMenuController(){
        this.view = new ImportAndExportMenuView();
        instance = this;
    }

    public void importCard(User user, String cardName) {

    }
    public void exportCard(User user, String cardName) {

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
