package edu.sharif.nameless.in.seattle.yugioh.model.card.magicCards.traps;

import edu.sharif.nameless.in.seattle.yugioh.controller.GameController;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Card;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Monster;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Trap;
import edu.sharif.nameless.in.seattle.yugioh.model.card.action.Action;
import edu.sharif.nameless.in.seattle.yugioh.model.card.action.AttackEvent;
import edu.sharif.nameless.in.seattle.yugioh.model.card.action.Effect;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.Icon;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.MonsterState;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.Status;

public class MirrorForce extends Trap {

    public MirrorForce(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    public Effect activateEffect() {
        assert canActivateEffect();
        return () -> {
            for (Card card : GameController.getInstance().getGame().getOtherPlayer(this.owner).getBoard().getAllCardsOnBoard()) {
                if (card instanceof Monster) {
                    Monster monster = (Monster) card;
                    if (monster.getMonsterState().equals(MonsterState.OFFENSIVE_OCCUPIED))
                        GameController.getInstance().getPlayerControllerByPlayer(monster.owner).moveCardToGraveYard(card);
                }
            }
        };
    }

    @Override
    public boolean canActivateEffect() {
        if (getChain().isEmpty())
            return false;
        Action action = getChain().peek();
        if (action.getEvent() instanceof AttackEvent) {
            AttackEvent event = (AttackEvent) action.getEvent();
            return event.getAttacker().owner.equals(GameController.getInstance().getGame().getOtherPlayer(this.owner));
        }
        return false;
    }
}
