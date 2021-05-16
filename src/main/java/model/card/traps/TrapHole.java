package model.card.traps;

import model.card.Trap;
import model.enums.Icon;
import model.enums.Status;

public class TrapHole extends Trap {
    protected TrapHole(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }


}
