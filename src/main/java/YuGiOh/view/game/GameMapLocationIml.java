package YuGiOh.view.game;

import YuGiOh.model.CardAddress;
import YuGiOh.model.Game;
import YuGiOh.model.Player.Player;
import YuGiOh.model.enums.Phase;
import YuGiOh.model.enums.ZoneType;

public class GameMapLocationIml extends GameMapLocation {
    public GameMapLocationIml(Player downPlayer) {
        super(downPlayer);
    }

    private RatioLocation getHandLocation(int cardId, int totalCards, int up){
        double y = up == 1 ? 0.06 : 0.94;
        double x = 0.1 + 0.8 * cardId / totalCards;
        return new RatioLocation(x, y);
    }

    @Override
    public RatioLocation getLocationByCardAddress(CardAddress address) {
        int up = getOwnerUpDown(address);
        int id = address.getId()-1;
        if(address.isInHand())
            return getHandLocation(id, address.getOwner().getBoard().getCardsOnHand().size(), up);
        else if(address.isInFieldZone())
            return fieldLocation[up];
        else if(address.isInGraveYard())
            return graveYardLocation[up];
        else if(address.isInMagicZone())
            return magicLocation[up][id];
        else if(address.isInMonsterZone())
            return monsterLocation[up][id];
        else if(address.isInDeck())
            return deckLocation[up];
        throw new Error("this will never happen");
    }

    @Override
    public RatioLocation getDirectPlayerLocation(Player player) {
        int up = getPlayerUpDown(player);
        return new RatioLocation(0.5, 1-up+0.05);
    }

    @Override
    public double getCardHeightRatio() {
        return getCardWidthRatio() * 1.458432304;
    }
    @Override
    public double getCardWidthRatio() {
        return 0.1;
    }

    @Override
    public RatioLocation getPhaseLocation(Phase phase) {
        return new RatioLocation(0.001, 0.13 + 0.13 * phase.ordinal());
    }

    @Override
    public RatioLocation getZonePileOpenRatio(ZoneType zoneType, Player player) {
        RatioLocation ret = getLocationByCardAddress(new CardAddress(zoneType, 1, player));
        return new RatioLocation(ret.xRatio, ret.yRatio + (getPlayerUpDown(player) == 1 ? 0.4 : -0.4));
    }

    @Override
    public RatioLocation getZonePileCloseRatio(ZoneType zoneType, Player player) {
        RatioLocation ret = getLocationByCardAddress(new CardAddress(zoneType, 1, player));
        return ret;
    }

    private final RatioLocation[][] monsterLocation = new RatioLocation[][]{
            new RatioLocation[]{
                    new RatioLocation(0.5175, 0.599),
                    new RatioLocation(0.65375, 0.599),
                    new RatioLocation(0.37625, 0.599),
                    new RatioLocation(0.7925, 0.599),
                    new RatioLocation(0.245, 0.599)
            },
            new RatioLocation[]{
                    new RatioLocation(0.51875, 0.396),
                    new RatioLocation(0.37875, 0.396),
                    new RatioLocation(0.655, 0.396),
                    new RatioLocation(0.24625, 0.396),
                    new RatioLocation(0.795, 0.396)
            }
    };
    private final RatioLocation[][] magicLocation = new RatioLocation[][]{
            new RatioLocation[]{
                    new RatioLocation(0.5175, 0.771),
                    new RatioLocation(0.6525, 0.771),
                    new RatioLocation(0.3775, 0.771),
                    new RatioLocation(0.7925, 0.771),
                    new RatioLocation(0.24, 0.771)
            },
            new RatioLocation[]{
                    new RatioLocation(0.515, 0.232),
                    new RatioLocation(0.37625, 0.232),
                    new RatioLocation(0.65375, 0.232),
                    new RatioLocation(0.2425, 0.232),
                    new RatioLocation(0.7925, 0.232)
            }
    };
    private final RatioLocation[] fieldLocation = new RatioLocation[]{
            new RatioLocation(0.0975, 0.592),
            new RatioLocation(0.94125, 0.4)
    };
    private final RatioLocation[] graveYardLocation = new RatioLocation[]{
            new RatioLocation(0.9285714285714286,0.633),
            new RatioLocation(0.1207142857142857, 0.363)
    };
    private final RatioLocation[] deckLocation = new RatioLocation[] {
            new RatioLocation(0.9285714285714286,0.633 + 0.17),
            new RatioLocation(0.1207142857142857, 0.363 - 0.17)
    };
}
