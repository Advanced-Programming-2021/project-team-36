package model.card;

import model.Player.Player;

import java.util.TreeMap;
import utils.ClassFinder;

public abstract class Card implements Comparable<Card>, Cloneable{
    protected String name;
    protected String description;
    protected int price;
    public Player owner;
    boolean isInBattle;
    private static TreeMap<String, String> cardsData = new TreeMap();
    private static Class[] magicCardsClasses = ClassFinder.getClasses("model.card.magicCards");

    {
        owner = null;
        isInBattle = false;
    }

    protected Card(String name, String description, int price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public static void addCard(String type, String name) {
        cardsData.put(name, type);
    }

    public static Card getCard(String name) {
        if (!cardsData.containsKey(name))
            return null;
        if (cardsData.get(name).equals("Monster"))
            return Monster.getMonster(name);
        for (Class magicCardClass : magicCardsClasses)
            if (magicCardClass.getName().replaceAll(".*\\.", "").equals(Utils.formatClassName(name)))
                try {
                    return (Magic) magicCardClass.getConstructor().newInstance();
                } catch (Exception exception) {
                    return null;
                }
        return null;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public Card readyForBattle(Player owner){
        isInBattle = true;
        this.owner = owner;
        return this;
    }

    @Override
    public Card clone() {
        try {
            Card cloned = (Card) super.clone();
            cloned.name = name;
            cloned.description = description;
            cloned.price = price;
            return cloned;
        } catch (CloneNotSupportedException e){
            e.printStackTrace();
            return null;
        }
    }

    public void save() {
        // TODO
    }

    @Override
    public String toString() {
        return name + ":" + description;
    }

    @Override
    public int compareTo(Card other) {
        return this.name.compareTo(other.name);
    }
}