package YuGiOh;


import YuGiOh.controller.MediaPlayerController;
import YuGiOh.model.User;
import YuGiOh.utils.Cheat;
import YuGiOh.utils.DatabaseHandler;
import YuGiOh.view.menu.LoginMenuView;
import YuGiOh.view.menu.MainMenuView;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        initializeApp(primaryStage);

//        LoginMenuView.init(primaryStage);
//        new User("ad", "Ad", "w8");
//        new User("ad183", "nclad", "w8");
//        new User("19", "1398d", "w8");
//        new User("19831", "Adakd", "w8");
        User shayan  = new User("shayan", "shayan.p", "1234");
        User dummy = new User("dummy", "dum", "123");
        Cheat.buildSuperUser(shayan);
        Cheat.buildSuperUser(dummy);
        MainMenuView.init(primaryStage, shayan);
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

    public static void main(String[] args) {
        launch(args);
    }
}
