package YuGiOh.view.game;

import YuGiOh.model.CardAddress;
import YuGiOh.model.Game;
import YuGiOh.model.Player.Player;
import YuGiOh.model.card.Card;
import YuGiOh.model.enums.ZoneType;
import YuGiOh.view.game.component.CardFrame;
import YuGiOh.view.menu.DuelMenuView;
import javafx.geometry.Bounds;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class GameCardFrameManager {
    private final HashMap<CardFrame, CardAddress> occupied = new HashMap<>();
    private CardMoveHandler moveHandler;

    public void setMoveHandler(CardMoveHandler moveHandler) {
        this.moveHandler = moveHandler;
    }

    public synchronized CompletableFuture<Void> refresh() {
        return DuelMenuView.getInstance().getApi().getCardPositions().thenCompose(cardPositions->{
            Map<CardFrame, CardAddress> changes = new HashMap<>();
            AtomicBoolean invalid = new AtomicBoolean(false);
            occupied.forEach((cardFrame, address) -> {
                try {
                    CardAddress newAddress = cardPositions.get(cardFrame.getCard());
                    if (!newAddress.equals(address))
                        changes.put(cardFrame, newAddress);
                } catch (Throwable tt) {
                    tt.printStackTrace();
                    invalid.set(true);
                }
            });
            if (invalid.get()) {
                throw new Error("this must never happen!");
            }

            List<CompletableFuture<Void>> completableFutures = new ArrayList<>();
            changes.forEach(((cardFrame, cardAddress) -> {
                if (moveHandler != null)
                    completableFutures.add(moveHandler.move(cardFrame, cardAddress));
                occupied.put(cardFrame, cardAddress);
            }));
            occupied.forEach(((cardFrame, address) -> {
                if (address.getZone().equals(ZoneType.HAND))
                    completableFutures.add(moveHandler.move(cardFrame, address));
            }));
            return CompletableFuture.allOf(completableFutures.toArray(CompletableFuture[]::new));
        });
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
        public CompletableFuture<Void> move(CardFrame card, CardAddress to);
    }
}
