package YuGiOh.view.gui;

import YuGiOh.model.CardAddress;
import YuGiOh.model.Game;
import YuGiOh.model.Player.Player;
import YuGiOh.model.enums.Phase;
import YuGiOh.model.enums.ZoneType;

public abstract class GameMapLocation {
    Game game;

    GameMapLocation(Game game){
        this.game = game;
    }

    public int getPlayerUpDown(Player player){
        return player.equals(game.getFirstPlayer()) ? 0 : 1;
    }
    public int getOwnerUpDown(CardAddress address){
        return getPlayerUpDown(address.getOwner());
    }

    abstract public RatioLocation getLocationByCardAddress(CardAddress address);
    abstract public RatioLocation getDirectPlayerLocation(Player player);
    abstract public double getCardHeightRatio();
    abstract public double getCardWidthRatio();
    abstract public RatioLocation getPhaseLocation(Phase phase);
    abstract public RatioLocation getZonePileOpenRatio(ZoneType zoneType, Player player);
    abstract public RatioLocation getZonePileCloseRatio(ZoneType zoneType, Player player);
}
