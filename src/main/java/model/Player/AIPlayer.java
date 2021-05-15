package model.Player;

import utils.CustomPrinter;
import model.ModelException;
import model.User;
import model.card.monsterCards.*;
import model.deck.Deck;

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
            user.buy(new AxeRaider());
            user.buy(new BattleOx());
            user.buy(new Fireyarou());
            user.buy(new HornImp());
            user.buy(new SilverFang());
        } catch (ModelException e) {
            CustomPrinter.println("error in constructing ai");
        }
        Deck deck = new Deck("aiDeck");
        deck.getMainDeck().addCard(new AxeRaider());
        deck.getMainDeck().addCard(new AxeRaider());
        deck.getSideDeck().addCard(new AxeRaider());
        deck.getMainDeck().addCard(new BattleOx());
        deck.getMainDeck().addCard(new BattleOx());
        deck.getSideDeck().addCard(new SilverFang());
        deck.getMainDeck().addCard(new HornImp());
        deck.getMainDeck().addCard(new SilverFang());
        deck.getSideDeck().addCard(new HornImp());

        user.addDeck(deck);
        user.setActiveDeck(deck);
        return user;
    }
}
