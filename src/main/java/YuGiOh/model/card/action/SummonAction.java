package YuGiOh.model.card.action;

import YuGiOh.controller.GameController;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.Player.Player;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.event.SummonEvent;
import YuGiOh.model.enums.Color;
import YuGiOh.model.enums.MonsterState;
import YuGiOh.model.enums.SummonType;
import YuGiOh.model.exception.ValidateResult;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.model.exception.ResistToChooseCard;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class SummonAction extends Action {
    public SummonAction(SummonEvent event) {
        super(event, ()->{
            GameController gameController = GameController.getInstance();
            Player player = event.getPlayer();
            Monster monster = event.getMonster();
            monster.readyForBattle(player);
            if (event.getRequiredTributes() > 0)
                for (Card card : event.getChosenCardsToTribute())
                    gameController.moveCardToGraveYard(card);
            gameController.addCardToBoard(monster);
            monster.setMonsterState(event.getMonsterState());
            if (!event.getSummonType().equals(SummonType.SPECIAL))
                gameController.setSummoned(monster.getOwner());
            CustomPrinter.println(String.format("%s has summoned %s successfully.", player.getUser().getUsername(), monster.getName()), Color.Green);
            return CompletableFuture.completedFuture(null);
        });
    }

    public void validateEffect() throws ValidateResult {
        SummonEvent event = (SummonEvent) getEvent();
        ValidateTree.checkSummon(event.getPlayer(), event.getMonster(), event.getSummonType());
        ValidateTree.checkTribute(event.getPlayer(), event.getRequiredTributes(), event.getTributeCondition());
    }

    @Override
    protected CompletableFuture<Void> preprocess() {
        SummonEvent event = (SummonEvent) getEvent();
        PlayerController playerController = GameController.getInstance().getPlayerControllerByPlayer(event.getPlayer());
        CompletableFuture<Void> ret = CompletableFuture.completedFuture(null);
        if (event.getRequiredTributes() > 0) {
            ret = ret.thenCompose(dum->playerController.chooseKCards(String.format("Choose %d cards to tribute", event.getRequiredTributes()), event.getRequiredTributes(), event.getTributeCondition())
                    .thenAccept(event::setChosenCardsToTribute));
        }
        if (event.getMonsterState() == null) {
            ret = ret.thenCompose(dum->playerController.askRespondToQuestion("which position you want to summon?", "defending", "attacking")
                    .thenAccept(res ->
                        event.setMonsterState((res ? MonsterState.DEFENSIVE_OCCUPIED : MonsterState.OFFENSIVE_OCCUPIED))
                    ));
        }
        return ret;
    }
}
