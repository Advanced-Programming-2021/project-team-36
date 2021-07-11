package YuGiOh.model.card.action;

import YuGiOh.controller.GameController;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.Player.Player;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.event.SetMonster;
import YuGiOh.model.enums.Color;
import YuGiOh.model.enums.MonsterState;
import YuGiOh.model.enums.SummonType;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.view.cardSelector.ResistToChooseCard;

import java.util.Arrays;

public class SetMonsterAction extends Action {
    public SetMonsterAction(SetMonster event) {
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
            gameController.addCardToBoard(monster);
            monster.setMonsterState(MonsterState.DEFENSIVE_HIDDEN);
            gameController.setSummoned(monster.getOwner());
            CustomPrinter.println(String.format("%s has set %s successfully.", player.getUser().getUsername(), monster.getName()), Color.Green);
        };
    }

    @Override
    public void validateEffect() throws ValidateResult {
        SetMonster event = (SetMonster) getEvent();
        ValidateTree.checkSummon(event.getPlayer(), event.getMonster(), SummonType.NORMAL);
        ValidateTree.checkTribute(event.getPlayer(), event.getRequiredTributes(), event.getTributeCondition());
    }

    public void preprocess() throws ResistToChooseCard {
        SetMonster event = (SetMonster) getEvent();
        PlayerController playerController = GameController.getInstance().getPlayerControllerByPlayer(event.getPlayer());
        if (event.getRequiredTributes() > 0)
            event.setChosenCardsToTribute(Arrays.asList(playerController.chooseKCards(String.format("Choose %d cards to tribute", event.getRequiredTributes()), event.getRequiredTributes(), event.getTributeCondition())));
    }
}
