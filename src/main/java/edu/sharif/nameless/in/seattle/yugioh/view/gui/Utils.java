package edu.sharif.nameless.in.seattle.yugioh.view.gui;

import edu.sharif.nameless.in.seattle.yugioh.model.card.Card;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Monster;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Spell;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Trap;
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
    public static Image getMonsterImage(String name){
        return new Image(getAsset("Cards/Monsters/" + name + ".jpg").toURI().toString());
    }
    public static Image getTrapImage(String name){
        return new Image(getAsset("Cards/SpellTrap/" + name + ".jpg").toURI().toString());
    }
    public static Image getSpellImage(String name){
        return new Image(getAsset("Cards/SpellTrap/" + name + ".jpg").toURI().toString());
    }
    public static Image getTexture(String name){
        return new Image(getAsset("Texture/" + name).toURI().toString());
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
