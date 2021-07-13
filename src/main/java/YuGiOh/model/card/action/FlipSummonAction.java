package YuGiOh.model.card.action;

import YuGiOh.controller.GameController;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.Player.Player;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.event.FlipSummonEvent;
import YuGiOh.model.card.event.SummonEvent;
import YuGiOh.model.enums.Color;
import YuGiOh.model.enums.MonsterState;
import YuGiOh.model.enums.SummonType;
import YuGiOh.model.exception.ValidateResult;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.model.exception.ResistToChooseCard;

import java.util.concurrent.CompletableFuture;

public class FlipSummonAction extends Action {
    public FlipSummonAction(FlipSummonEvent event) {
        super(event, ()->{
            GameController gameController = GameController.getInstance();
            Player player = event.getPlayer();
            Monster monster = event.getMonster();
            monster.setMonsterState(event.getMonsterState());
            if (!event.getSummonType().equals(SummonType.SPECIAL))
                gameController.setSummoned(monster.getOwner());
            monster.changeFromHiddenToOccupiedIfCanEffect();
            CustomPrinter.println(String.format("%s has flip summoned %s successfully.", player.getUser().getUsername(), monster.getName()), Color.Green);
            return CompletableFuture.completedFuture(null);
        });
    }

    @Override
    protected CompletableFuture<Void> preprocess() {
        FlipSummonEvent event = (FlipSummonEvent) getEvent();
        PlayerController playerController = GameController.getInstance().getPlayerControllerByPlayer(event.getPlayer());
        return playerController.askRespondToQuestion("which position you want to summon?", "defending", "attacking")
                .thenAccept(res->
                    event.setMonsterState((res ? MonsterState.DEFENSIVE_OCCUPIED : MonsterState.OFFENSIVE_OCCUPIED))
                );
    }

    @Override
    public void validateEffect() throws ValidateResult {
        SummonEvent event = (SummonEvent) getEvent();
        ValidateTree.checkSummon(event.getPlayer(), event.getMonster(), event.getSummonType());
    }
}
