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
import YuGiOh.model.exception.ValidateResult;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.model.exception.ResistToChooseCard;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class SetMonsterAction extends Action {
    public SetMonsterAction(SetMonster event) {
        super(event, ()->{
            GameController gameController = GameController.getInstance();
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
            return CompletableFuture.completedFuture(null);
        });
    }

    @Override
    public void validateEffect() throws ValidateResult {
        SetMonster event = (SetMonster) getEvent();
        ValidateTree.checkSummon(event.getPlayer(), event.getMonster(), SummonType.NORMAL);
        ValidateTree.checkTribute(event.getPlayer(), event.getRequiredTributes(), event.getTributeCondition());
    }

    public CompletableFuture<Void> preprocess() {
        SetMonster event = (SetMonster) getEvent();
        PlayerController playerController = GameController.getInstance().getPlayerControllerByPlayer(event.getPlayer());
        if (event.getRequiredTributes() > 0) {
            return playerController.chooseKCards(String.format("Choose %d cards to tribute", event.getRequiredTributes()), event.getRequiredTributes(), event.getTributeCondition())
                    .thenAccept(event::setChosenCardsToTribute);
        } else {
            return CompletableFuture.completedFuture(null);
        }
    }
}
