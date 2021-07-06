package YuGiOh.graphicView;

import YuGiOh.model.User;
import YuGiOh.utils.Cheat;
import YuGiOh.utils.DatabaseHandler;
import javafx.application.Application;
import javafx.stage.Stage;

public class KasraGraphicStuffBtwThisNameIsWayTooLongLOL extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setResizable(false);
        primaryStage.setTitle("YuGiOh!");
        DatabaseHandler.importFromDatabase();

        //LoginMenuView.init(primaryStage);
        //ProfileMenuView.init(primaryStage, new User("abi", "badi", "c"));
        new User("ad", "Ad", "w8");
        new User("ad183", "nclad", "w8");
        new User("19", "1398d", "w8");
        new User("19831", "Adakd", "w8");
        User shayan  = new User("shayan", "shayan.p", "1234");
        //Cheat.buildSuperUser(shayan);
        MainMenuView.init(primaryStage, shayan);
        //ScoreboardMenuView.init(primaryStage);
    }
}
