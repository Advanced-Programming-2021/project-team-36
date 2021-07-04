package YuGiOh.view.gui.component;

import YuGiOh.controller.GameController;
import YuGiOh.controller.LogicException;
import YuGiOh.controller.MainGameThread;
import YuGiOh.controller.events.RoundOverExceptionEvent;
import YuGiOh.controller.menu.DuelMenuController;
import YuGiOh.controller.player.AIPlayerController;
import YuGiOh.model.Board;
import YuGiOh.model.CardAddress;
import YuGiOh.model.Game;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Magic;
import YuGiOh.model.card.action.DirectAttackEvent;
import YuGiOh.model.card.action.MagicActivation;
import YuGiOh.model.card.action.MonsterAttackEvent;
import YuGiOh.model.enums.Phase;
import YuGiOh.model.enums.ZoneType;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.view.cardSelector.ResistToChooseCard;
import YuGiOh.view.gui.*;
import YuGiOh.view.gui.component.AlertBox;
import YuGiOh.view.gui.component.CardFrame;
import YuGiOh.view.gui.component.PhaseLamps;
import YuGiOh.view.gui.component.PileOfCardManager;
import YuGiOh.view.gui.event.DropCardEvent;
import YuGiOh.view.gui.event.DuelOverEvent;
import YuGiOh.view.gui.event.RoundOverEvent;
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

import java.util.*;
import java.util.stream.Collectors;

public class GameField extends Pane {
    @Getter
    private final DoubleBinding cardHeightProperty, cardWidthProperty, widthProperty, heightProperty;
    private final Game game;
    private final ImageView background;
    private final GameMapLocation gameMapLocation;
    private final GameCardFrameManager cardFrameManager;
    private final GameCardMovementManager movementManager;
    private final PileOfCardManager[] deckPile, graveYardPile;

    private void refreshHand(Board board){
        for (int i = 0; i < board.getCardsOnHand().size(); i++) {
            Card card = board.getCardsOnHand().get(i);
            moveCardByAddress(new CardAddress(ZoneType.HAND, i+1, board.getOwner()), card);
        }
    }
    private void refreshGraveYard(Board board){
        int up = gameMapLocation.getPlayerUpDown(board.getOwner());
        graveYardPile[up].close();
        for (int i = 0; i < board.getGraveYard().size(); i++) {
            Card card = board.getGraveYard().get(i);
            moveCardByAddress(new CardAddress(ZoneType.GRAVEYARD, i+1, board.getOwner()), card);
        }
        graveYardPile[up].setCardFrames(board.getGraveYard().stream().map(cardFrameManager::getCardFrameByCard).collect(Collectors.toList()));
    }
    private void refreshMonsterZone(Board board){
        board.getMonsterCardZone().forEach((id, card)->{
            moveCardByAddress(new CardAddress(ZoneType.MONSTER, id, board.getOwner()), card);
        });
    }
    private void refreshMagicZone(Board board){
        board.getMagicCardZone().forEach((id, card)->{
            moveCardByAddress(new CardAddress(ZoneType.MAGIC, id, board.getOwner()), card);
        });
    }
    private void refreshFieldZone(Board board){
        // why is id required for field zone?
        if(board.getFieldZoneCard() != null) {
            moveCardByAddress(new CardAddress(ZoneType.FIELD, 0, board.getOwner()), board.getFieldZoneCard());
        }
        Magic fieldSpell = null;
        if(game.getFirstPlayer().getBoard().getFieldZoneCard() != null)
            fieldSpell = game.getFirstPlayer().getBoard().getFieldZoneCard();
        if(game.getSecondPlayer().getBoard().getFieldZoneCard() != null)
            fieldSpell = game.getSecondPlayer().getBoard().getFieldZoneCard();
        String address = "Field/" + (fieldSpell == null ? "Normal" : fieldSpell.getName()) + ".bmp";
        background.setImage(new Image(Utils.getAsset(address).toURI().toString()));
    }
    private void refreshDeckZone(Board board) {
        int up = gameMapLocation.getPlayerUpDown(board.getOwner());
        deckPile[up].close();
        for (int i = 0; i < board.getMainDeck().getCards().size(); i++) {
            Card card = board.getMainDeck().getCards().get(i);
            moveCardByAddress(new CardAddress(ZoneType.DECK, i+1, board.getOwner()), card);
        }
        deckPile[up].setCardFrames(board.getMainDeck().getCards().stream().map(cardFrameManager::getCardFrameByCard).collect(Collectors.toList()));
    }

