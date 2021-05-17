package model.card;

import controller.GameController;
import model.card.action.Action;
import model.card.action.Effect;
import model.enums.Icon;
import model.enums.Status;

import java.util.Stack;

abstract public class Spell extends Magic {
    protected Spell(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }
}
