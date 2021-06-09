package edu.sharif.nameless.in.seattle.yugioh.view;

import edu.sharif.nameless.in.seattle.yugioh.controller.ProgramController;
import edu.sharif.nameless.in.seattle.yugioh.controller.events.DuelOverEvent;
import edu.sharif.nameless.in.seattle.yugioh.controller.events.RoundOverEvent;
import edu.sharif.nameless.in.seattle.yugioh.controller.menu.DuelMenuController;
import edu.sharif.nameless.in.seattle.yugioh.controller.menu.LoginMenuController;
import edu.sharif.nameless.in.seattle.yugioh.model.Game;
import edu.sharif.nameless.in.seattle.yugioh.model.Player.HumanPlayer;
import edu.sharif.nameless.in.seattle.yugioh.model.User;
import edu.sharif.nameless.in.seattle.yugioh.utils.DatabaseHandler;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.stage.Stage;

public class DummyStarter extends Application {

    @Override
    public void start(Stage primaryStage) {
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
            Thread gameControllerService = new Thread(new Task<Void>() {
                @Override
                protected Void call() {
                    try{
                        DuelMenuController.getInstance().control();
                    } catch (DuelOverEvent duelOverEvent){
                        System.out.println("game over!");
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    return null;
                }
            }, "my service thread");
            gameControllerService.setDaemon(true);
            gameControllerService.start();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
