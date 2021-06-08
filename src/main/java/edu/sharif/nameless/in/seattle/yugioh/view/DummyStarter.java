package edu.sharif.nameless.in.seattle.yugioh.view;

import edu.sharif.nameless.in.seattle.yugioh.controller.ProgramController;
import edu.sharif.nameless.in.seattle.yugioh.controller.menu.DuelMenuController;
import edu.sharif.nameless.in.seattle.yugioh.controller.menu.LoginMenuController;
import edu.sharif.nameless.in.seattle.yugioh.model.Game;
import edu.sharif.nameless.in.seattle.yugioh.model.Player.HumanPlayer;
import edu.sharif.nameless.in.seattle.yugioh.model.User;
import edu.sharif.nameless.in.seattle.yugioh.utils.DatabaseHandler;
import javafx.application.Application;
import javafx.stage.Stage;

public class DummyStarter extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            DatabaseHandler.importFromDatabase();
            new ProgramController();
            new LoginMenuController().cheatLogin("shayan1", "shayan.p1", "123");
            new LoginMenuController().cheatLogin("shayan2", "shayan.p2", "123");
            Game game = new Game(
                    new HumanPlayer(User.getUserByUsername("shayan1")),
                    new HumanPlayer(User.getUserByUsername("shayan2")),
                    3
            );
            new DuelMenuController(game);
            DuelMenuController.getInstance().getGraphicView().start(primaryStage);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
