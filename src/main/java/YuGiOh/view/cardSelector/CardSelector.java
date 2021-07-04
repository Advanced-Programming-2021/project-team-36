package YuGiOh.view.cardSelector;

import YuGiOh.controller.LogicException;
import YuGiOh.model.card.Card;
import YuGiOh.view.gui.*;
import YuGiOh.view.gui.component.CardFrame;
import YuGiOh.view.gui.component.DuelInfoBox;
import YuGiOh.view.gui.event.ClickOnCardEvent;

import java.util.ArrayList;
import java.util.List;

public class CardSelector {
    private final List<CardFrame> selectedCards;
    private SelectCondition condition;
    private SelectMode selectMode;
    private final DuelInfoBox infoBox;
    private Runnable onAction;

    public CardSelector(DuelInfoBox infoBox){
        this.infoBox = infoBox;
        selectedCards = new ArrayList<>();
        GuiReporter.getInstance().addEventHandler(ClickOnCardEvent.MY_TYPE, e->{
            if(condition.canSelect(e.getCardFrame().getCard())) {
                if (selectedCards.contains(e.getCardFrame())) {
                    deselectCard(e.getCardFrame());
                } else {
                    if (selectMode.equals(SelectMode.Normal)) {
                        if (!selectedCards.isEmpty())
                            deselectCard(selectedCards.get(0));
                    }
                    selectCard(e.getCardFrame());
                }
            }
            if(onAction != null) {
                onAction.run();
            }
        });
        refresh();
    }

    private void loadInfoBox(CardFrame cardFrame){
        infoBox.addInfo(cardFrame);
    }

    private void clearInfoBox(){
        infoBox.clear();
    }

    private void selectCard(CardFrame cardFrame){
        if(selectMode.equals(SelectMode.Normal))
            loadInfoBox(cardFrame);
        cardFrame.select();
        selectedCards.add(cardFrame);
    }
    private void deselectCard(CardFrame cardFrame){
        if(selectMode.equals(SelectMode.Normal))
            clearInfoBox();
        cardFrame.deselect();
        selectedCards.remove(cardFrame);
    }
    private void deselectAll(){
        if(selectMode.equals(SelectMode.Normal))
            clearInfoBox();
        selectedCards.forEach(CardFrame::deselect);
        selectedCards.clear();
    }

    public Card getSelectedCard() throws LogicException {
        if (selectedCards.size() == 0)
            throw new LogicException("no card is selected");
        return selectedCards.get(0).getCard();
    }

    public List<Card> getSelectedCards(){
        List<Card> ret = new ArrayList<>();
        selectedCards.forEach(e->ret.add(e.getCard()));
        return ret;
    }

    public boolean isCardSelected(){
        return selectedCards.size() != 0;
    }

    public void refresh(SelectCondition condition, SelectMode mode){
        this.condition = condition;
        this.selectMode = mode;
        deselectAll();
        onAction = null;
    }
    public void refresh(){
        refresh(SelectConditions.noCondition, SelectMode.Normal);
    }

    public enum SelectMode{
        Normal, Choosing;
    }

    public void setOnAction(Runnable onAction) {
        this.onAction = onAction;
    }
}
