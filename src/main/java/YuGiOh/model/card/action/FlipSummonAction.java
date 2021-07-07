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
import YuGiOh.utils.CustomPrinter;
import YuGiOh.archive.view.cardSelector.ResistToChooseCard;

public class FlipSummonAction extends Action {
    public FlipSummonAction(FlipSummonEvent event) {
        super(event);
        this.effect = () -> {
            GameController gameController = GameController.getInstance();
            preprocess();
            Player player = event.getPlayer();
            Monster monster = event.getMonster();
            monster.setMonsterState(event.getMonsterState());
            if (!event.getSummonType().equals(SummonType.SPECIAL))
                gameController.setSummoned(monster.getOwner());
            monster.changeFromHiddenToOccupiedIfCanEffect(); // todo : this should be an action
            CustomPrinter.println(String.format("%s has flip summoned %s successfully.", player.getUser().getUsername(), monster.getName()), Color.Green);
        };
    }

    protected void preprocess() throws ResistToChooseCard {
        FlipSummonEvent event = (FlipSummonEvent) getEvent();
        PlayerController playerController = GameController.getInstance().getPlayerControllerByPlayer(event.getPlayer());
        boolean AttackingState = playerController.askRespondToQuestion("which position you want to summon?", "defending", "attacking");
        event.setMonsterState((AttackingState ? MonsterState.DEFENSIVE_OCCUPIED : MonsterState.OFFENSIVE_OCCUPIED));
        // todo : after making changeFromHiddenToOccupiedIfCanEffect an action, this should preprocess to
    }

    @Override
    public void validateEffect() throws ValidateResult {
        SummonEvent event = (SummonEvent) getEvent();
        ValidateTree.checkSummon(event.getPlayer(), event.getMonster(), event.getSummonType());
        // todo : after making changeFromHiddenToOccupiedIfCanEffect an action, this should preprocess to
    }
}
