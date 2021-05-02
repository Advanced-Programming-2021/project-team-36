package Utils;

import view.BaseMenu;

public class Router {
    private static BaseMenu currentMenu;

    public static void setCurrentMenu(BaseMenu currentMenu){
        Router.currentMenu = currentMenu;
    }
    public static BaseMenu getCurrentMenu(){
        return currentMenu;
    }
}
