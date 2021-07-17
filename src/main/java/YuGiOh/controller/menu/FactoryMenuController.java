package YuGiOh.controller.menu;

import YuGiOh.model.exception.LogicException;
import YuGiOh.model.User;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Utils;
import YuGiOh.model.enums.MonsterAttribute;
import YuGiOh.model.enums.MonsterCardType;
import YuGiOh.model.enums.MonsterType;
import YuGiOh.network.packet.Request;
import lombok.Getter;

import java.util.List;

public class FactoryMenuController extends BaseMenuController {
    // todo submittong monster is different from buying it! We must not pay anything here.
    //  and the card must be added to database so that we can buy it later

    public static void submitThisMonster(Request request, Monster monster) throws LogicException {
        if(monster == null)
            throw new LogicException("no monster is selected");
        if (request.getUser().getBalance() < getPrice(monster))
            throw new LogicException("You don't have enough money!");
        monster.setPrice(getPrice(monster));
        Utils.addCardToInvented(monster.clone());
        request.getUser().increaseBalance(-monster.getPrice() / 10);
    }

    public static int getPrice(Monster monster) throws LogicException {
        double x = monster.getLevel();
        double y = monster.getAttackDamageOnCard();
        double z = monster.getDefenseRateOnCard();
        double w = 0;
        if(monster.getMonsterCardType().equals(MonsterCardType.EFFECT))
            w = 1;
        if(monster.getMonsterCardType().equals(MonsterCardType.RITUAL))
            w = 2;
        double A = monster.getPrice();
        double ret = A + w * 3000 + x * 100 + y + z;
        return (int)ret;
    }
}
