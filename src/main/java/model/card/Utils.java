package model.card;

import model.card.monsterCards.*;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static List<Class<? extends Card>> getAllCards(){
        List<Class<? extends Card>> list = new ArrayList<>();
        list.add(AxeRaider.class);
        list.add(BattleOx.class);
        list.add(Fireyarou.class);
        list.add(HornImp.class);
        list.add(SilverFang.class);
        return list;
    }
}
