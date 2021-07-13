package YuGiOh.model.card.action;

import YuGiOh.controller.GameController;
import YuGiOh.model.Game;
import YuGiOh.model.Player.Player;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.event.ChangeMonsterPositionEvent;
import YuGiOh.model.enums.Color;
import YuGiOh.model.enums.MonsterState;
import YuGiOh.model.enums.Phase;
import YuGiOh.model.exception.ResistToChooseCard;
import YuGiOh.model.exception.ValidateResult;
import YuGiOh.utils.CustomPrinter;

import java.util.concurrent.CompletableFuture;

public class ChangeMonsterPositionAction extends Action {
    private final Monster monster;
    private final MonsterState monsterState;
    private final Player player;

    public ChangeMonsterPositionAction(Player player, Monster monster, MonsterState monsterState) {
        super(new ChangeMonsterPositionEvent(monster, monsterState), ()->{
            monster.setMonsterState(monsterState);
            CustomPrinter.println(String.format("<%s>'s <%s>'s position changed to %s", player.getUser().getUsername(), monster.getName(), monsterState), Color.Green);
            return CompletableFuture.completedFuture(null);
        });
        this.monster = monster;
        this.monsterState = monsterState;
        this.player = player;
    }

    @Override
    public void validateEffect() throws ValidateResult, ValidateResult {
        Game game = GameController.getInstance().getGame();
        if (!GameController.getInstance().getCurrentPlayerController().getPlayer().getBoard().getMonsterCardZone().containsValue(monster))
            throw new ValidateResult("you can't change this card position");
        if (!game.getPhase().equals(Phase.MAIN_PHASE1) && !game.getPhase().equals(Phase.MAIN_PHASE2))
            throw new ValidateResult("you can’t do this action in this phase");
        if (monster.getMonsterState().equals(monsterState))
            throw new ValidateResult("this card is already in the wanted position");
        if (monsterState.equals(MonsterState.DEFENSIVE_HIDDEN))
            throw new ValidateResult("it's pointless to hide your card");
        if (monster.getMonsterState().equals(MonsterState.DEFENSIVE_HIDDEN))
            throw new ValidateResult("you should flip summon it");
    }
}
