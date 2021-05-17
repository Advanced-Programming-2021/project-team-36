package model.card;

import model.enums.MonsterAttribute;
import model.enums.MonsterCardType;
import model.enums.MonsterType;
import utils.ClassFinder;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Utils {
    private static TreeMap<String, String> cardsData = new TreeMap();
    private static Class[] magicCardsClasses = ClassFinder.getClasses("model.card.magicCards");
    private static TreeMap<String, TreeMap<String, String>> monstersData = new TreeMap<>();
    private static Class[] specialMonstersClasses = ClassFinder.getClasses("model.card.monsterCards");

    protected static void addCard(String type, String name) {
        cardsData.put(name, type);
    }

    public static String correctIgnoreCase(String name){
        for (Map.Entry<String,String> e : cardsData.entrySet()) {
            if (e.getKey().equalsIgnoreCase(name))
                return e.getKey();
        }
        return null;
    }

    public static Card getCard(String name) {
        if(name == null)
            return null;
        name = correctIgnoreCase(name);
        if(name == null)
            return null;
        if (!cardsData.containsKey(name))
            return null;
        if (cardsData.get(name).equals("Monster"))
            return getMonster(name);
        for (Class magicCardClass : magicCardsClasses)
            if (magicCardClass.getName().replaceAll(".*\\.", "").equals(Utils.formatClassName(name)))
                try {
                    return (Magic) magicCardClass.getConstructor().newInstance();
                } catch (Exception exception) {
                    return null;
                }
        return null;
    }

    public static Monster getMonster(String name) {
        // todo remove this? @Kasra. Why? @Shayan?
        name = Utils.formatClassName(name);
        TreeMap<String, String> monsterData = monstersData.get(name);
        if (specialMonstersClasses != null)
            for (Class specialMonsterClass : specialMonstersClasses) {
                if (specialMonsterClass.getName().replaceAll(".*\\.", "").equals(name))
                    try {
                        return (Monster) specialMonsterClass.getConstructors()[0].newInstance(
                                monsterData.get("Name"),
                                monsterData.get("Description"),
                                Integer.parseInt(monsterData.get("Price")),
                                Integer.parseInt(monsterData.get("Atk")),
                                Integer.parseInt(monsterData.get("Def")),
                                MonsterAttribute.valueOf(monsterData.get("Attribute")),
                                MonsterType.valueOf(monsterData.get("Monster Type")),
                                MonsterCardType.valueOf(monsterData.get("Card Type")),
                                Integer.parseInt(monsterData.get("Level")));
                    } catch (Exception exception) {
                        return null;
                    }
            }
        /*if (!monsterData.get("Card Type").equals("Normal"))
            return null;*/ // TODO : Temporarily commented this out but it should be present in production.
        return new Monster(monsterData.get("Name"),
                monsterData.get("Description"),
                Integer.parseInt(monsterData.get("Price")),
                Integer.parseInt(monsterData.get("Atk")),
                Integer.parseInt(monsterData.get("Def")),
                MonsterAttribute.valueOf(monsterData.get("Attribute")),
                MonsterType.valueOf(monsterData.get("Monster Type")),
                MonsterCardType.valueOf(monsterData.get("Card Type")),
                Integer.parseInt(monsterData.get("Level")));
    }

    public static void addMonsterData(TreeMap<String, String> monsterData) {
        monsterData.put("Name", Utils.formatClassName(monsterData.get("Name")));
        monsterData.put("Attribute", monsterData.get("Attribute").toUpperCase());
        monsterData.put("Monster Type", monsterData.get("Monster Type").toUpperCase().replaceAll("-|\\s", ""));
        monsterData.put("Card Type", monsterData.get("Card Type").toUpperCase());
        monstersData.put(monsterData.get("Name"), monsterData);
        Utils.addCard("Monster", monsterData.get("Name"));
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
