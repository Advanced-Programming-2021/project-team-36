package YuGiOh.view.gui;

import YuGiOh.controller.GameController;
import YuGiOh.controller.LogicException;
import YuGiOh.controller.MainGameThread;
import YuGiOh.controller.QueryGameThread;
import YuGiOh.controller.events.RoundOverExceptionEvent;
import YuGiOh.controller.menu.DuelMenuController;
import YuGiOh.model.Board;
import YuGiOh.model.CardAddress;
import YuGiOh.model.Game;
import YuGiOh.model.Player.Player;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Magic;
import YuGiOh.model.card.action.DirectAttackEvent;
import YuGiOh.model.card.action.MagicActivation;
import YuGiOh.model.card.action.MonsterAttackEvent;
import YuGiOh.model.enums.ZoneType;
import YuGiOh.view.cardSelector.ResistToChooseCard;
import YuGiOh.view.gui.event.DropCardEvent;
import YuGiOh.view.gui.event.DuelOverEvent;
import YuGiOh.view.gui.event.RoundOverEvent;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.ListChangeListener;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GameField extends Pane {
    private final HashMap<CardFrame, CardAddress> occupied = new HashMap<>();
    @Getter
    private final DoubleBinding cardHeightProperty, cardWidthProperty, widthProperty, heightProperty;
    private final Game game;

    @Setter
    private DuelInfoBox infoBox;

    // todo how to force infoBox to be present?

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

    CardLocation[] graveYardLocation = new CardLocation[]{
            new CardLocation(0.9285714285714286,0.633),
            new CardLocation(0.1207142857142857, 0.363)
    };

    // todo what happens if there are too many cards in hand?
    public CardLocation getHandLocation(int cardId, int totalCards, int up){
        double y = up == 1 ? 0.06 : 0.94;
        double x = 0.1 + 0.8 * cardId / totalCards;
        return new CardLocation(x, y);
    }
    public CardLocation getGraveYardLocation(int id, int totalCards, int up){
        double x = graveYardLocation[up].xRatio;
        double y = graveYardLocation[up].yRatio;
        return new CardLocation(x + (1/widthProperty.get()) * 40 * id / totalCards, y);
    }

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
        board.getCardsOnHand().addListener((InvalidationListener) (observable)-> refreshHand(board));
        board.getGraveYard().addListener((InvalidationListener) (observable)-> refreshGraveYard(board));
        board.getMonsterCardZone().addListener((InvalidationListener) (observable)-> refreshMonsterZone(board));
        board.getMagicCardZone().addListener((InvalidationListener) (observable)-> refreshMagicZone(board));
        board.getFieldZoneCardObservableList().addListener((ListChangeListener<Magic>) (c)-> refreshFieldZone(board));
    }

    public GameField(Game game, DoubleBinding widthProperty, DoubleBinding heightProperty){
        this.widthProperty = widthProperty;
        this.heightProperty = heightProperty;
        this.game = game;

        new GuiReporter(this);

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
        setEventListeners();
    }

    private void setEventListeners(){
        GuiReporter.getInstance().addEventHandler(DropCardEvent.MY_TYPE, e->{
            occupied.forEach((cardFrame, cardAddress)->{
                double difX = e.getBounds().getCenterX() - cardFrame.getBoundsInParent().getCenterX();
                double difY = e.getBounds().getCenterY() - cardFrame.getBoundsInParent().getCenterY();
                if(Math.abs(difX) + Math.abs(difY) <= (cardWidthProperty.get() + cardHeightProperty.get()) * 0.3){
                    if(!cardFrame.equals(e.getCardFrame()))
                        runAndAlert(()-> DuelMenuController.getInstance().attack(e.getCardFrame().getCard(), cardAddress), ()->{});
                }
            });
        });
        GuiReporter.getInstance().addEventHandler(RoundOverEvent.MY_TYPE, e->{
            System.out.println("round was over");
            // todo this is just a sample
        });
        GuiReporter.getInstance().addEventHandler(DuelOverEvent.MY_TYPE, e->{
            System.out.println("duel was over");
            // todo this is just a sample
        });
        GuiReporter.getInstance().addGameEventHandler((GuiReporter.GameEventHandler<MonsterAttackEvent>) (event)->{
            moveOrCreateCardByCardAddress(getCardAddressByCard(event.getDefender()), event.getAttacker());
            moveOrCreateCardByCardAddress(game.getCardAddress(event.getAttacker()), event.getAttacker());
        });
        GuiReporter.getInstance().addGameEventHandler((GuiReporter.GameEventHandler<DirectAttackEvent>) (event)->{
            int up = getPlayerUpDown(event.getPlayer());
            CardLocation opponentPlayerLocation = new CardLocation(0.5, 1-up+0.05);
            CardFrame cardFrame = getCardFrameByCard(event.getAttacker());
            assert cardFrame != null;
            tellCardFrameToMoveByCardLocation(cardFrame, opponentPlayerLocation, Duration.millis(400));
            tellCardFrameToMoveByCardLocation(cardFrame, getCardLocationByAddress(game.getCardAddress(event.getAttacker())), Duration.millis(400));
        });
        GuiReporter.getInstance().addGameEventHandler((GuiReporter.GameEventHandler<MagicActivation>) (event)->{
            Platform.runLater(()-> {
                Magic magic = event.getMagic();
                Text text = new Text(magic.getName() + " activated!");
                text.setFont(Font.font(50));
                text.setFill(Color.RED);
                text.layoutXProperty().bind(widthProperty().multiply(0.3));
                text.layoutYProperty().bind(heightProperty().multiply(0.4));
                getChildren().add(text);
                new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {
                    }
                    Platform.runLater(() -> getChildren().remove(text));
                }).start();
            });
        });
    }

    private int getPlayerUpDown(Player player){
        return player.equals(game.getFirstPlayer()) ? 0 : 1;
    }
    private int getOwnerUpDown(CardAddress address){
        return getPlayerUpDown(address.getOwner());
    }
    private CardLocation getCardLocationByAddress(CardAddress address){
        int up = getOwnerUpDown(address);
        int id = address.getId()-1;
        if(address.isInHand())
            return getHandLocation(id, address.getOwner().getBoard().getCardsOnHand().size(), up);
        else if(address.isInFieldZone())
            return fieldLocation[up];
        else if(address.isInGraveYard())
            return getGraveYardLocation(id, address.getOwner().getBoard().getGraveYard().size(), up);
        else if(address.isInMagicZone())
            return magicLocation[up][id];
        else if(address.isInMonsterZone())
            return monsterLocation[up][id];
        throw new Error("this will never happen");
    }
    private Duration getAnimationDuration(CardAddress address, Card card){
        if(address.isInHand()) {
            return Duration.millis(100);
        }  else if(address.isInFieldZone()) {
            return Duration.millis(300);
        } else if(address.isInGraveYard()) {
            if (game.getCardZoneType(card) == ZoneType.GRAVEYARD)
                return Duration.millis(3);
            return Duration.millis(300);
        } else if(address.isInMagicZone()){
            return Duration.millis(300);
        } if(address.isInMonsterZone()) {
            return Duration.millis(300);
        }
        throw new Error("this will never happen");
    }
    private CardAddress getCardAddressByCard(Card card){
        for (Map.Entry<CardFrame,CardAddress> entry : occupied.entrySet()){
            if(entry.getKey().getCard().equals(card))
                return entry.getValue();
        }
        return null;
    }
    private CardFrame getCardFrameByCard(Card card){
        for (Map.Entry<CardFrame,CardAddress> entry : occupied.entrySet()){
            if(entry.getKey().getCard().equals(card))
                return entry.getKey();
        }
        return null;
    }

    private void tellCardFrameToMoveByCardLocation(CardFrame cardFrame, CardLocation cardLocation, Duration animationDuration){
        cardFrame.animateCardMoving(
            widthProperty.multiply(cardLocation.xRatio).add(cardWidthProperty.divide(2).negate()),
            heightProperty.multiply(cardLocation.yRatio).add(cardHeightProperty.divide(2).negate()),
            animationDuration
        );
    }
    private void moveOrCreateCardByCardAddress(CardAddress address, Card card) {
        CardFrame cardFrame = getCardFrameByCard(card);
        CardLocation cardLocation = getCardLocationByAddress(address);
        Duration animationDuration = getAnimationDuration(address, card);
        // if it is new born we don't animate
        boolean newBorn = cardFrame == null;
        if (newBorn){
            cardFrame = new CardFrame(this, card, cardWidthProperty, cardHeightProperty);
            cardFrame.moveByBindingCoordinates(
                    widthProperty.multiply(cardLocation.xRatio).add(cardWidthProperty.divide(2).negate()),
                    heightProperty.multiply(cardLocation.yRatio).add(cardHeightProperty.divide(2).negate())
            );
            final CardFrame finalCardFrame = cardFrame;
            Platform.runLater(()-> getChildren().add(finalCardFrame));
        } else {
            tellCardFrameToMoveByCardLocation(cardFrame, cardLocation, animationDuration);
        }
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

    public void runAndAlert(GameRunnable runnable, Runnable onFail){
        QueryGameThread.getInstance().addRunnable(()->{
            try {
                runnable.run();
            } catch (LogicException | ResistToChooseCard e){
                onFail.run();
                Platform.runLater(()->new AlertBox().display(this, e.getMessage()));
            } catch (RoundOverExceptionEvent e){
                onFail.run();
                GuiReporter.getInstance().report(new RoundOverEvent(e));
            }
        });
    }

    static class CardLocation{
        double xRatio, yRatio;
        CardLocation(double xRatio, double yRatio){
            this.xRatio = xRatio;
            this.yRatio = yRatio;
        }
    }
    public interface GameRunnable{
        void run() throws LogicException, RoundOverExceptionEvent, ResistToChooseCard;
    }
}