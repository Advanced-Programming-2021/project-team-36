package YuGiOh.archive.view.gui;

import YuGiOh.model.Board;
import YuGiOh.model.CardAddress;
import YuGiOh.model.Game;
import YuGiOh.model.Player.Player;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Magic;
import YuGiOh.model.enums.ZoneType;
import YuGiOh.archive.view.gui.component.CardFrame;
import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.geometry.Bounds;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameCardFrameManager {
    private final HashMap<CardFrame, CardAddress> occupied = new HashMap<>();
    private final Game game;
    private CardMoveHandler moveHandler;

    public GameCardFrameManager(Game game) {
        this.game = game;
        setEventListeners();
    }

    public void setMoveHandler(CardMoveHandler moveHandler) {
        this.moveHandler = moveHandler;
    }

    private void setEventListeners() {
        for(Board board : Arrays.asList(game.getFirstPlayer().getBoard(), game.getSecondPlayer().getBoard())) {
            board.getCardsOnHand().addListener((InvalidationListener) (observable) -> refresh());
            board.getGraveYard().addListener((InvalidationListener) (observable) -> refresh());
            board.getMonsterCardZone().addListener((InvalidationListener) (observable) -> refresh());
            board.getMagicCardZone().addListener((InvalidationListener) (observable) -> refresh());
            board.getFieldZoneCardObservableList().addListener((ListChangeListener<Magic>) (c) -> refresh());
            board.getMainDeck().getCards().addListener((InvalidationListener) (observable) -> refresh());
        }
    }

    public synchronized void refresh() {
        if (game.getAllCards().size() != occupied.size()) {
            return;
        }
        Map<CardFrame, CardAddress> changes = new HashMap<>();
        AtomicBoolean invalid = new AtomicBoolean(false);
        occupied.forEach((cardFrame, address) -> {
            try {
                CardAddress newAddress = game.getCardAddress(cardFrame.getCard());
                if (!newAddress.equals(address))
                    changes.put(cardFrame, newAddress);
            } catch (Throwable tt) {
                tt.printStackTrace();
                invalid.set(true);
            }
        });
        if (invalid.get())
            return;

        changes.forEach(((cardFrame, cardAddress) -> {
            if (moveHandler != null)
                moveHandler.move(cardFrame, cardAddress);
            occupied.put(cardFrame, cardAddress);
        }));
        occupied.forEach(((cardFrame, address) -> {
            if (address.getZone().equals(ZoneType.HAND))
                moveHandler.move(cardFrame, address);
        }));
    }

    public CardAddress getCardAddressByCard(Card card){
        for (Map.Entry<CardFrame,CardAddress> entry : occupied.entrySet()){
            if(entry.getKey().getCard().equals(card))
                return entry.getValue();
        }
        return null;
    }
    public CardFrame getCardFrameByCard(Card card){
        for (Map.Entry<CardFrame,CardAddress> entry : occupied.entrySet()){
            if(entry.getKey().getCard().equals(card))
                return entry.getKey();
        }
        return null;
    }
    public List<CardFrame> getIntersectingCards(Bounds bounds) {
        ArrayList<CardFrame> ret = new ArrayList<>();
        occupied.forEach((cardFrame, cardAddress)->{
            double difX = bounds.getCenterX() - cardFrame.getBoundsInParent().getCenterX();
            double difY = bounds.getCenterY() - cardFrame.getBoundsInParent().getCenterY();
            if(Math.abs(difX) + Math.abs(difY) <= (cardFrame.getWidth() + cardFrame.getHeight()) * 0.3)
                ret.add(cardFrame);
        });
        return ret;
    }
    public List<CardFrame> getAllCardFrames() {
        List<CardFrame> ret = new ArrayList<>();
        occupied.forEach(((cardFrame, cardAddress) -> ret.add(cardFrame)));
        return ret;
    }
    public List<CardFrame> getCardsByZoneAndPlayer(ZoneType zoneType, Player player) {
        List<CardFrame> ret = new ArrayList<>();
        occupied.forEach(((cardFrame, cardAddress) -> {
            if(cardAddress.getOwner().equals(player) && cardAddress.getZone().equals(zoneType))
                ret.add(cardFrame);
        }));
        return ret;
    }
    public void addNewCard(CardFrame cardFrame, CardAddress cardAddress) {
        if(occupied.containsKey(cardFrame))
            throw new Error("this is just for creating");
        occupied.put(cardFrame, cardAddress);
    }

    public interface CardMoveHandler {
        public void move(CardFrame card, CardAddress to);
    }
}
