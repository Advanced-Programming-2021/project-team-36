package YuGiOh.graphicView;

import YuGiOh.model.User;
import javafx.application.Application;
import javafx.stage.Stage;

public class KasraGraphicStuffBtwThisNameIsWayTooLongLOL extends Application {

    @Override
    public void start(Stage primaryStage) {
        //new LoginMenuController().start(primaryStage);
        //new MainMenuController(new User("u", "n", "p")).start(primaryStage);
        //new LoginMenuView().start(primaryStage);
        //LoginMenuView.init(primaryStage);
        //ProfileMenuView.init(primaryStage, new User("abi", "badi", "c"));
        new User("ad", "Ad", "w8");
        new User("ad183", "nclad", "w8");
        new User("19", "1398d", "w8");
        new User("19831", "Adakd", "w8");
        MainMenuView.init(primaryStage, new User("u", "n", "p"));
        //ScoreboardMenuView.init(primaryStage);
    }
}
