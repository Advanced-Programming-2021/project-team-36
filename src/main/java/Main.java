import Utils.*;
import controller.*;
import controller.events.GameEvent;
import controller.menu.*;
import model.ModelException;
import org.junit.Test;
import view.*;
import org.junit.jupiter.api.*;

public class Main {
    public static void main(String[] args) {
        ProgramController controller = new ProgramController();
        try {
            //getReady();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error in getting ready");
            System.exit(0);
        }
        controller.control();
    }

//    public static void getReady() throws ModelException, ParserException, RoutingException, LogicException {
//        if (ProgramController.getInstance() == null)
//            new ProgramController();
//        LoginMenuController.getInstance().createUser("shayan", "Shayan.P", "1234");
//        LoginMenuController.getInstance().createUser("abolfazl", "Abolof", "12345");
//        LoginMenuController.getInstance().createUser("kasra", "KasaMaza", "123456");
//        LoginMenuController.getInstance().login("shayan", "1234");
//        Assertions.assertEquals(ProgramController.getInstance().getCurrentController().getClass(), MainMenuController.class);
//        ProgramController.getInstance().navigateToMenu(ShopMenuController.class);
//        ShopMenuController.getInstance().buyCard(Parser.cardParser("axeraider"));
//        ShopMenuController.getInstance().buyCard(Parser.cardParser("AxeRaider"));
//        ShopMenuController.getInstance().buyCard(Parser.cardParser("AxeRaider"));
//        ShopMenuController.getInstance().buyCard(Parser.cardParser("AxeRaider"));
//        ShopMenuController.getInstance().buyCard(Parser.cardParser("AxeRaider"));
//        ShopMenuController.getInstance().buyCard(Parser.cardParser("BattleOx"));
//        ShopMenuController.getInstance().buyCard(Parser.cardParser("BattleOx"));
//        ShopMenuController.getInstance().buyCard(Parser.cardParser("BaTTleOX"));
//        ShopMenuController.getInstance().buyCard(Parser.cardParser("battleox"));
//        ShopMenuController.getInstance().buyCard(Parser.cardParser("fireyarou"));
//        ShopMenuController.getInstance().buyCard(Parser.cardParser("fireyarou"));
//        ShopMenuController.getInstance().buyCard(Parser.cardParser("fireyarou"));
//        ShopMenuController.getInstance().buyCard(Parser.cardParser("fireyarou"));
//        ShopMenuController.getInstance().buyCard(Parser.cardParser("hornimp"));
//        ShopMenuController.getInstance().buyCard(Parser.cardParser("hornimp"));
//        ShopMenuController.getInstance().buyCard(Parser.cardParser("hornimp"));
//        ShopMenuController.getInstance().buyCard(Parser.cardParser("hornimp"));
//        ShopMenuController.getInstance().buyCard(Parser.cardParser("hornimp"));
//        ProgramController.getInstance().navigateToMenu(MainMenuController.class);
//        ProgramController.getInstance().navigateToMenu(DeckMenuController.class);
//        DeckMenuController.getInstance().showAllCards();
//        DeckMenuController.getInstance().createDeck("myBigDeck");
//        DeckMenuController.getInstance().addCardToDeck(Parser.cardParser("battleox"), DeckMenuController.getInstance().deckParser("myBigDeck"), false);
//        DeckMenuController.getInstance().addCardToDeck(Parser.cardParser("battleox"), DeckMenuController.getInstance().deckParser("myBigDeck"), false);
//        DeckMenuController.getInstance().addCardToDeck(Parser.cardParser("battleox"), DeckMenuController.getInstance().deckParser("myBigDeck"), false);
//        DeckMenuController.getInstance().addCardToDeck(Parser.cardParser("axeraider"), DeckMenuController.getInstance().deckParser("myBigDeck"), false);
//        DeckMenuController.getInstance().addCardToDeck(Parser.cardParser("axeraider"), DeckMenuController.getInstance().deckParser("myBigDeck"), false);
//        DeckMenuController.getInstance().addCardToDeck(Parser.cardParser("axeraider"), DeckMenuController.getInstance().deckParser("myBigDeck"), true);
//        DeckMenuController.getInstance().addCardToDeck(Parser.cardParser("fireyarou"), DeckMenuController.getInstance().deckParser("myBigDeck"), true);
//        DeckMenuController.getInstance().addCardToDeck(Parser.cardParser("fireyarou"), DeckMenuController.getInstance().deckParser("myBigDeck"), false);
//        DeckMenuController.getInstance().addCardToDeck(Parser.cardParser("fireyarou"), DeckMenuController.getInstance().deckParser("myBigDeck"), false);
//        DeckMenuController.getInstance().setActiveDeck(DeckMenuController.getInstance().deckParser("myBigDeck"));
//        DeckMenuController.getInstance().showAllDecks();
//        ProgramController.getInstance().navigateToMenu(MainMenuController.class);
//        MainMenuController.getInstance().logout();
//
//        Assertions.assertEquals(ProgramController.getInstance().getCurrentController().getClass(), LoginMenuController.class);
//        LoginMenuController.getInstance().login("abolfazl", "12345");
//        ProgramController.getInstance().navigateToMenu(ShopMenuController.class);
//        ShopMenuController.getInstance().buyCard(Parser.cardParser("axeraider"));
//        ShopMenuController.getInstance().buyCard(Parser.cardParser("AxeRaider"));
//        ShopMenuController.getInstance().buyCard(Parser.cardParser("AxeRaider"));
//        ShopMenuController.getInstance().buyCard(Parser.cardParser("AxeRaider"));
//        ShopMenuController.getInstance().buyCard(Parser.cardParser("AxeRaider"));
//        ShopMenuController.getInstance().buyCard(Parser.cardParser("BattleOx"));
//        ShopMenuController.getInstance().buyCard(Parser.cardParser("BattleOx"));
//        ShopMenuController.getInstance().buyCard(Parser.cardParser("BaTTleOX"));
//        ShopMenuController.getInstance().buyCard(Parser.cardParser("battleox"));
//        ShopMenuController.getInstance().buyCard(Parser.cardParser("fireyarou"));
//        ShopMenuController.getInstance().buyCard(Parser.cardParser("fireyarou"));
//        ShopMenuController.getInstance().buyCard(Parser.cardParser("fireyarou"));
//        ShopMenuController.getInstance().buyCard(Parser.cardParser("fireyarou"));
//        ShopMenuController.getInstance().buyCard(Parser.cardParser("hornimp"));
//        ShopMenuController.getInstance().buyCard(Parser.cardParser("hornimp"));
//        ShopMenuController.getInstance().buyCard(Parser.cardParser("hornimp"));
//        ShopMenuController.getInstance().buyCard(Parser.cardParser("hornimp"));
//        ShopMenuController.getInstance().buyCard(Parser.cardParser("hornimp"));
//
//        ProgramController.getInstance().navigateToMenu(MainMenuController.class);
//        ProgramController.getInstance().navigateToMenu(DeckMenuController.class);
//        DeckMenuController.getInstance().showAllCards();
//        DeckMenuController.getInstance().createDeck("myBigDeck");
//        DeckMenuController.getInstance().addCardToDeck(Parser.cardParser("battleox"), DeckMenuController.getInstance().deckParser("myBigDeck"), false);
//        DeckMenuController.getInstance().addCardToDeck(Parser.cardParser("battleox"), DeckMenuController.getInstance().deckParser("myBigDeck"), false);
//        DeckMenuController.getInstance().addCardToDeck(Parser.cardParser("battleox"), DeckMenuController.getInstance().deckParser("myBigDeck"), false);
//        DeckMenuController.getInstance().addCardToDeck(Parser.cardParser("axeraider"), DeckMenuController.getInstance().deckParser("myBigDeck"), false);
//        DeckMenuController.getInstance().addCardToDeck(Parser.cardParser("axeraider"), DeckMenuController.getInstance().deckParser("myBigDeck"), false);
//        DeckMenuController.getInstance().addCardToDeck(Parser.cardParser("axeraider"), DeckMenuController.getInstance().deckParser("myBigDeck"), true);
//        DeckMenuController.getInstance().addCardToDeck(Parser.cardParser("fireyarou"), DeckMenuController.getInstance().deckParser("myBigDeck"), true);
//        DeckMenuController.getInstance().addCardToDeck(Parser.cardParser("fireyarou"), DeckMenuController.getInstance().deckParser("myBigDeck"), false);
//        DeckMenuController.getInstance().addCardToDeck(Parser.cardParser("fireyarou"), DeckMenuController.getInstance().deckParser("myBigDeck"), false);
//        DeckMenuController.getInstance().setActiveDeck(DeckMenuController.getInstance().deckParser("myBigDeck"));
//        DeckMenuController.getInstance().setActiveDeck(DeckMenuController.getInstance().deckParser("myBigDeck"));
//        DeckMenuController.getInstance().setActiveDeck(DeckMenuController.getInstance().deckParser("myBigDeck"));
//        DeckMenuController.getInstance().setActiveDeck(DeckMenuController.getInstance().deckParser("myBigDeck"));
//
//        DeckMenuController.getInstance().showAllDecks();
//        ProgramController.getInstance().navigateToMenu(MainMenuController.class);
//        MainMenuController.getInstance().logout();
//
//        LoginMenuController.getInstance().login("shayan", "1234");
//        Assertions.assertEquals(ProgramController.getInstance().getCurrentController().getClass(), MainMenuController.class);
//    }
//
//    @Test
//    public void test() throws ModelException, ParserException, RoutingException, LogicException, GameEvent {
//        getReady();
//
//        MainMenuController.getInstance().startNewDuel(Parser.UserParser("abolfazl"), 3);
//
//        DuelMenuController.getInstance().showHand();
//        CardSelector.getInstance().selectCard(Parser.cardAddressParser("hand", "1", false));
//        CardSelector.getInstance().showSelectedCard();
//        DuelMenuController.getInstance().goNextPhase();
//        DuelMenuController.getInstance().summonCard();
//        DuelMenuController.getInstance().showBoard();
//        DuelMenuController.getInstance().showHand();
//        CardSelector.getInstance().selectCard(Parser.cardAddressParser("hand", "2", false));
//        DuelMenuController.getInstance().goNextPhase();
//        CardSelector.getInstance().showSelectedCard();
//        DuelMenuController.getInstance().goNextPhase();
//        DuelMenuController.getInstance().showHand();
//        DuelMenuController.getInstance().goNextPhase();
//        DuelMenuController.getInstance().goNextPhase();
//        CardSelector.getInstance().selectCard(Parser.cardAddressParser("hand", "5", false));
//        DuelMenuController.getInstance().summonCard();
//        DuelMenuController.getInstance().showBoard();
//        DuelMenuController.getInstance().goNextPhase();
//        CardSelector.getInstance().selectCard(Parser.cardAddressParser("monster", "1", false));
//        DuelMenuController.getInstance().attack(1);
//    }
//
//    @Test
//    public void doubleAITest() throws ModelException, ParserException, RoutingException, LogicException, GameEvent {
//        if (ProgramController.getInstance() == null)
//            new ProgramController();
//        LoginMenuController.getInstance().createUser("shayan", "Shayan.P", "1234");
//        LoginMenuController.getInstance().login("shayan", "1234");
//        MainMenuController.getInstance().startDuelDoubleAI(3);
//        ProgramController.getInstance().control();
//    }
}