package YuGiOh.view.gui.component;

import YuGiOh.controller.GameController;
import YuGiOh.controller.MainGameThread;
import YuGiOh.model.card.Monster;
import YuGiOh.model.enums.Phase;
import YuGiOh.view.gui.ObservableBuilder;
import YuGiOh.view.gui.Utils;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.HashMap;

public class AttackingSword extends ImageView {
    private CardFrame cardFrame;
    private static HashMap<CardFrame, AttackingSword> cardToSword = new HashMap<>();
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
        fitWidthProperty().bind(cardFrame.widthProperty().multiply(0.4));
        fitHeightProperty().bind(cardFrame.heightProperty().multiply(0.3));
        setImage(Utils.getImage("Icon/sword.bmp"));
        setActivationConditions();
        setViewOrder(-1);
    }

    private void setActivationConditions() {
        if(cardFrame.getCard() instanceof Monster){
            Monster monster = (Monster) cardFrame.getCard();
            BooleanBinding activationNeeds = monster.facedUpProperty()
                    .and(monster.isDefensive().not())
                    .and(ObservableBuilder.inMonsterZoneBinding(monster))
                    .and(GameController.getInstance().getGame().phaseProperty().isEqualTo(Phase.BATTLE_PHASE))
                    .and(ObservableBuilder.myTurnBinding(monster));
            visibleProperty().bind(extraVisibility.and(activationNeeds));
            activationNeeds.addListener((InvalidationListener) o->{
                if(activationNeeds.get())
                    ready();
            });
        }
    }

    private void ready(DoubleBinding x, DoubleBinding y) {
        setTranslateX(0);
        setTranslateY(0);
        setRotate(0);
        show();
    }
    public void ready() {
        ready(cardFrame.getCenterXProperty(), cardFrame.getCenterYProperty());
    }
    public void shoot(DoubleBinding x, DoubleBinding y) {
        MainGameThread.getInstance().onlyBlockRunningThreadThenDoInGui(()-> {
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
                MainGameThread.getInstance().unlockTheThreadIfMain();
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
