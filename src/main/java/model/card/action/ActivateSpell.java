package model.card.action;

import lombok.Getter;
import model.card.Spell;

public class ActivateSpell extends Event {
    @Getter
    Spell spell;

    public ActivateSpell(Spell spell) {
        this.spell = spell;
    }
}
