package Utils;

import controller.LogicException;
import view.BaseMenu;

public class Router {
    private static BaseMenu currentMenu;

    public static void setCurrentMenu(BaseMenu currentMenu){
        Router.currentMenu = currentMenu;
    }
    public static BaseMenu getCurrentMenu(){
        return currentMenu;
    }
    public static void navigateToMenu(Class<?> menu) throws RoutingException {
        currentMenu = currentMenu.getNavigatingMenuObject(menu);
    }
    public static void gameOver(){
        // maybe you want to save the game or anything else now?
        System.exit(0);
    }
}
