import Utils.Parser;
import Utils.ParserException;
import Utils.Router;
import Utils.RoutingException;
import model.ModelException;
import view.Context;
import view.LoginMenu;

import java.util.Scanner;

public class Main {
    private static final Scanner scanner;

    static {
        scanner = new Scanner(System.in);
    }
    
    public static void main(String[] args) throws ParserException, ModelException, RoutingException {
        // this is for test
        Router.setCurrentMenu(new LoginMenu(scanner));
        controller.LoginMenuController.createUser(Context.getInstance(), "shayan", "shayan.p", "1234");
        controller.LoginMenuController.createUser(Context.getInstance(), "abolfazl", "abolof", "12345");
        controller.LoginMenuController.login(Context.getInstance(), "shayan", "1234");
//        controller.MainMenuController.startNewDuel(Context.getInstance(), Parser.UserParser("abolfazl"), 3);
        // end initializing
        while (true){
            Router.getCurrentMenu().runNextCommand();
        }
    }
}
