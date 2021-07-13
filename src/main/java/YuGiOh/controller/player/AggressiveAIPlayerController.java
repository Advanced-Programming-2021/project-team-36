package YuGiOh.controller.player;

import YuGiOh.controller.GameController;
import YuGiOh.model.Player.AIPlayer;
import YuGiOh.model.card.*;
import YuGiOh.model.card.action.Action;
import YuGiOh.model.card.action.NextPhaseAction;
import YuGiOh.model.enums.MonsterState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class AggressiveAIPlayerController extends AIPlayerController {
    public AggressiveAIPlayerController(AIPlayer player) {
        super(player);
    }

    protected List<Action> getAllActionsOfCard(Card card) {
        List<Action> actions = new ArrayList<>();
        if (card instanceof Monster) {
            actions.add(normalSummon((Monster) card));
            actions.add(flipSummon((Monster) card));
            actions.add(specialSummon((Monster) card));
            actions.add(activateMonsterEffect((Monster) card));
            actions.add(directAttack((Monster) card));
            for (Card opponentCard : GameController.getInstance().getGame().getOtherPlayer(player).getBoard().getAllCardsOnBoard()) {
                if (opponentCard instanceof Monster)
                    actions.add(attack((Monster) card, (Monster) opponentCard));
            }
        } else if (card instanceof Magic) {
            actions.add(setMagic((Magic) card));
        }
        if (card instanceof Spell) {
            actions.add(activateSpellEffect((Spell) card));
        }
        return actions;
    }

    @Override
    public CompletableFuture<Boolean> askRespondToQuestion(String question, String yes, String no) {
        if(question.equals("which position you want to summon?"))
            return CompletableFuture.completedFuture(yes.equals("attacking"));
        Random rand = new Random();
        return CompletableFuture.completedFuture(rand.nextBoolean());
    }
}
