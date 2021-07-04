package YuGiOh.view.gui;

import YuGiOh.controller.GameController;
import YuGiOh.model.Game;
import YuGiOh.model.Player.Player;
import YuGiOh.model.card.Card;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;

public class ObservableBuilder {
    public static BooleanBinding getInHandBinding(Card card) {
        return new BooleanBinding() {
            {
                super.bind(
                        card.getOwner().getBoard().getCardsOnHand(),
                        card.ownerProperty()
                );
            }
            @Override
            protected boolean computeValue() {
                return card.getOwner().getBoard().getCardsOnHand().contains(card);
            }
        };
    }

    public static BooleanBinding myTurnBinding(Card card) {
        return new BooleanBinding() {
            {
                super.bind(
                        GameController.getInstance().getGame().currentPlayerProperty(),
                        card.ownerProperty()
                );
            }
            @Override
            protected boolean computeValue() {
                return GameController.getInstance().getGame().getCurrentPlayer().equals(card.getOwner());
            }
        };
    }

}
