package YuGiOh.view.cardSelector;

import YuGiOh.controller.LogicException;
import YuGiOh.controller.menu.DuelMenuController;
import YuGiOh.model.card.Card;
import YuGiOh.model.enums.MonsterState;
import YuGiOh.view.gui.*;
import YuGiOh.view.gui.*;
import YuGiOh.view.gui.event.ClickOnCard;

import java.util.ArrayList;
import java.util.List;

public class CardSelector {
    private final List<CardFrame> selectedCards;
    private SelectCondition condition;
    private SelectMode selectMode;
    private GameField gameRoot;
    private DuelInfoBox infoBox;

    public CardSelector(GameField gameRoot, DuelInfoBox infoBox){
        this.gameRoot = gameRoot;
        this.infoBox = infoBox;
        selectedCards = new ArrayList<>();
        gameRoot.addEventHandler(ClickOnCard.MY_TYPE, e->{
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
        });
        refresh();
    }

    private void loadInfoBox(CardFrame cardFrame){
        // todo this should come from the card not me :))
        int buttonFontSize = 14;
        infoBox.addInfo(cardFrame.getImage(),
                new CustomButton("summon", buttonFontSize, ()-> gameRoot.runAndAlert(
                                    ()-> DuelMenuController.getInstance().summonCard(cardFrame.getCard()),
                                    ()->{ clearInfoBox(); deselectAll(); }
                                    )),
                new CustomButton("set", buttonFontSize, ()-> gameRoot.runAndAlert(
                        ()-> DuelMenuController.getInstance().setCard(cardFrame.getCard()),
                        ()->{ clearInfoBox(); deselectAll(); }
                )),
                new CustomButton("change position", buttonFontSize, ()->{
                    ArrayList<CustomButton> buttons = new ArrayList<>();
                    for(MonsterState state : new MonsterState[]{MonsterState.DEFENSIVE_HIDDEN, MonsterState.DEFENSIVE_OCCUPIED, MonsterState.OFFENSIVE_OCCUPIED}){
                        buttons.add(new CustomButton(state.getName(), buttonFontSize, ()->{
                            gameRoot.runAndAlert(
                                    ()->DuelMenuController.getInstance().changeCardPosition(cardFrame.getCard(), state),
                                    ()->{ clearInfoBox(); deselectAll(); }
                            );
                        }));
                    }
                    new AlertBox().display(gameRoot, "choose state", buttons);
                }),
                new CustomButton("flip summon", buttonFontSize, ()->gameRoot.runAndAlert(
                        ()-> DuelMenuController.getInstance().flipSummon(cardFrame.getCard()),
                        ()->{ clearInfoBox(); deselectAll(); }
                )),
                new CustomButton("activate effect", buttonFontSize, ()->{
                    gameRoot.runAndAlert(
                            ()-> DuelMenuController.getInstance().activateEffect(cardFrame.getCard()),
                            ()->{ clearInfoBox(); deselectAll(); }
                    );
                }),
                new CustomButton("direct attack", buttonFontSize, ()->gameRoot.runAndAlert(
                        ()-> DuelMenuController.getInstance().directAttack(cardFrame.getCard()),
                        ()->{ clearInfoBox(); deselectAll(); }
                ))
        );
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
    }
    public void refresh(){
        refresh(Conditions.noCondition, SelectMode.Normal);
    }

    public enum SelectMode{
        Normal, Choosing;
    }
}
