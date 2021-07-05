package YuGiOh.view;

import YuGiOh.controller.GameController;
import YuGiOh.controller.MainGameThread;
import YuGiOh.controller.ProgramController;
import YuGiOh.controller.menu.DuelMenuController;
import YuGiOh.controller.menu.LoginMenuController;
import YuGiOh.controller.player.AggressiveAIPlayerController;
import YuGiOh.controller.player.HumanPlayerController;
import YuGiOh.model.Duel;
import YuGiOh.model.Game;
import YuGiOh.model.Player.AIPlayer;
import YuGiOh.model.Player.HumanPlayer;
import YuGiOh.model.User;
import YuGiOh.model.card.Card;
import YuGiOh.model.enums.AIMode;
import YuGiOh.utils.Cheat;
import YuGiOh.utils.DatabaseHandler;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Random;

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

            Card allCards[] = YuGiOh.model.card.Utils.getAllCards();
            Random random = new Random();
            int length = 1;
            String some_cards1[] = new String[length];
            String some_cards2[] = new String[length];
            for (int i = 0; i < length; i ++) {
                some_cards1[i] = "AxeRaider";//allCards[random.nextInt(allCards.length)].getName();
                some_cards2[i] = "AxeRaider";//allCards[random.nextInt(allCards.length)].getName();
            }
            Cheat.buildSuperUserWithManyOfThisCards(fakeUser2, 40, some_cards1);
            Cheat.buildSuperUserWithManyOfThisCards(abolfazl, 40, some_cards2);
            //Cheat.buildSuperUserWithManyOfThisCards(fakeUser2, 40, "AxeRaider", "TheTricky", "MonsterReborn", "ManEaterBug");
            //Cheat.buildSuperUserWithManyOfThisCards(abolfazl, 40, "AxeRaider", "TheTricky", "MonsterReborn", "ManEaterBug");
//            Cheat.buildSuperUser(fakeUser2);

            Duel duel = new Duel(
                    //new AIPlayer(abolfazl),
                    new AIPlayer(fakeUser2, AIMode.AGGRESSIVE),
                    new HumanPlayer(abolfazl),
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
