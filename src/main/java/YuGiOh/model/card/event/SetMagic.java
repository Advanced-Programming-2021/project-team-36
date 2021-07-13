package YuGiOh.model.card.event;

import YuGiOh.model.Player.Player;
import YuGiOh.model.card.Magic;
import lombok.Getter;

public class SetMagic extends Event {
    @Getter
    private final Magic magic;
    @Getter
    private final Player player;

    public SetMagic(Player player, Magic magic){
        this.magic = magic;
        this.player = player;
    }

    @Override
    public int getSpeed() {
        return 0;
    }

    @Override
    public String getActivationQuestion() {
        return String.format("Do you want to set %s?", magic.getName());
    }

    @Override
    public String getDescription(){
        return "set magic";
    }
}
