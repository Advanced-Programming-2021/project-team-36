package YuGiOh.view.gui;

import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.Trap;
import javafx.scene.image.Image;

import java.io.File;

public class Utils {
    public static File getAsset(String relativeAddress){
        File file = new File("assets/" + relativeAddress);
        // todo this is for test until every asset name is ok
        if(!file.exists()){
            System.out.println("not found " + file.toURI());
            throw new Error();
        }
        return file;
    }
    public static Image getImage(String address) {
        return new Image(getAsset(address).toURI().toString());
    }
    public static Image getMonsterImage(String name){
        return getImage("Cards/Monsters/" + name + ".jpg");
    }
    public static Image getTrapImage(String name){
        return getImage("Cards/SpellTrap/" + name + ".jpg");
    }
    public static Image getSpellImage(String name){
        return getImage("Cards/SpellTrap/" + name + ".jpg");
    }
    public static Image getTexture(String name){
        return getImage("Texture/" + name);
    }
    public static Image getCardImage(Card card){
        if(card instanceof Monster) {
            System.out.println("trying to load image of " + card.getName());
            return getMonsterImage(card.getName());
        }
        if(card instanceof Trap)
            return getTrapImage(card.getName());
        if(card instanceof Spell)
            return getSpellImage(card.getName());
        throw new Error("there is a mistake in loading image");
    }
}
