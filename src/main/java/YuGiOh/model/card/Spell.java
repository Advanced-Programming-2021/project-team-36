package YuGiOh.model.card;

import YuGiOh.model.enums.Icon;
import YuGiOh.model.enums.Status;

abstract public class Spell extends Magic {
    public Spell(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    public int getSpeed(){
        if (icon.equals(Icon.QUICKPLAY))
            return 2;
        return 1;
    }

    @Override
    public String toString() {
        return String.format("%s (Spell - %s) : %s", getName(), getIcon(), getDescription());
    }
}