    public void addBoardListeners(Board board){
        board.getCardsOnHand().addListener((InvalidationListener) (observable)-> refreshHand(board));
        board.getGraveYard().addListener((InvalidationListener) (observable)-> refreshGraveYard(board));
        board.getMonsterCardZone().addListener((InvalidationListener) (observable)-> refreshMonsterZone(board));
        board.getMagicCardZone().addListener((InvalidationListener) (observable)-> refreshMagicZone(board));
        board.getFieldZoneCardObservableList().addListener((ListChangeListener<Magic>) (c)-> refreshFieldZone(board));
        board.getMainDeck().getCards().addListener((InvalidationListener) (observable) -> refreshDeckZone(board));
    }

    public GameField(Game game, DoubleBinding widthProperty, DoubleBinding heightProperty, GameMapLocation gameMapLocation){
        this.widthProperty = widthProperty;
        this.heightProperty = heightProperty;
        this.cardWidthProperty = widthProperty.multiply(gameMapLocation.getCardWidthRatio());
        this.cardHeightProperty = heightProperty.multiply(gameMapLocation.getCardHeightRatio());

        this.game = game;
        this.gameMapLocation = gameMapLocation;
        this.cardFrameManager = new GameCardFrameManager();
        this.movementManager = new GameCardMovementManager(cardFrameManager, widthProperty, heightProperty, cardWidthProperty, cardHeightProperty);
        this.deckPile = createPiles(ZoneType.DECK);
        this.graveYardPile = createPiles(ZoneType.GRAVEYARD);

        new GuiReporter(this);

        background = new ImageView(new Image(Utils.getAsset("Field/Normal.bmp").toURI().toString()));

        minWidthProperty().bind(widthProperty);
        minHeightProperty().bind(heightProperty);
        background.fitWidthProperty().bind(widthProperty);
        background.fitHeightProperty().bind(heightProperty);
        getChildren().add(background);
        getChildren().add(new PhaseLamps(
                widthProperty,
                heightProperty,
                Map.of(
                        Phase.DRAW_PHASE, gameMapLocation.getPhaseLocation(Phase.DRAW_PHASE),
                        Phase.MAIN_PHASE1, gameMapLocation.getPhaseLocation(Phase.MAIN_PHASE1),
                        Phase.BATTLE_PHASE, gameMapLocation.getPhaseLocation(Phase.BATTLE_PHASE),
                        Phase.MAIN_PHASE2, gameMapLocation.getPhaseLocation(Phase.MAIN_PHASE2)
                )
        ));
        getChildren().addAll(deckPile);
        getChildren().addAll(graveYardPile);

        for(Board board : new Board[]{ game.getFirstPlayer().getBoard(), game.getSecondPlayer().getBoard()}){
            createCards(board);
            addBoardListeners(board);
        }
        background.toBack();
        setEventListeners();
    }

    // todo this is tof
    private PileOfCardManager[] createPiles(ZoneType zoneType) {
        RatioLocation[] ratioLocation = new RatioLocation[] {
                gameMapLocation.getLocationByCardAddress(new CardAddress(zoneType, 1, game.getFirstPlayer())),
                gameMapLocation.getLocationByCardAddress(new CardAddress(zoneType, 1, game.getSecondPlayer())),
        };
        return new PileOfCardManager[] {
            new PileOfCardManager(
                    Direction.LEFT,
                    widthProperty.multiply(ratioLocation[0].xRatio),
                    heightProperty.multiply(ratioLocation[0].yRatio),
                    widthProperty.multiply(ratioLocation[0].xRatio),
                    heightProperty.multiply(ratioLocation[0].yRatio),
                    widthProperty.multiply(ratioLocation[0].xRatio),
                    heightProperty.multiply(ratioLocation[0].yRatio - 0.4),
                    cardWidthProperty,
                    cardHeightProperty
            ),
            new PileOfCardManager(
                    Direction.RIGHT,
                    widthProperty.multiply(ratioLocation[1].xRatio),
                    heightProperty.multiply(ratioLocation[1].yRatio),
                    widthProperty.multiply(ratioLocation[1].xRatio),
                    heightProperty.multiply(ratioLocation[1].yRatio),
                    widthProperty.multiply(ratioLocation[1].xRatio),
                    heightProperty.multiply(ratioLocation[1].yRatio + 0.4),
                    cardWidthProperty,
                    cardHeightProperty
            )
        };
    }

