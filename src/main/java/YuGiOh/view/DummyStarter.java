package YuGiOh.view;

import YuGiOh.controller.GameController;
import YuGiOh.controller.MainGameThread;
import YuGiOh.controller.ProgramController;
import YuGiOh.controller.menu.DuelMenuController;
import YuGiOh.controller.menu.LoginMenuController;
import YuGiOh.controller.player.AggressiveAIPlayerController;
import YuGiOh.model.Duel;
import YuGiOh.model.Game;
import YuGiOh.model.Player.AIPlayer;
import YuGiOh.model.Player.HumanPlayer;
import YuGiOh.model.User;
import YuGiOh.model.enums.AIMode;
import YuGiOh.utils.Cheat;
import YuGiOh.utils.DatabaseHandler;
import javafx.application.Application;
import javafx.stage.Stage;

public class DummyStarter extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            DatabaseHandler.importFromDatabase();
            new ProgramController();
            new LoginMenuController().cheatLogin("Abolfazl", "Abolfazl.s", "123");
            new LoginMenuController().cheatLogin("shayan2", "shayan.p2", "123");

            User abolfazl = User.getUserByUsername("Abolfazl");
            User fakeUser2 = new User("magool", "magool.m", "123");
            Cheat.buildSuperUserWithManyOfThisCards(fakeUser2, 40, "AxeRaider", "TheTricky", "BlackPendant");
            Cheat.buildSuperUserWithManyOfThisCards(abolfazl, 40, "ManEaterBug");
//            Cheat.buildSuperUser(fakeUser2);

            Duel duel = new Duel(
//                    new HumanPlayer(abolfazl),
                    new AIPlayer(abolfazl, AIMode.AGGRESSIVE),
                    new AIPlayer(fakeUser2, AIMode.NORMAL),
                    3
            );
            new DuelMenuController(duel);

            // todo how to set aggressive AI player now?

            DuelMenuController.getInstance().getGraphicView().start(primaryStage);

            new MainGameThread(()->{
                DuelMenuController.getInstance().control();
            }).start();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
