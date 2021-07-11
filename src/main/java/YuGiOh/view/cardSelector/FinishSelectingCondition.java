package YuGiOh.view.cardSelector;

import YuGiOh.model.card.Card;

import java.util.List;

public interface FinishSelectingCondition {
    public boolean canFinish(List<Card> cardList);
}
