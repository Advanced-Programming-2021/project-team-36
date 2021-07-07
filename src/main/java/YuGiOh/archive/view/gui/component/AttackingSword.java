package YuGiOh.archive.view.gui.component;

import YuGiOh.controller.GameController;
import YuGiOh.controller.MainGameThread;
import YuGiOh.model.card.Monster;
import YuGiOh.model.enums.Phase;
import YuGiOh.archive.view.gui.ObservableBuilder;
import YuGiOh.archive.view.gui.Utils;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.HashMap;

public class AttackingSword extends ImageView {
    private final CardFrame cardFrame;
    private static final HashMap<CardFrame, AttackingSword> cardToSword = new HashMap<>();
    private final SimpleBooleanProperty extraVisibility;

    public static AttackingSword getOrCreateSwordForCard(CardFrame cardFrame) {
        if(!cardToSword.containsKey(cardFrame)) {
            AttackingSword sword = new AttackingSword(cardFrame);
            cardToSword.put(cardFrame, sword);
            cardFrame.getChildren().add(sword);
        }
        return cardToSword.get(cardFrame);
    }
    private AttackingSword(CardFrame cardFrame) {
        super();
        this.cardFrame = cardFrame;
        this.extraVisibility = new SimpleBooleanProperty(false);
        fitWidthProperty().bind(cardFrame.widthProperty().multiply(0.7));
        fitHeightProperty().bind(cardFrame.heightProperty().multiply(0.6));
        setImage(Utils.getImage("Icon/attacksword.png"));
        setActivationConditions();
        setViewOrder(-1);
    }

    private void setActivationConditions() {
        if(cardFrame.getCard() instanceof Monster){
            Monster monster = (Monster) cardFrame.getCard();
            BooleanBinding inBattlePhase = GameController.getInstance().getGame().phaseProperty().isEqualTo(Phase.BATTLE_PHASE);
            BooleanBinding activationNeeds = monster.facedUpProperty()
                    .and(monster.isDefensive().not())
                    .and(ObservableBuilder.inMonsterZoneBinding(monster))
                    .and(inBattlePhase)
                    .and(ObservableBuilder.myTurnBinding(monster))
                    .and(((Monster) cardFrame.getCard()).allowAttackProperty());
            visibleProperty().bind(extraVisibility.and(activationNeeds));
            inBattlePhase.addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if(!oldValue && newValue)
                        ready();
                }
            });
        }
    }

    private void ready(DoubleBinding x, DoubleBinding y) {
        setTranslateX(0);
        setTranslateY(0);
        setRotate(0);
        show();
    }
    private void ready() {
        ready(cardFrame.getCenterXProperty(), cardFrame.getCenterYProperty());
    }
    public void shoot(DoubleBinding x, DoubleBinding y) {
        MainGameThread.OneWayTicket ticket = new MainGameThread.OneWayTicket();
        ticket.runOneWay(()->{
            TranslateTransition t = new TranslateTransition(Duration.millis(600), this);
            translateXProperty().unbind();
            translateYProperty().unbind();
            DoubleBinding toX = x.add(cardFrame.getCenterXProperty().negate());
            DoubleBinding toY = y.add(cardFrame.getCenterYProperty().negate());
            t.toXProperty().bind(toX);
            t.toYProperty().bind(toY);
            setRotate(Math.atan2(toY.get(), toX.get()) * 180 / Math.PI + 90);
            t.setOnFinished(e -> {
                hide();
                ticket.free();
            });
            t.play();
        });
    }
    public void hide() {
        extraVisibility.set(false);
    }
    public void show() {
        extraVisibility.set(true);
    }
}
