package YuGiOh.graphicController;

import YuGiOh.controller.LogicException;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Utils;
import YuGiOh.model.enums.MonsterAttribute;
import YuGiOh.model.enums.MonsterCardType;
import YuGiOh.model.enums.MonsterType;

import java.util.List;

public class FactoryMenuController extends BaseMenuController {
    private FactoryMenuController instance;
    private Monster selectedMonster = null;

    public FactoryMenuController() {
        instance = this;
    }

    public void selectBaseMonster(Monster monster) throws LogicException {
        if(selectedMonster != null)
            throw new LogicException("if you want to select another monster you have to discard this one first!");
        selectedMonster = monster.clone();
    }

    private void checkCardIsSelected() throws LogicException {
        if(selectedMonster == null)
            throw new LogicException("no card is selected!");
    }

    public void setAttackDamage(int value) throws LogicException {
        checkCardIsSelected();
        if(value < 0)
            throw new LogicException("attack damage cannot be negative");
        if(value > 10000)
            throw new LogicException("attack damage is at most 10000");
        selectedMonster.setAttackDamage(value);
    }
    public void setDefenseRate(int value) throws LogicException {
        checkCardIsSelected();
        if(value < 0)
            throw new LogicException("defense rate cannot be negative");
        if(value > 10000)
            throw new LogicException("defense rate is at most 10000");
        selectedMonster.setDefenseRate(value);
    }
    public void setLevel(int level) throws LogicException {
        checkCardIsSelected();
        if(level < 1)
            throw new LogicException("level must be between 1 and 10");
        if(level > 10)
            throw new LogicException("level must be between 1 and 10");
        selectedMonster.setLevel(level);
    }
    public void setMonsterType (MonsterType type) throws LogicException {
        checkCardIsSelected();
        selectedMonster.setMonsterType(type);
    }
    public void setMonsterAttribute (MonsterAttribute attribute) throws LogicException {
        checkCardIsSelected();
        selectedMonster.setAttribute(attribute);
    }
    public void setMonster(MonsterCardType cardType) throws LogicException {
        checkCardIsSelected();
        selectedMonster.setMonsterCardType(cardType);
    }

    public List<Card> getInventedCards() {
        return Utils.getInventedCards();
    }
    public void submitThisMonster() throws LogicException {
        checkCardIsSelected();
        Utils.addCardToInvented(selectedMonster.clone());
    }
    public void discardThisMonster() throws LogicException {
        checkCardIsSelected();
        selectedMonster = null;
    }
    public void discardFromInvented(Card card) throws LogicException {
        Utils.removeCardFromInvented(card);
    }

    public int getPrice() throws LogicException {
        checkCardIsSelected();
        double x = selectedMonster.getLevel();
        double y = selectedMonster.getAttackDamageOnCard();
        double z = selectedMonster.getDefenseRateOnCard();
        double w = 0;
        if(selectedMonster.getMonsterCardType().equals(MonsterCardType.EFFECT))
            w = 1;
        if(selectedMonster.getMonsterCardType().equals(MonsterCardType.RITUAL))
            w = 2;
        double A = selectedMonster.getPrice();
        double B =
                + 1.52471426e+03 * x
                -4.42142022e+00 * y
                -2.14990595e+00 * z
                +1.87980583e+02 * x * x
                -1.15270277e+00 * x * y
                -4.53481107e-01 * x * z
                + 2.52208740e-03 * y * y
                + 2.07575957e-03 * y * z
                + 5.27207140e-04 * z * z;
        double ret = Math.max(Math.max(A * 0.75, B * 0.75), A/2 + B/2 + 150 * w * w * w);
        return ((int)(ret / 100) * 100);
    }
}
