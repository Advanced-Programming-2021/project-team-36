package model;

import model.card.Card;
import model.deck.Deck;

import java.util.ArrayList;
import java.util.Comparator;

public class User {
    private static ArrayList<User> users = new ArrayList<>();
    private static User currentUser;

    private String username;
    private String password;
    private String nickname;
    private int score;
    private int balance;
    private ArrayList<Card> cards = new ArrayList<>();
    private ArrayList<Deck> decks = new ArrayList<>();

    public User(String username, String password, String nickname) {
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

    public static User getUserByNickname(String nickname) {
        for (User user : users)
            if (user.getNickname().equals(nickname))
                return user;
        return null;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static ArrayList<User> retrieveUsersBasedOnScore() {
        users.sort(new scoreBasedComparator());
        return users;
    }

    public ArrayList<Card> getCardsLexicographically() {
        // TODO
        return new ArrayList<>();
    }

    public ArrayList<Deck> getDecksLexicographically() {
        // TODO
        return new ArrayList<>();
    }

    public static String checkValidity(String username, String nickname, String password) {
        // TODO :
        return "TODO";
    }

    public static boolean authenticateUser(User user, String password) {
        return user != null && user.password.equals(password);
    }

    public static void logIn(User user, String password) {
        assert user != null && user.password.equals(password);
        User.currentUser = user;
    }

    public static void logOut() {
        User.currentUser = null;
    }

    private static class scoreBasedComparator implements Comparator<User> {
        public int compare(User user1, User user2) {
            if (user1.score != user2.score)
                return (user1.score < user2.score ? 1 : -1);
            return user1.nickname.compareTo(user2.nickname);
        }
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
        // TODO
        return new Deck("");
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
        // TODO
    }

    public void increaseScore(Integer value) {
        score += value;
    }

    public void decreaseScore(Integer value) {
        score -= value;
    }

    public void buy(Card card) {
        balance -= card.getPrice();
        addCard(card);
    }

    public void addCard(Card card) {
        // TODO
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

    public boolean authenticate(String password) {
        return this.password.equals(password);
    }
}
