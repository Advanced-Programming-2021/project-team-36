package YuGiOh.model.card.action;

import YuGiOh.controller.GameController;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.Game;
import YuGiOh.model.Player.Player;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.event.SummonEvent;
import YuGiOh.model.enums.Color;
import YuGiOh.model.enums.MonsterState;
import YuGiOh.model.enums.SummonType;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.view.cardSelector.ResistToChooseCard;

import java.util.Arrays;

public class SummonAction extends Action {
    public SummonAction(SummonEvent event) {
        super(event);
        this.effect = () -> {
            GameController gameController = GameController.getInstance();
            preprocess();
            Player player = event.getPlayer();
            Monster monster = event.getMonster();
            monster.setOwner(player);
            if (event.getRequiredTributes() > 0)
                for (Card card : event.getChosenCardsToTribute())
                    gameController.moveCardToGraveYard(card);
            gameController.removeCardFromGame(monster);
            gameController.addCardToBoard(monster);
            monster.setMonsterState(event.getMonsterState());
            if (!event.getSummonType().equals(SummonType.SPECIAL))
                gameController.setSummoned(monster.getOwner());
            CustomPrinter.println(String.format("%s has summoned %s successfully.", player.getUser().getUsername(), monster.getName()), Color.Green);
        };
    }

    private void preprocess() throws ResistToChooseCard {
        SummonEvent event = (SummonEvent) getEvent();
        PlayerController playerController = GameController.getInstance().getPlayerControllerByPlayer(event.getPlayer());
        if (event.getRequiredTributes() > 0)
            event.setChosenCardsToTribute(Arrays.asList(playerController.chooseKCards(String.format("Choose %d cards to tribute", event.getRequiredTributes()), event.getRequiredTributes(), event.getTributeCondition())));
        if (event.getMonsterState() == null) {
            boolean AttackingState = playerController.askRespondToQuestion("which position you want to summon?", "defending", "attacking");
            event.setMonsterState((AttackingState ? MonsterState.DEFENSIVE_OCCUPIED : MonsterState.OFFENSIVE_OCCUPIED));
        }
    }

    @Override
    public void validateEffect() throws ValidateResult {
        SummonEvent event = (SummonEvent) getEvent();
        ValidateTree.checkSummon(event.getPlayer(), event.getMonster(), event.getSummonType());
        ValidateTree.checkTribute(event.getPlayer(), event.getRequiredTributes(), event.getTributeCondition());

    }
}
