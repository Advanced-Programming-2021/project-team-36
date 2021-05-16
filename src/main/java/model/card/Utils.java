package model.card;

import utils.ClassFinder;

import java.util.*;

public class Utils {
    private static TreeMap<String, String> cardsData = new TreeMap();
    private static Class[] magicCardsClasses = ClassFinder.getClasses("model.card.magicCards");

    protected static void addCard(String type, String name) {
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

    public static Card[] getAllCards() {
        ArrayList<Card> cards = new ArrayList<>();
        cardsData.forEach((k, v) -> {
            cards.add(getCard(k));
        });
        cards.sort(new cardLexicographicalOrder());
        return cards.toArray(new Card[0]);
    }

    protected static String formatClassName(String name) {
        String[] elements = name.trim().replaceAll(",|-", "").split("\\s+");
        String formattedName = "";
        for (int i = 0; i < elements.length; i++)
            formattedName += elements[i].substring(0, 1).toUpperCase() + elements[i].substring(1, elements[i].length());
        return formattedName;
    }

    static class cardLexicographicalOrder implements Comparator<Card> {
        public int compare(Card card1, Card card2) {
            return card1.name.compareTo(card2.name);
        }
    }
}
