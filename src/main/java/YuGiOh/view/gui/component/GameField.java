package YuGiOh.view.gui.component;

import YuGiOh.controller.GameController;
import YuGiOh.controller.LogicException;
import YuGiOh.controller.MainGameThread;
import YuGiOh.controller.events.RoundOverExceptionEvent;
import YuGiOh.graphicController.DuelMenuController;
import YuGiOh.controller.player.AIPlayerController;
import YuGiOh.model.Board;
import YuGiOh.model.CardAddress;
import YuGiOh.model.Game;
import YuGiOh.model.Player.Player;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Magic;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.event.DirectAttackEvent;
import YuGiOh.model.card.event.MagicActivation;
import YuGiOh.model.card.event.MonsterAttackEvent;
import YuGiOh.model.enums.Phase;
import YuGiOh.model.enums.ZoneType;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.view.cardSelector.ResistToChooseCard;
import YuGiOh.view.gui.*;
import YuGiOh.view.gui.event.DropCardEvent;
import YuGiOh.view.gui.event.DuelOverEvent;
import YuGiOh.view.gui.event.RoundOverEvent;
import YuGiOh.view.gui.sound.GameMediaHandler;
import javafx.application.Platform;
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
    private Game game;
    private GameMapLocation gameMapLocation;
    private GameCardFrameManager cardFrameManager;
    private GameCardMovementManager movementManager;
    private PileOfCardManager[] deckPile, graveYardPile;
    private ImageView background;

    public void init(Game game, GameMapLocation gameMapLocation){
        this.game = game;
        this.gameMapLocation = gameMapLocation;
        this.cardFrameManager = new GameCardFrameManager(game);

        this.movementManager = new GameCardMovementManager(this);

        // todo remove this and put somewhere else
        new GuiReporter(this);

        this.background = new ImageView(new Image(Utils.getAsset("Field/Normal.bmp").toURI().toString()));

        background.fitWidthProperty().bind(widthProperty());
        background.fitHeightProperty().bind(heightProperty());
        getChildren().add(background);
        getChildren().add(new PhaseLamps(this, gameMapLocation, Phase.DRAW_PHASE, Phase.STANDBY_PHASE, Phase.MAIN_PHASE1, Phase.BATTLE_PHASE, Phase.MAIN_PHASE2));

        createCards(game.getFirstPlayer().getBoard());
        createCards(game.getSecondPlayer().getBoard());

        this.cardFrameManager.setMoveHandler((cardFrame, to)->{
            for(int i = 0; i < 2; i++) {
                graveYardPile[i].close(false);
                deckPile[i].close(false);
            }
            moveCardByAddress(to, cardFrame);
            for(int i = 0; i < 2; i++) {
                graveYardPile[i].close(false);
                deckPile[i].close(false);
            }
        });

        this.deckPile = new PileOfCardManager[] {createPile(ZoneType.DECK, game.getFirstPlayer()), createPile(ZoneType.DECK, game.getSecondPlayer())};
        this.graveYardPile = new PileOfCardManager[] {createPile(ZoneType.GRAVEYARD, game.getFirstPlayer()), createPile(ZoneType.GRAVEYARD, game.getSecondPlayer())};

        getChildren().addAll(deckPile);
        getChildren().addAll(graveYardPile);

        background.toBack();
        setEventListeners();
    }

    private PileOfCardManager createPile(ZoneType zoneType, Player player) {
        return new PileOfCardManager(
                this,
                cardFrameManager,
                zoneType,
                player,
                gameMapLocation.getLocationByCardAddress(new CardAddress(zoneType, 1, player)),
                gameMapLocation.getZonePileOpenRatio(zoneType, player)
        );
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
        GuiReporter.getInstance().addGameEventHandler((GuiReporter.GameEventHandler<MonsterAttackEvent>) (event)->{
            DoubleBinding x = widthProperty().multiply(gameMapLocation.getLocationByCardAddress(cardFrameManager.getCardAddressByCard(event.getDefender())).xRatio);
            DoubleBinding y = heightProperty().multiply(gameMapLocation.getLocationByCardAddress(cardFrameManager.getCardAddressByCard(event.getDefender())).yRatio);
            AttackingSword.getOrCreateSwordForCard(cardFrameManager.getCardFrameByCard(event.getAttacker())).shoot(x, y);
        });
        GuiReporter.getInstance().addGameEventHandler((GuiReporter.GameEventHandler<DirectAttackEvent>) (event)->{
            DoubleBinding x = widthProperty().multiply(gameMapLocation.getDirectPlayerLocation(event.getPlayer()).xRatio);
            DoubleBinding y = heightProperty().multiply(gameMapLocation.getDirectPlayerLocation(event.getPlayer()).yRatio);
            AttackingSword.getOrCreateSwordForCard(cardFrameManager.getCardFrameByCard(event.getAttacker())).shoot(x, y);
        });

        // todo remove this
        GuiReporter.getInstance().addGameEventHandler((GuiReporter.GameEventHandler<MagicActivation>) (event)->{
            Platform.runLater(()-> {
                Card card = event.getMagic();
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

        for (Board board : Arrays.asList(game.getFirstPlayer().getBoard(), game.getSecondPlayer().getBoard())) {
            board.getFieldZoneCardObservableList().addListener((ListChangeListener<Magic>) (c) -> {
                Magic me = board.getFieldZoneCard();
                Magic field1 = game.getFirstPlayer().getBoard().getFieldZoneCard();
                Magic field2 = game.getSecondPlayer().getBoard().getFieldZoneCard();
                if(me == null && field1 != null)
                    me = field1;
                if(me == null && field2 != null)
                    me = field2;
                try {
                    Image image = Utils.getImage("Field/" + (me == null ? "Normal" : me.getName()) + ".bmp");
                    this.background.setImage(image);
                } catch (Throwable ignored){
                }
            });
        }
//        setOnMouseClicked(e->{
//            System.out.println(((e.getX() - getLayoutX()) / getWidth()) + "  " + ((e.getY() - getLayoutY()) / getHeight()));
//        });
    }


    public Duration getAnimationDuration(CardAddress from, CardAddress to){
        if(from == null || to == null)
            throw new Error("this must never happen");
        double speedRatio = 1;
        if(from.getZone().equals(to.getZone())){
            if(to.isInGraveYard() || to.isInDeck())
                return Duration.ZERO;
        }
        if(to.isInHand()) {
            return Duration.millis(100 * speedRatio);
        }  else if(to.isInFieldZone()) {
            return Duration.millis(300 * speedRatio);
        } else if(to.isInGraveYard()) {
            return Duration.millis(300 * speedRatio);
        } else if(to.isInMagicZone()){
            return Duration.millis(300 * speedRatio);
        } if(to.isInMonsterZone()) {
            return Duration.millis(300 * speedRatio);
        } if(to.isInDeck())
            return Duration.millis(100 * speedRatio);
        throw new Error("this will never happen");
    }

    public boolean getAnimationBlocking(CardAddress from, CardAddress to){
        if(from == null || to == null)
            throw new Error("this must never happen");
        if(from.getZone().equals(to.getZone())) {
            ZoneType zoneType = from.getZone();
            return zoneType.equals(ZoneType.HAND) || zoneType.equals(ZoneType.GRAVEYARD) || zoneType.equals(ZoneType.DECK);
        }
        return true;
    }


    private void createCards(Board board) {
        board.getAllCards().forEach(card->{
            CardAddress address = board.getCardAddress(card);
            RatioLocation location = gameMapLocation.getLocationByCardAddress(address);
            CardFrame cardFrame = new CardFrame(card);
            cardFrame.setGameField(this);
            cardFrame.prefHeightProperty().bind(heightProperty().multiply(gameMapLocation.getCardHeightRatio()));
            cardFrame.prefWidthProperty().bind(widthProperty().multiply(gameMapLocation.getCardWidthRatio()));
            if(card instanceof Monster)
                AttackingSword.getOrCreateSwordForCard(cardFrame);
            cardFrame.moveByBindingCoordinates(
                    widthProperty().multiply(location.xRatio),
                    heightProperty().multiply(location.yRatio)
            );
            Platform.runLater(()-> getChildren().add(cardFrame));
            cardFrameManager.addNewCard(cardFrame, address);
        });
    }

    private void moveCardByAddress(CardFrame cardFrame, CardAddress address, Duration duration) {
        movementManager.animateCardMoving(
                cardFrame,
                gameMapLocation.getLocationByCardAddress(address),
                duration,
                getAnimationBlocking(cardFrameManager.getCardAddressByCard(cardFrame.getCard()), address)
        );
    }

    private void moveCardByAddress(CardAddress address, CardFrame cardFrame) {
        moveCardByAddress(cardFrame, address, getAnimationDuration(cardFrameManager.getCardAddressByCard(cardFrame.getCard()), address));
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