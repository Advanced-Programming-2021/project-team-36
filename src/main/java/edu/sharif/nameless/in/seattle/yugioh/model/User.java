package edu.sharif.nameless.in.seattle.yugioh.model;

import edu.sharif.nameless.in.seattle.yugioh.model.card.Card;
import edu.sharif.nameless.in.seattle.yugioh.model.deck.Deck;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

public class User implements Serializable {
    private static final ArrayList<User> users = new ArrayList<>();

    private String username;
    private String password;
    private String nickname;
    private int score;
    private int balance;
    private final ArrayList<Card> cards;
    private final ArrayList<Deck> decks;
    private Deck activeDeck;

    public User(String username, String nickname, String password) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.score = Constants.InitialScore.val;
        this.balance = Constants.InitialMoney.val;
        this.cards = new ArrayList<>();
        this.decks = new ArrayList<>();
        this.activeDeck = null;
        this.save();
    }

    public static User getUserByUsername(String username) {
        for (User user : users)
            if (user.getUsername().equals(username))
                return user;
        return null;
    }

    public static User getUserByNickname(String nickname) {
        for (User user : users)
            if (user.getNickname().equals(nickname))
                return user;
        return null;
    }

    public static ArrayList<User> retrieveUsersBasedOnScore() {
        users.sort(Comparator.comparing(User::getScore).reversed().thenComparing(User::getNickname));
        return users;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public ArrayList<Deck> getDecks() {
        return decks;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public int getScore() {
        return score;
    }

    public int getBalance() {
        return balance;
    }

    public Deck getActiveDeck() {
        return activeDeck;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setActiveDeck(Deck deck) {
        this.activeDeck = deck;
    }

    public void increaseScore(Integer value) {
        score += value;
    }

    public void decreaseScore(Integer value) {
        score -= value;
    }

    public void increaseBalance(int value) {
        this.balance += value;
    }

    public void save() {
        users.add(this);
    }

    public void buy(Card card) throws ModelException {
        if (balance < card.getPrice())
            throw new ModelException("not enough money");
        balance -= card.getPrice();
        addCard(card);
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public int getCardFrequency(Card card) {
        int count = 0;
        for (Card c : cards)
            if (c.getName().equalsIgnoreCase(card.getName()))
                count++;
        return count;
    }

    public Deck getDeckByName(String deckName) {
        for (Deck deck : decks)
            if (deck.getName().equals(deckName))
                return deck;
        return null;
    }

    public void addDeck(Deck deck) {
        decks.add(deck);
    }

    public void deleteDeck(Deck deck) {
        decks.remove(deck);
    }

    public boolean authenticate(String password) {
        return this.password.equals(password);
    }
}
