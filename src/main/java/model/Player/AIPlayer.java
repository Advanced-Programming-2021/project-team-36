package model.Player;

import utils.CustomPrinter;
import model.ModelException;
import model.User;
import model.card.monsterCards.*;
import model.deck.Deck;
import view.Parser;
import view.ParserException;

import java.util.Random;

public class AIPlayer extends Player {
    public AIPlayer() throws ModelException {
        super(getAIUser());
    }
    private static User getAIUser() {
        User user = User.getUserByUsername("artificial_intelligence");
        if(user != null)
            return user;
        user = new User("artificial_intelligence" + new Random().nextInt(), "Mr.AI" + new Random().nextInt(), "thisIsAStrongPassword");
        try {
            user.buy(Parser.cardParser("AxeRaider"));
            user.buy(Parser.cardParser("BattleOx"));
            user.buy(Parser.cardParser("Suijin"));
            user.buy(Parser.cardParser("HornImp"));
            user.buy(Parser.cardParser("SilverFang"));
            user.buy(Parser.cardParser("Scanner"));
            user.buy(Parser.cardParser("TexChanger"));

            Deck deck = new Deck("aiDeck");
            deck.getMainDeck().addCard(Parser.cardParser("AxeRaider"));
            deck.getMainDeck().addCard(Parser.cardParser("BattleOx"));
            deck.getMainDeck().addCard(Parser.cardParser("Suijin"));
            deck.getMainDeck().addCard(Parser.cardParser("HornImp"));
            deck.getMainDeck().addCard(Parser.cardParser("SilverFang"));
            deck.getMainDeck().addCard(Parser.cardParser("AxeRaider"));
            deck.getMainDeck().addCard(Parser.cardParser("Scanner"));
            deck.getMainDeck().addCard(Parser.cardParser("TexChanger"));

            user.addDeck(deck);
            user.setActiveDeck(deck);

        } catch (ModelException | ParserException e) {
            e.printStackTrace();
        }
        return user;
    }
}
