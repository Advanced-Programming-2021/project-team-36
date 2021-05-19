package model.card.magicCards.traps;

import controller.GameController;
import model.card.Card;
import model.card.Monster;
import model.card.Trap;
import model.card.action.Action;
import model.card.action.AttackEvent;
import model.card.action.Effect;
import model.enums.Icon;
import model.enums.MonsterState;
import model.enums.Status;

public class MirrorForce extends Trap {

    public MirrorForce(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    public Effect activateEffect() {
        assert canActivateEffect();
        return ()-> {
            for (Card card : GameController.getInstance().getGame().getOtherPlayer(this.owner).getBoard().getAllCardsOnBoard()) {
                if(card instanceof Monster) {
                    Monster monster = (Monster) card;
                    if (monster.getMonsterState().equals(MonsterState.OFFENSIVE_OCCUPIED))
                        GameController.getInstance().moveCardToGraveYard(card);
                }
            }
        };
    }

    @Override
    public boolean canActivateEffect() {
        if(getChain().isEmpty())
            return false;
        Action action = getChain().peek();
        if(action.getEvent() instanceof AttackEvent){
            AttackEvent event = (AttackEvent) action.getEvent();
            return event.getAttacker().owner.equals(GameController.getInstance().getGame().getOtherPlayer(this.owner));
        }
        return false;
    }
}
