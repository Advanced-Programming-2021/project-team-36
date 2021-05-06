package model.card;

import model.card.monsters.Shayan;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static List<Class<? extends Card>> getAllCards(){
        List<Class<? extends Card>> list = new ArrayList<>();
        list.add(Shayan.class);
        return list;
    }
}
