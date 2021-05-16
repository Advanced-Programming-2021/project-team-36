package controller.events;

import model.card.Card;

public class SummonEvent extends GameEvent{
    public Card summonedCard;

    SummonEvent(Card summonedCard){
        super();
        this.summonedCard = summonedCard;
    }
}
