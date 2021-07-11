package YuGiOh.view.game;

import YuGiOh.controller.GameController;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;
import javafx.beans.binding.BooleanBinding;

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

    public static BooleanBinding inGraveyardBinding(Card card) {
        return new BooleanBinding() {
            {
                super.bind(
                        card.ownerProperty(),
                        card.getOwner().getBoard().getGraveYard()
                );
            }
            @Override
            protected boolean computeValue() {
                return card.getOwner().getBoard().getGraveYard().contains(card);
            }
        };
    }

    public static BooleanBinding inDeckBinding(Card card) {
        return new BooleanBinding() {
            {
                super.bind(
                        card.ownerProperty(),
                        card.getOwner().getBoard().getMainDeck().getCards()
                );
            }
            @Override
            protected boolean computeValue() {
                return card.getOwner().getBoard().getMainDeck().getCards().contains(card);
            }
        };
    }

    public static BooleanBinding inMonsterZoneBinding(Monster monster) {
        return new BooleanBinding() {
            {
                super.bind(
                        monster.ownerProperty(),
                        monster.getOwner().getBoard().getMonsterCardZone()
                );
            }
            @Override
            protected boolean computeValue() {
                return monster.getOwner().getBoard().getMonsterCardZone().containsValue(monster);
            }
        };
    }
}
