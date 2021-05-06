import Utils.Parser;
import Utils.ParserException;
import Utils.Router;
import Utils.RoutingException;
import controller.*;
import model.ModelException;
import org.junit.Test;
import view.*;
import org.junit.jupiter.api.*;

public class Main {
    public static void main(String[] args) {
        Router.setCurrentMenu(new LoginMenu());
        while (true)
            Router.getCurrentMenu().runNextCommand();
    }

    @BeforeEach
    public void readyForBattle() throws ModelException, ParserException, RoutingException, LogicException {
        Router.setCurrentMenu(new LoginMenu());
        LoginMenuController.createUser(Context.getInstance(), "shayan", "Shayan.P", "1234");
        LoginMenuController.createUser(Context.getInstance(), "abolfazl", "Abolof", "12345");
        LoginMenuController.createUser(Context.getInstance(), "kasra", "KasaMaza", "123456");
        LoginMenuController.login(Context.getInstance(), "shayan", "1234");
        Assertions.assertEquals(Router.getCurrentMenu().getClass(), view.MainMenu.class);
        Router.navigateToMenu(ShopMenu.class);
        ShopMenuController.buyCard(Context.getInstance(), Parser.cardParser("axeraider"));
        ShopMenuController.buyCard(Context.getInstance(), Parser.cardParser("AxeRaider"));
        ShopMenuController.buyCard(Context.getInstance(), Parser.cardParser("BattleOx"));
        ShopMenuController.buyCard(Context.getInstance(), Parser.cardParser("BattleOx"));
        ShopMenuController.buyCard(Context.getInstance(), Parser.cardParser("BaTTleOX"));
        ShopMenuController.buyCard(Context.getInstance(), Parser.cardParser("battleox"));
        ShopMenuController.buyCard(Context.getInstance(), Parser.cardParser("fireyarou"));
        ShopMenuController.buyCard(Context.getInstance(), Parser.cardParser("silverfang"));
        Router.navigateToMenu(view.MainMenu.class);
        Router.navigateToMenu(DeckMenu.class);
        DeckMenuController.showAllCards(Context.getInstance());
        DeckMenuController.createDeck(Context.getInstance(), "myBigDeck");
        DeckMenuController.addCardToDeck(Context.getInstance(), Parser.cardParser("battleox"), Parser.deckParser("myBigDeck"), false);
        DeckMenuController.addCardToDeck(Context.getInstance(), Parser.cardParser("battleox"), Parser.deckParser("myBigDeck"), false);
        DeckMenuController.addCardToDeck(Context.getInstance(), Parser.cardParser("battleox"), Parser.deckParser("myBigDeck"), false);
        DeckMenuController.addCardToDeck(Context.getInstance(), Parser.cardParser("axeraider"), Parser.deckParser("myBigDeck"), false);
        DeckMenuController.addCardToDeck(Context.getInstance(), Parser.cardParser("axeraider"), Parser.deckParser("myBigDeck"), true);
        DeckMenuController.addCardToDeck(Context.getInstance(), Parser.cardParser("fireyarou"), Parser.deckParser("myBigDeck"), true);
        DeckMenuController.setActiveDeck(Context.getInstance(), Parser.deckParser("myBigDeck"));
        DeckMenuController.showAllDecks(Context.getInstance());
        Router.navigateToMenu(view.MainMenu.class);
        MainMenuController.logout(Context.getInstance());

        Assertions.assertEquals(Router.getCurrentMenu().getClass(), view.LoginMenu.class);
        LoginMenuController.login(Context.getInstance(), "abolfazl", "12345");
        Router.navigateToMenu(ShopMenu.class);
        ShopMenuController.buyCard(Context.getInstance(), Parser.cardParser("axeraider"));
        ShopMenuController.buyCard(Context.getInstance(), Parser.cardParser("AxeRaider"));
        ShopMenuController.buyCard(Context.getInstance(), Parser.cardParser("BattleOx"));
        ShopMenuController.buyCard(Context.getInstance(), Parser.cardParser("BattleOx"));
        ShopMenuController.buyCard(Context.getInstance(), Parser.cardParser("BaTTleOX"));
        ShopMenuController.buyCard(Context.getInstance(), Parser.cardParser("battleox"));
        ShopMenuController.buyCard(Context.getInstance(), Parser.cardParser("fireyarou"));
        ShopMenuController.buyCard(Context.getInstance(), Parser.cardParser("hornimp"));
        ShopMenuController.buyCard(Context.getInstance(), Parser.cardParser("hornimp"));
        ShopMenuController.buyCard(Context.getInstance(), Parser.cardParser("hornimp"));
        ShopMenuController.buyCard(Context.getInstance(), Parser.cardParser("hornimp"));
        ShopMenuController.buyCard(Context.getInstance(), Parser.cardParser("hornimp"));

        Router.navigateToMenu(view.MainMenu.class);
        Router.navigateToMenu(DeckMenu.class);
        DeckMenuController.showAllCards(Context.getInstance());
        DeckMenuController.createDeck(Context.getInstance(), "myBigDeck");
        DeckMenuController.addCardToDeck(Context.getInstance(), Parser.cardParser("battleox"), Parser.deckParser("myBigDeck"), false);
        DeckMenuController.addCardToDeck(Context.getInstance(), Parser.cardParser("battleox"), Parser.deckParser("myBigDeck"), false);
        DeckMenuController.addCardToDeck(Context.getInstance(), Parser.cardParser("battleox"), Parser.deckParser("myBigDeck"), false);
        DeckMenuController.addCardToDeck(Context.getInstance(), Parser.cardParser("axeraider"), Parser.deckParser("myBigDeck"), false);
        DeckMenuController.addCardToDeck(Context.getInstance(), Parser.cardParser("axeraider"), Parser.deckParser("myBigDeck"), true);
        DeckMenuController.addCardToDeck(Context.getInstance(), Parser.cardParser("fireyarou"), Parser.deckParser("myBigDeck"), true);
        DeckMenuController.addCardToDeck(Context.getInstance(), Parser.cardParser("hornimp"), Parser.deckParser("myBigDeck"), true);
        DeckMenuController.addCardToDeck(Context.getInstance(), Parser.cardParser("hornimp"), Parser.deckParser("myBigDeck"), true);
        DeckMenuController.setActiveDeck(Context.getInstance(), Parser.deckParser("myBigDeck"));
        DeckMenuController.setActiveDeck(Context.getInstance(), Parser.deckParser("myBigDeck"));
        DeckMenuController.setActiveDeck(Context.getInstance(), Parser.deckParser("myBigDeck"));
        DeckMenuController.setActiveDeck(Context.getInstance(), Parser.deckParser("myBigDeck"));

        DeckMenuController.showAllDecks(Context.getInstance());
        Router.navigateToMenu(view.MainMenu.class);
        MainMenuController.logout(Context.getInstance());

        LoginMenuController.login(Context.getInstance(), "shayan", "1234");
        Assertions.assertEquals(Router.getCurrentMenu().getClass(), MainMenu.class);
        MainMenuController.startNewDuel(Context.getInstance(), Parser.UserParser("abolfazl"), 3);
    }

    @Test
    public void duelTest1(){

    }

    @Test
    public void duelTest2(){

    }
}
