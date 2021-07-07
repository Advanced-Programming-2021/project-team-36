package YuGiOh.view;


import YuGiOh.controller.menus.*;
import YuGiOh.model.User;
import YuGiOh.model.card.Utils;
import YuGiOh.utils.Cheat;
import YuGiOh.utils.DatabaseHandler;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        initializeApp(primaryStage);

        LoginMenuView.init(primaryStage);
//        new User("ad", "Ad", "w8");
//        new User("ad183", "nclad", "w8");
//        new User("19", "1398d", "w8");
//        new User("19831", "Adakd", "w8");
//        User shayan  = new User("shayan", "shayan.p", "1234");
//        Cheat.buildSuperUser(shayan);
//        MainMenuView.init(primaryStage, shayan);
    }

    private void initializeApp(Stage primaryStage) {
        primaryStage.setResizable(false);
        primaryStage.setTitle("YuGiOh!");
        DatabaseHandler.importFromDatabase();
        new MediaPlayerController();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                DatabaseHandler.exportToDatabase();
            }
        });
    }
}
