package model.card;

import model.card.monsterCards.*;

public class Utils {
    public static Card[] getAllCards(){
        return new Card[]{
                new AxeRaider(),
                new BattleOx(),
                new Fireyarou(),
                new HornImp(),
                new SilverFang()
        };
    }
}
