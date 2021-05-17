package model.card;

import controller.GameController;
import model.card.action.Action;
import model.card.action.Effect;
import model.enums.Icon;
import model.enums.Status;

import java.util.Stack;

abstract public class Trap extends Magic {
    public Trap(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    public int getSpeed(){
        if(icon.equals(Icon.COUNTER))
            return 3;
        return 2;
    }

    @Override
    public String toString() {
        return String.format("%s (Trap - %s) : %s", getName(), getIcon(), getDescription());
    }
}
