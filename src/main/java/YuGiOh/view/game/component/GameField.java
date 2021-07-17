package YuGiOh.view.game.component;

import YuGiOh.controller.GameController;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.exception.GameException;
import YuGiOh.model.exception.eventException.RoundOverExceptionEvent;
import YuGiOh.controller.menu.DuelMenuController;
import YuGiOh.controller.player.AIPlayerController;
import YuGiOh.model.Board;
import YuGiOh.model.CardAddress;
import YuGiOh.model.Game;
import YuGiOh.model.Player.Player;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Magic;
import YuGiOh.model.card.Monster;
import YuGiOh.model.enums.Phase;
import YuGiOh.model.enums.ZoneType;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.model.exception.ResistToChooseCard;
import YuGiOh.view.game.*;
import YuGiOh.view.game.event.DropCardEvent;
import javafx.animation.ScaleTransition;
import javafx.collections.ListChangeListener;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class GameField extends Pane {
    private Game game;
    @Getter
    private GameMapLocation gameMapLocation;
    @Getter
    private GameCardFrameManager cardFrameManager;
    @Getter
    private GameCardMovementManager movementManager;
    private PileOfCardManager[] deckPile, graveYardPile;
    private ImageView background;

    @Getter
    private static GameField instance;

    public void init(Game game, GameMapLocation gameMapLocation){
        instance = this;
        this.game = game;
        this.gameMapLocation = gameMapLocation;
        this.cardFrameManager = new GameCardFrameManager(game);

        this.movementManager = new GameCardMovementManager(this);

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
            CompletableFuture<Void> completableFuture = moveCardByAddress(to, cardFrame);
            for(int i = 0; i < 2; i++) {
                graveYardPile[i].close(false);
                deckPile[i].close(false);
            }
            return completableFuture;
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
            if(e.getCardFrame().getCard() instanceof Monster) {
                Optional<CardFrame> opt = cardFrameManager.getIntersectingCards(e.getBounds())
                        .stream().filter(candid -> !candid.equals(e.getCardFrame()))
                        .findFirst();
                if (opt.isPresent()) {
                    if (opt.get().getCard() instanceof Monster) {
                        GameController.getInstance().addRunnableToMainThreadForCard(
                                e.getCardFrame().getCard(),
                                () -> {
                                    PlayerController controller = GameController.getInstance().getPlayerControllerByPlayer(e.getCardFrame().getCard().getOwner());
                                    controller.startChain(controller.attack((Monster) e.getCardFrame().getCard(), (Monster) opt.get().getCard()));
                                }
                        );
                    }
                }
            }
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
        setOnMouseClicked(e->{
//            Platform.runLater(()->{
                ImageView imageView = new ImageView(Utils.getImage("Texture/ring1.png"));
                getChildren().add(imageView);
                imageView.setX(e.getX());
                imageView.setY(e.getY());
                imageView.fitWidthProperty().bind(widthProperty().multiply(0.007));
                imageView.fitHeightProperty().bind(heightProperty().multiply(0.007));
                ScaleTransition t = new ScaleTransition(Duration.millis(400), imageView);
                t.setToX(7);
                t.setToY(7);
                t.setOnFinished(E->getChildren().remove(imageView));
                t.play();
//            });
        });
    }

    public Duration getAnimationDuration(CardAddress from, CardAddress to){
        if(from == null || to == null)
            throw new Error("this must never happen");
        double speedRatio = 3;
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
            getChildren().add(cardFrame);
            cardFrameManager.addNewCard(cardFrame, address);
        });
    }

    private CompletableFuture<Void> moveCardByAddress(CardFrame cardFrame, CardAddress address, Duration duration) {
        return movementManager.animateCardMoving(
                cardFrame,
                gameMapLocation.getLocationByCardAddress(address),
                duration
        );
    }

    private CompletableFuture<Void> moveCardByAddress(CardAddress address, CardFrame cardFrame) {
        return moveCardByAddress(cardFrame, address, getAnimationDuration(cardFrameManager.getCardAddressByCard(cardFrame.getCard()), address));
    }

    public interface GameRunnable{
        void run() throws GameException, RoundOverExceptionEvent;
    }
}