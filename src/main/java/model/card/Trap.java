package model.card;

import model.enums.Icon;
import model.enums.Status;

public class Trap extends Magic {
    protected Trap(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }
}
