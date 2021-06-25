package YuGiOh.view;

import YuGiOh.controller.GameController;
import YuGiOh.controller.ProgramController;
import YuGiOh.controller.QueryGameThread;
import YuGiOh.controller.menu.DuelMenuController;
import YuGiOh.controller.menu.LoginMenuController;
import YuGiOh.controller.player.AIPlayerController;
import YuGiOh.controller.player.AggressiveAIPlayerController;
import YuGiOh.controller.player.HumanPlayerController;
import YuGiOh.model.Game;
import YuGiOh.model.Player.AIPlayer;
import YuGiOh.model.Player.HumanPlayer;
import YuGiOh.model.User;
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
            Cheat.buildSuperUser(abolfazl);
            Cheat.buildSuperUser(fakeUser2);

            Game game = new Game(
                    new HumanPlayer(abolfazl),
                    new AIPlayer(),
                    3
            );
            new DuelMenuController(game);

            // todo remove this
            if(game.getFirstPlayer() instanceof AIPlayer)
                GameController.getInstance().setPlayerController1(new AggressiveAIPlayerController((AIPlayer) game.getFirstPlayer()));
            if(game.getSecondPlayer() instanceof AIPlayer)
                GameController.getInstance().setPlayerController2(new AggressiveAIPlayerController((AIPlayer) game.getSecondPlayer()));

            DuelMenuController.getInstance().getGraphicView().start(primaryStage);
            DuelMenuController.getInstance().control();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
