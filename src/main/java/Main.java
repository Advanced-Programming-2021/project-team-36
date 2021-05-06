import Utils.Router;
import view.LoginMenu;

public class Main {
    public static void main(String[] args) {
        Router.setCurrentMenu(new LoginMenu());
        while (true)
            Router.getCurrentMenu().runNextCommand();
    }
}
