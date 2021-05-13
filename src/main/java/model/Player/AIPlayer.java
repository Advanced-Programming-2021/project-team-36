package model.Player;

import controller.DuelMenuController;
import controller.LogicException;
import model.Game;
import model.ModelException;
import model.User;
import model.card.monsterCards.*;
import model.deck.Deck;

public class AIPlayer extends Player {
    public AIPlayer() throws ModelException {
        super(getAIUser());
    }
    private static User getAIUser() {
        User user = User.getUserByUsername("artificial_intelligence");
        if(user != null)
            return user;
        user = new User("artificial_intelligence", "Mr.AI", "thisIsAStrongPassword");
        try {
            user.buy(new AxeRaider());
            user.buy(new BattleOx());
            user.buy(new Fireyarou());
            user.buy(new HornImp());
            user.buy(new SilverFang());
        } catch (ModelException e) {
            System.out.println("error in constructing ai");
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
    public void play(Game game) throws LogicException {
        DuelMenuController.getInstance().goNextPhase();
        // change this!
    }
}
