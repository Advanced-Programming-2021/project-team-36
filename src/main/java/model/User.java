package model;

import model.card.Card;
import model.deck.Deck;

import java.util.ArrayList;

public class User {
    private static ArrayList<User> users = new ArrayList<>();

    private String username;
    private String password;
    private String nickname;
    private Integer score;
    private ArrayList<Card> cards = new ArrayList<>();
    private ArrayList<Deck> decks = new ArrayList<>();

    User(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        users.add(this);
    }

    public static User getUserByUsername(String username) {
        for (User user : users)
            if (user.getUsername().equals(username))
                return user;
        return null;
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

    public Integer getScore() {
        return score;
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

    public void increaseScore(Integer value) {
        score += value;
    }

    public void decreaseScore(Integer value) {
        score -= value;
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
        decks.removeIf(deck1 -> deck1.equals(deck));
    }
}
