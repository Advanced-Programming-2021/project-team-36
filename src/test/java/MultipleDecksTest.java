import YuGiOh.controller.menu.DeckMenuController;
import YuGiOh.controller.menu.ShopMenuController;
import initialize.Sample;
import org.junit.Test;

public class MultipleDecksTest extends Sample {
    @Test
    public void oneCardInMultipleDecksTest(){
        initializeUser("newShayan", "123", "new shayan.p");
        run("user login -u newShayan -p 123");
        run("menu enter shop");
        checkCurrentMenu(ShopMenuController.class);
        run("shop buy blackpendant");
        checkNoInvalidCommandsInBuffer();
        run("menu enter main");
        run("menu enter deck");
        checkCurrentMenu(DeckMenuController.class);
        for(int i = 0; i < 10; i++) {
            String deckName = "deck" + i;
            run("deck create " + deckName);
            run("deck add-card --card " + "BlackPendant " + "--deck " + deckName);
        }
        checkNoInvalidCommandsInBuffer();
    }
}
