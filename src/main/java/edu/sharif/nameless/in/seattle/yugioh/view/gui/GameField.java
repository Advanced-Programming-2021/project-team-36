package edu.sharif.nameless.in.seattle.yugioh.view.gui;

import edu.sharif.nameless.in.seattle.yugioh.controller.GameController;
import edu.sharif.nameless.in.seattle.yugioh.model.Board;
import edu.sharif.nameless.in.seattle.yugioh.model.CardAddress;
import edu.sharif.nameless.in.seattle.yugioh.model.Game;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Card;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Magic;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.ZoneType;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.ListChangeListener;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Pair;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameField extends Pane {
    private final HashMap<CardFrame, CardAddress> occupied = new HashMap<>();
    @Getter
    private final DoubleBinding cardHeightProperty, cardWidthProperty, widthProperty, heightProperty;
    private final Game game;

    // down up pos
    CardLocation[][] monsterLocation = new CardLocation[][]{
            new CardLocation[]{
                    new CardLocation(0.5175, 0.599),
                    new CardLocation(0.65375, 0.599),
                    new CardLocation(0.37625, 0.599),
                    new CardLocation(0.7925, 0.599),
                    new CardLocation(0.245, 0.599)
            },
            new CardLocation[]{
                    new CardLocation(0.51875, 0.396),
                    new CardLocation(0.37875, 0.396),
                    new CardLocation(0.655, 0.396),
                    new CardLocation(0.24625, 0.396),
                    new CardLocation(0.795, 0.396)
            }
    };
    CardLocation[][] magicLocation = new CardLocation[][]{
            new CardLocation[]{
                    new CardLocation(0.5175, 0.771),
                    new CardLocation(0.6525, 0.771),
                    new CardLocation(0.3775, 0.771),
                    new CardLocation(0.7925, 0.771),
                    new CardLocation(0.24, 0.771)
            },
            new CardLocation[]{
                    new CardLocation(0.515, 0.232),
                    new CardLocation(0.37625, 0.232),
                    new CardLocation(0.65375, 0.232),
                    new CardLocation(0.2425, 0.232),
                    new CardLocation(0.7925, 0.232)
            }
    };
    CardLocation[] fieldLocation = new CardLocation[]{
            new CardLocation(0.0975, 0.592),
            new CardLocation(0.94125, 0.4)
    };

    // todo what happens if there are too many cards in hand?
    CardLocation[][] handLocation = new CardLocation[][]{
            new CardLocation[]{
                    new CardLocation(0.1, 0.94),
                    new CardLocation(0.24, 0.94),
                    new CardLocation(0.38, 0.94),
                    new CardLocation(0.52, 0.94),
                    new CardLocation(0.68, 0.94)
            },
            new CardLocation[]{
                    new CardLocation(0.1, 0.06),
                    new CardLocation(0.24, 0.06),
                    new CardLocation(0.38, 0.06),
                    new CardLocation(0.52, 0.06),
                    new CardLocation(0.68, 0.06)
            }
    };
    CardLocation[] graveYardLocation = new CardLocation[]{
            new CardLocation(0.9285714285714286,0.633),
            new CardLocation(0.3007142857142857, 0.363)
    };

    private void refreshHand(Board board){
        int[] counter = new int[1];
        counter[0] = 1;
        board.getCardsOnHand().forEach(card->{
            moveOrCreateCardByCardAddress(new CardAddress(ZoneType.HAND, counter[0], board.getOwner()), card);
            counter[0]+=1;
        });
    }
    private void refreshGraveYard(Board board){
        int[] counter = new int[1];
        counter[0] = 1;
        board.getGraveYard().forEach(card->{
            moveOrCreateCardByCardAddress(new CardAddress(ZoneType.GRAVEYARD, counter[0], board.getOwner()), card);
            counter[0]+=1;
        });
    }
    private void refreshMonsterZone(Board board){
        board.getMonsterCardZone().forEach((id, card)->{
            moveOrCreateCardByCardAddress(new CardAddress(ZoneType.MONSTER, id, board.getOwner()), card);
        });
    }
    private void refreshMagicZone(Board board){
        board.getMagicCardZone().forEach((id, card)->{
            moveOrCreateCardByCardAddress(new CardAddress(ZoneType.MAGIC, id, board.getOwner()), card);
        });
    }
    private void refreshFieldZone(Board board){
        // why is id required for field zone?
        if(board.getFieldZoneCard() != null)
            moveOrCreateCardByCardAddress(new CardAddress(ZoneType.FIELD, 0, board.getOwner()), board.getFieldZoneCard());
    }

    public void addBoardListeners(Board board){
        // todo these are just default implementations!
        board.getCardsOnHand().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                refreshHand(board);
            }
        });
        board.getGraveYard().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                refreshGraveYard(board);
            }
        });
        board.getMonsterCardZone().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                refreshMonsterZone(board);
            }
        });
        board.getMagicCardZone().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                refreshMagicZone(board);
            }
        });
        board.getFieldZoneCardObservableList().addListener(new ListChangeListener<Magic>() {
            @Override
            public void onChanged(Change<? extends Magic> c) {
                refreshFieldZone(board);
            }
        });
    }

    public GameField(Game game, DoubleBinding widthProperty, DoubleBinding heightProperty){
        this.widthProperty = widthProperty;
        this.heightProperty = heightProperty;
        this.game = game;

        ImageView background = new ImageView(new Image(Utils.getAsset("Field/fie_normal.bmp").toURI().toString()));

        minWidthProperty().bind(widthProperty);
        minHeightProperty().bind(heightProperty);
        background.fitWidthProperty().bind(widthProperty);
        background.fitHeightProperty().bind(heightProperty);
        getChildren().add(background);

        cardWidthProperty = widthProperty.multiply(0.1);
        cardHeightProperty = heightProperty.multiply(0.12);

        for(Board board : new Board[]{ game.getFirstPlayer().getBoard(), game.getSecondPlayer().getBoard()}){
            addBoardListeners(board);
            refreshFieldZone(board);
            refreshMonsterZone(board);
            refreshHand(board);
            refreshGraveYard(board);
            refreshMagicZone(board);
        }
    }

    private void moveOrCreateCardByCardAddress(CardAddress address, Card card) {
        int up = address.getOwner().equals(game.getFirstPlayer()) ? 0 : 1;
        int id = address.getId()-1;

        // todo this is dummy remove this
        CardLocation cardLocation;

        if(address.isInHand()){
            cardLocation = handLocation[up][id];
        }
        else if(address.isInFieldZone()){
            cardLocation = fieldLocation[up];
        }
        else if(address.isInGraveYard()){
            cardLocation = graveYardLocation[up];
        }
        else if(address.isInMagicZone()){
            cardLocation = magicLocation[up][id];
        }
        else{
            assert address.isInMonsterZone();
            cardLocation = monsterLocation[up][id];
        }
        final CardFrame[] cardFrameTmp = new CardFrame[1];
        occupied.forEach((cardFrame, cardAddress)->{
            if(cardFrame.getCard().equals(card))
                cardFrameTmp[0] = cardFrame;
        });
        final CardFrame cardFrame = cardFrameTmp[0] == null ? new CardFrame(card, cardWidthProperty, cardHeightProperty) : cardFrameTmp[0];
        moveCardByCoordinate(cardFrame, widthProperty.multiply(cardLocation.xRatio), heightProperty.multiply(cardLocation.yRatio));
        occupied.put(cardFrame, address);
    }

    public double getCenterPositionX(int i, int j, boolean isUp){
        double x = cardWidthProperty.get() * (i+1);
        if(!isUp)
            x = widthProperty.get() - x;
        return x;
    }
    public double getCenterPositionY(int i, int j, boolean isUp){
        double y = cardHeightProperty.get() * (j+1.06);
        if(!isUp)
            y = heightProperty.get() - y;
        return y;
    }

    public void moveCardByCoordinate(CardFrame cardFrame, DoubleBinding x, DoubleBinding y){
        cardFrame.xProperty().bind(x.add(cardWidthProperty.divide(2).negate()));
        cardFrame.yProperty().bind(y.add(cardHeightProperty.divide(2).negate()));
        if(!getChildren().contains(cardFrame))
            getChildren().add(cardFrame);
    }

    static class CardLocation{
        double xRatio, yRatio;
        CardLocation(double xRatio, double yRatio){
            this.xRatio = xRatio;
            this.yRatio = yRatio;
        }
    }
}