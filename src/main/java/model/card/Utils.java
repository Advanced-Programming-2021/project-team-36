package model.card;

import model.card.monsterCards.*;

public class Utils {
    public static Card[] getAllCards() {
        return new Card[]{
                new AxeRaider(),
                new BattleOx(),
                new Fireyarou(),
                new HornImp(),
                new SilverFang()
        };
    }

    public static String formatClassName(String name) {
        String[] elements = name.trim().replaceAll(",|-", "").split("\\s+");
        String formattedName = "";
        for (int i = 0; i < elements.length; i++)
            formattedName += elements[i].substring(0, 1).toUpperCase() + elements[i].substring(1, elements[i].length());
        return formattedName;
    }
}