    private void setEventListeners(){
        GuiReporter.getInstance().addEventHandler(DropCardEvent.MY_TYPE, e->{
            Optional<CardFrame> opt = cardFrameManager.getIntersectingCards(e.getBounds())
                    .stream().filter(candid -> !candid.equals(e.getCardFrame()))
                    .findFirst();
            if(opt.isPresent()) {
                CardAddress address = cardFrameManager.getCardAddressByCard(opt.get().getCard());
                addRunnableToMainThreadForCard(
                        e.getCardFrame().getCard(),
                        () -> DuelMenuController.getInstance().attack(e.getCardFrame().getCard(), address)
                );
            }
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
            moveCardByAddress(event.getAttacker(), cardFrameManager.getCardAddressByCard(event.getDefender()), Duration.millis(300));
            moveCardByAddress(event.getAttacker(), cardFrameManager.getCardAddressByCard(event.getAttacker()), Duration.millis(300));
        });
        GuiReporter.getInstance().addGameEventHandler((GuiReporter.GameEventHandler<DirectAttackEvent>) (event)->{
            RatioLocation opponentPlayerLocation = gameMapLocation.getDirectPlayerLocation(event.getPlayer());
            CardFrame cardFrame = cardFrameManager.getCardFrameByCard(event.getAttacker());
            assert cardFrame != null;
            movementManager.animateCardMoving(cardFrame, opponentPlayerLocation, Duration.millis(400), true);
            movementManager.animateCardMoving(cardFrame, gameMapLocation.getLocationByCardAddress(game.getCardAddress(event.getAttacker())), Duration.millis(400), true);
        });
        GuiReporter.getInstance().addGameEventHandler((GuiReporter.GameEventHandler<MagicActivation>) (event)->{
            Platform.runLater(()-> {
                Card card = event.getCard();
                Text text = new Text(card.getName() + " activated!");
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

//        setOnMouseClicked(e->{
//            System.out.println(((e.getX() - getLayoutX()) / getWidth()) + "  " + ((e.getY() - getLayoutY()) / getHeight()));
//        });
    }


    public Duration getAnimationDuration(CardAddress address, Card card){
        if(cardFrameManager.getCardAddressByCard(card) == null)
            return Duration.ZERO;
        double speedRatio = 1;
        CardAddress previousAddress = cardFrameManager.getCardAddressByCard(card);
        if(previousAddress.getZone().equals(address.getZone())){
            if(address.isInGraveYard() || address.isInDeck())
                return Duration.ZERO;
        }
        if(address.isInHand()) {
            return Duration.millis(100 * speedRatio);
        }  else if(address.isInFieldZone()) {
            return Duration.millis(300 * speedRatio);
        } else if(address.isInGraveYard()) {
            if (previousAddress.isInGraveYard())
                return Duration.millis(50 * speedRatio);
            return Duration.millis(300 * speedRatio);
        } else if(address.isInMagicZone()){
            return Duration.millis(300 * speedRatio);
        } if(address.isInMonsterZone()) {
            return Duration.millis(300 * speedRatio);
        } if(address.isInDeck())
            return Duration.millis(100 * speedRatio);
        throw new Error("this will never happen");
    }

    private void createCards(Board board) {
        board.getAllCards().forEach(card->{
            CardAddress address = board.getCardAddress(card);
            RatioLocation location = gameMapLocation.getLocationByCardAddress(address);
            CardFrame cardFrame = new CardFrame(this, card, cardWidthProperty, cardHeightProperty);
            cardFrame.moveByBindingCoordinates(
                    widthProperty.multiply(location.xRatio),
                    heightProperty.multiply(location.yRatio)
            );
            Platform.runLater(()-> getChildren().add(cardFrame));
            cardFrameManager.put(cardFrame, address);
        });
        refreshGraveYard(board);
        refreshDeckZone(board);
    }

    private void moveCardByAddress(Card card, CardAddress address, Duration duration) {
        movementManager.animateCardMoving(
                cardFrameManager.getCardFrameByCard(card),
                gameMapLocation.getLocationByCardAddress(address),
                duration,
                true
        );
        cardFrameManager.put(cardFrameManager.getCardFrameByCard(card), address);
    }

    private void moveCardByAddress(CardAddress address, Card card) {
        moveCardByAddress(card, address, getAnimationDuration(address, card));
    }

    public void addRunnableToMainThreadForCard(Card card, GameRunnable runnable){
        if(card.getOwner().equals(GameController.getInstance().getGame().getCurrentPlayer()))
            addRunnableToMainThread(runnable);
        else
            CustomPrinter.println("You can't control your opponent's card", YuGiOh.model.enums.Color.Red);
    }
    public void addRunnableToMainThread(GameRunnable runnable){
        if(GameController.getInstance().getCurrentPlayerController() instanceof AIPlayerController){
            CustomPrinter.println("you can't do stuff in opponent's turn", YuGiOh.model.enums.Color.Red);
            return;
        }
        MainGameThread.getInstance().addRunnable(()->{
            try {
                runnable.run();
            } catch (LogicException e){
                Platform.runLater(()->new AlertBox().display(this, e.getMessage()));
            } catch (ResistToChooseCard e) {
            }
        });
    }

    public interface GameRunnable{
        void run() throws LogicException, RoundOverExceptionEvent, ResistToChooseCard;
    }
}