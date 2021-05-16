package model.card;

import model.enums.Icon;
import model.enums.Status;

public abstract class Spell extends Magic {
    protected Spell(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }
}
