import Utils.Router;
import view.LoginMenu;

import java.util.Scanner;

public class Main {
    private static Scanner scanner;

    static {
        scanner = new Scanner(System.in);
    }
    
    public static void main(String[] args) {
        Router.setCurrentMenu(new LoginMenu(scanner));
        while (true){
            Router.getCurrentMenu().runNextCommand();
        }
    }
}
