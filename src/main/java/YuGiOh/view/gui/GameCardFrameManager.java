package YuGiOh.view.gui;

import YuGiOh.controller.menu.DuelMenuController;
import YuGiOh.model.CardAddress;
import YuGiOh.model.card.Card;
import javafx.geometry.Bounds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameCardFrameManager {
    private final HashMap<CardFrame, CardAddress> occupied = new HashMap<>();

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
    public void put(CardFrame cardFrame, CardAddress cardAddress) {
        occupied.put(cardFrame, cardAddress);
    }
}
