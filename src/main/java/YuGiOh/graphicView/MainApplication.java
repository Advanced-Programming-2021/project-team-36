package YuGiOh.graphicView;


import YuGiOh.controller.menus.MediaPlayerController;
import YuGiOh.model.User;
import YuGiOh.utils.Cheat;
import YuGiOh.utils.DatabaseHandler;
import YuGiOh.view.MainMenuView;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setResizable(false);
        primaryStage.setTitle("YuGiOh!");
        DatabaseHandler.importFromDatabase();
        new MediaPlayerController();

        //LoginMenuView.init(primaryStage);
        new User("ad", "Ad", "w8");
        new User("ad183", "nclad", "w8");
        new User("19", "1398d", "w8");
        new User("19831", "Adakd", "w8");
        User shayan  = new User("shayan", "shayan.p", "1234");
        Cheat.buildSuperUser(shayan);
        /*Card card = Utils.getCard("Suijin");
        card.setDescription("this is a shit card");
        card.setName("The Modern Suijin");
        try {
            Utils.addCardToInvented(card);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        //Cheat.buildSuperUserWithManyOfThisCards(shayan, 20, "BlackPendant", "AxeRaider");
        MainMenuView.init(primaryStage, shayan);

        primaryStage.setOnCloseRequest(e->{
            DatabaseHandler.exportToDatabase();
        });
    }
}
