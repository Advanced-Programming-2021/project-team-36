package YuGiOh.archive.view;

import YuGiOh.controller.ProgramController;
import YuGiOh.archive.menu.LoginMenuController;
import YuGiOh.controller.menus.MainMenuController;
import YuGiOh.view.DuelMenuView;
import YuGiOh.model.User;
import YuGiOh.model.card.Card;
import YuGiOh.model.enums.AIMode;
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
//            Cheat.buildSuperUserWithManyOfThisCards(fakeUser2, 40, some_cards1);
//            Cheat.buildSuperUserWithManyOfThisCards(abolfazl, 40, some_cards2);
//            Cheat.buildSuperUserWithManyOfThisCards(fakeUser2, 40, "AxeRaider", "TheTricky", "MonsterReborn", "ManEaterBug");
//            Cheat.buildSuperUserWithManyOfThisCards(abolfazl, 40, "MagicJammer", "CrabTurtle", "SkullGuardian");
//            Cheat.buildSuperUser(fakeUser2);

//            new MainMenuController(abolfazl).startNewDuel(fakeUser2, 3);
            new MainMenuController(abolfazl).startDuelWithAI(true, 3, AIMode.AGGRESSIVE);
            DuelMenuView.init(primaryStage);

            primaryStage.setFullScreen(true);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
