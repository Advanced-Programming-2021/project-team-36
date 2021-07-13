package YuGiOh.model.card;

import YuGiOh.model.exception.LogicException;
import YuGiOh.model.enums.*;
import YuGiOh.utils.ClassFinder;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.util.*;

public class Utils {
    private static final TreeMap<String, String> cardsData = new TreeMap<>();
    private static final Class<?>[] magicCardsClasses = ClassFinder.getClasses("YuGiOh.model.card.magicCards");
    private static final TreeMap<String, TreeMap<String, String>> monstersData = new TreeMap<>();
    private static final TreeMap<String, TreeMap<String, String>> magicData = new TreeMap<>();
    private static final Class<?>[] specialMonstersClasses = ClassFinder.getClasses("YuGiOh.model.card.monsterCards");
    private static final TreeMap<String, Card> inventedCards = new TreeMap<>();

    protected static void addCard(String type, String name) {
        cardsData.put(name, type);
    }

    private static String ignoreSpaces(String name) {
        return name.replaceAll("\\s+", "");
    }
    public static String correctIgnoreCase(String name){
        name = ignoreSpaces(name);
        for (Map.Entry<String,String> e : cardsData.entrySet()) {
            if (ignoreSpaces(e.getKey()).equalsIgnoreCase(name))
                return e.getKey();
        }
        for (Map.Entry<String,Card> e : inventedCards.entrySet()) {
            if(ignoreSpaces(e.getKey()).equalsIgnoreCase(name))
                return e.getKey();
        }
        return null;
    }

    public static Card getCard(String name) {
        if(name == null)
            return null;
        name = correctIgnoreCase(name);
        if (inventedCards.containsKey(name))
            return inventedCards.get(name).clone();
        if (!cardsData.containsKey(name))
            return null;
        if (cardsData.get(name).equals("Monster"))
            return getMonster(name);
        else
            return getMagic(name);
    }

    public static ArrayList<Card> getInventedCards() {
        ArrayList<Card> ret = new ArrayList<>();
        inventedCards.forEach((name, card)->{
            ret.add(card.clone());
        });
        return ret;
    }

    public static void addCardToInvented(Card card) throws LogicException {
        if(checkCardExistInDatabase(card.getName()))
            throw new LogicException("The card name is repeated");
        inventedCards.put(card.getName(), card);
    }

    public static void removeCardFromInvented(Card card) throws LogicException {
        String name = correctIgnoreCase(card.getName());
        if(name == null)
            throw new LogicException("no such card!");
        inventedCards.remove(name);
    }
    public static boolean checkCardExistInDatabase(String name) {
        if(name == null)
            return false;
        return getCard(correctIgnoreCase(name)) != null;
    }

    public static Magic getMagic(String name) {
        for (Class<?> magicCardClass : magicCardsClasses) {
            if (magicCardClass.getName().replaceAll(".*\\.", "").equals(Utils.formatClassName(name))) {
                try {
                    name = Utils.formatClassName(name);
                    TreeMap<String, String> magicData = Utils.magicData.get(name);
                    return (Magic) magicCardClass.getConstructors()[0].newInstance(
                            magicData.get("Name"),
                            magicData.get("Description"),
                            Integer.parseInt(magicData.get("Price")),
                            Icon.valueOf(magicData.get("Icon")),
                            Status.valueOf(magicData.get("Status"))
                    );
                } catch (Exception exception) {
                    exception.printStackTrace();
                    throw new Error("error in initiating from magic class");
                }
            }
        }
        return null;
    }

    public static Monster getMonster(String name) {
        name = Utils.formatClassName(name);
        TreeMap<String, String> monsterData = monstersData.get(name);
        if (specialMonstersClasses != null)
            for (Class<?> specialMonsterClass : specialMonstersClasses) {
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
                        exception.printStackTrace();
                        throw new Error("error in initiating from monster class");
                    }
            }
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
        Utils.monstersData.put(monsterData.get("Name"), monsterData);
        Utils.addCard("Monster", monsterData.get("Name"));
    }

    public static void addMagicData(TreeMap<String, String> magicData) {
        magicData.put("Name", Utils.formatClassName(magicData.get("Name")));
        magicData.put("Icon", magicData.get("Icon").toUpperCase().replaceAll("-|\\s", ""));
        magicData.put("Status", magicData.get("Status").toUpperCase());
        Utils.magicData.put(magicData.get("Name"), magicData);
        Utils.addCard("Magic", magicData.get("Name"));
    }


    public static Card[] getAllCards() {
        ArrayList<Card> cards = new ArrayList<>();
        cardsData.forEach((k, v) -> {
            if(getCard(k) != null)
                cards.add(getCard(k));
        });
        cards.sort(new cardLexicographicalOrder());
        return cards.toArray(new Card[0]);
    }

    public static void checkIfAllImplementedCardsAreOk(){
        for(Class<?> magicClass : magicCardsClasses){
            String[] strings = magicClass.getName().split("\\.");
            String name = strings[strings.length-1];
            if(getCard(name) == null)
                throw new Error("Could not find " + name);
            if(getMagic(name) == null)
                throw new Error("Could not find " + name);
        }
        for(Class<?> monsterClass : specialMonstersClasses){
            String[] strings = monsterClass.getName().split("\\.");
            String name = strings[strings.length-1];

            // todo this was strange. classloader returned this non existence class
            if(name.equals("TheTricky$1"))
                continue;

            if(getCard(name) == null)
                throw new Error("Could not find " + name);
            if(getMonster(name) == null)
                throw new Error("Could not find " + name);
        }
    }

    protected static String formatClassName(String name) {
        String[] elements = name.trim().replaceAll("[,\\-']", "").split("\\s+");
        String formattedName = "";
        for (int i = 0; i < elements.length; i++)
            formattedName += elements[i].substring(0, 1).toUpperCase() + elements[i].substring(1, elements[i].length());
        return formattedName;
    }

    public static Image getCardImageView(Card card) {
        try {
            if (card instanceof Monster)
                return new Image(new FileInputStream(String.format("assets/Cards/Monsters/%s.jpg", card.getName())));
            else
                return new Image(new FileInputStream(String.format("assets/Cards/SpellTrap/%s.jpg", card.getName())));
        } catch (Exception ignored) {
            try {
                return new Image(new FileInputStream("assets/Cards/CustomCard.jpg"));
            } catch (Exception ignored2) {
                return null;
            }
        }
    }

    public static Card[] getAllMonsters() {
        ArrayList<Card> cards = new ArrayList<>();
        cardsData.forEach((k, v) -> {
            if(getCard(k) != null && getCard(k) instanceof Monster)
                cards.add(getCard(k));
        });
        cards.sort(new cardLexicographicalOrder());
        return cards.toArray(new Card[0]);
    }

    static class cardLexicographicalOrder implements Comparator<Card> {
        public int compare(Card card1, Card card2) {
            return card1.name.compareTo(card2.name);
        }
    }
}
