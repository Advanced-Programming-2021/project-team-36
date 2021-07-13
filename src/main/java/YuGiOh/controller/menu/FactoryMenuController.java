package YuGiOh.controller.menu;

import YuGiOh.model.exception.LogicException;
import YuGiOh.model.User;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Utils;
import YuGiOh.model.enums.MonsterAttribute;
import YuGiOh.model.enums.MonsterCardType;
import YuGiOh.model.enums.MonsterType;
import lombok.Getter;

import java.util.List;

public class FactoryMenuController extends BaseMenuController {
    @Getter
    private static FactoryMenuController instance;
    private Monster selectedMonster = null;
    private User user;

    public FactoryMenuController(User user) {
        this.user = user;
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
    public void setDescription(String description) throws LogicException {
        checkCardIsSelected();
        selectedMonster.setDescription(description);
    }

    public void setCardName(String name) throws LogicException {
        checkCardIsSelected();
        selectedMonster.setName(name);
    }

    public List<Card> getInventedCards() {
        return Utils.getInventedCards();
    }
    public void submitThisMonster() throws LogicException {
        checkCardIsSelected();
        if (user.getBalance() < getPrice())
            throw new LogicException("You don't have enough money!");
        selectedMonster.setPrice(getPrice());
        Utils.addCardToInvented(selectedMonster.clone());
        user.increaseBalance(-selectedMonster.getPrice() / 10);
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
        double ret = A + w * 3000 + x * 100 + y + z;
        return (int)ret;
    }
}
