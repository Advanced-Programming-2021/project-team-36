package YuGiOh.view;

import YuGiOh.controller.ProgramController;
import YuGiOh.controller.menu.DuelMenuController;
import YuGiOh.controller.menu.LoginMenuController;
import YuGiOh.model.Game;
import YuGiOh.model.Player.AIPlayer;
import YuGiOh.model.Player.HumanPlayer;
import YuGiOh.model.User;
import YuGiOh.utils.DatabaseHandler;
import javafx.application.Application;
import javafx.stage.Stage;

public class DummyStarter extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            DatabaseHandler.importFromDatabase();
            new ProgramController();
            new LoginMenuController().cheatLogin("shayan1", "shayan.p1", "123");
//            new LoginMenuController().cheatLogin("shayan2", "shayan.p2", "123");
            Game game = new Game(
                    new HumanPlayer(User.getUserByUsername("shayan1")),
                    new AIPlayer(),
//                    new HumanPlayer(User.getUserByUsername("shayan2")),
                    3
            );
            new DuelMenuController(game);
            DuelMenuController.getInstance().getGraphicView().start(primaryStage);
            DuelMenuController.getInstance().control();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
