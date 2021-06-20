package YuGiOh.graphicView;

import YuGiOh.graphicController.LoginMenuController;
import YuGiOh.graphicController.MainMenuController;
import YuGiOh.model.User;
import javafx.application.Application;
import javafx.stage.Stage;

public class KasraGraphicStuffBtwThisNameIsWayTooLongLOL extends Application {

    @Override
    public void start(Stage primaryStage) {
        //new LoginMenuController().start(primaryStage);
        new MainMenuController(new User("u", "n", "p")).start(primaryStage);
    }
}
