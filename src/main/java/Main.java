import YuGiOh.controller.ProgramController;
import YuGiOh.model.enums.Color;
import YuGiOh.model.enums.Constants;
import YuGiOh.utils.CustomPrinter;

public class Main {
    public static void main(String[] args) {
        ProgramController controller = new ProgramController();
        printWelcomeMessage();
        controller.control();
    }

    public static void printWelcomeMessage(){
        CustomPrinter.println(
                "Welcome to amazing game of Yu Gi Oh!\n" +
                        "press help when you needed help!\n" +
                        "also every thing will be saved if you \n" +
                        "exit the program by calling 'menu exit'\n" +
                        "now you can login! We will give you " + Constants.InitialMoney + "$ go buy some cards for yourself!",
                Color.Cyan
        );
    }
}