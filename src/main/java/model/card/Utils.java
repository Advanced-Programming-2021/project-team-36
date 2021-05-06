package model.card;

import model.card.monsterCards.*;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static Card[] getAllCards(){
        Card[] cards = {new AxeRaider(), new BattleOx(), new Fireyarou(), new HornImp(), new SilverFang()};
        return cards;
    }
}
