package YuGiOh.model.card.magicCards.spells;

import YuGiOh.controller.GameController;
import YuGiOh.model.card.Magic;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.enums.Icon;
import YuGiOh.model.enums.MonsterType;
import YuGiOh.model.enums.Status;
import YuGiOh.model.enums.Color;
import YuGiOh.utils.CustomPrinter;

import java.util.concurrent.CompletableFuture;

public class ClosedForest extends Spell {

    public ClosedForest(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    int lastAliveTurn = -1;

    @Override
    public int affectionOnAttackingMonster(Monster monster) {
        MonsterType monsterType = monster.getMonsterType();
        if (monster.getOwner().equals(this.getOwner()) && monsterType.equals(MonsterType.BEAST))
            return 100 * this.getOwner().getBoard().getGraveYard().size();
        return 0;
    }

    @Override
    protected Effect getEffect() {
        return () -> {
            lastAliveTurn = GameController.getInstance().getGame().getTurn();
            CustomPrinter.println(String.format("<%s> activated field spell <%s>", this.getOwner().getUser().getUsername(), this.getName()), Color.Yellow);
            CustomPrinter.println(this, Color.Gray);
            return CompletableFuture.completedFuture(null);
        };
    }

    @Override
    public boolean letMagicActivate(Magic magic){
        return magic.equals(this) || !magic.getIcon().equals(Icon.FIELD);
    }

    @Override
    public final boolean isActivated(){
        return GameController.getInstance().getGame().getTurn() == lastAliveTurn;
    }

    @Override
    public final void startOfNewTurn() {
        if(super.isActivated())
            lastAliveTurn = GameController.getInstance().getGame().getTurn();
    }

    @Override
    public boolean canActivateEffect() {
        return true;
    }
}