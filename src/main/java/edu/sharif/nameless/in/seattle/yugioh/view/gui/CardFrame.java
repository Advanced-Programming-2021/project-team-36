package edu.sharif.nameless.in.seattle.yugioh.view.gui;

import edu.sharif.nameless.in.seattle.yugioh.controller.GameController;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Card;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Monster;
import edu.sharif.nameless.in.seattle.yugioh.view.gui.event.ClickOnCard;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

public class CardFrame extends ImageView {
    BooleanProperty isSelected = new SimpleBooleanProperty();
    @Getter
    Card card;

    CardFrame(Card card, DoubleBinding widthProperty, DoubleBinding heightProperty){
        super();

        this.card = card;

        // todo implement this in hand observable better
        SimpleBooleanProperty inHandObservable = new SimpleBooleanProperty(true);
        card.owner.getBoard().getCardsOnHand().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                inHandObservable.set(card.owner.getBoard().getCardsOnHand().contains(card));
            }
        });

        imageProperty().bind(Bindings.when(card.facedUpProperty().or(inHandObservable)).then(getFacedUpImage()).otherwise(getHiddenImage()));
        fitWidthProperty().bind(widthProperty);
        fitHeightProperty().bind(heightProperty);

        if(card instanceof Monster)
            rotateProperty().bind(Bindings.when(((Monster) card).isDefensive()).then(90).otherwise(0));

        effectProperty().bind(
                Bindings.when(hoverProperty().or(isSelected))
                        .then((Effect) new Bloom())
                        .otherwise(new DropShadow())
        );

        setOnMouseClicked(e->{
            // todo we haven't handled clicking on dead cards
            fireEvent(new ClickOnCard(this));
        });
    }

    Image getHiddenImage(){
        return new Image(Utils.getAsset("Cards/hidden.png").toURI().toString());
    }
    Image getFacedUpImage(){
        return Utils.getCardImage(card);
    }
    public void select(){
        isSelected.set(true);
    }
    public void deselect(){
        isSelected.set(false);
    }
}
