package YuGiOh.controller.player;

import YuGiOh.controller.GameController;
import YuGiOh.model.Player.AIPlayer;
import YuGiOh.model.card.*;
import YuGiOh.model.enums.MonsterState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AggressiveAIPlayerController extends AIPlayerController {
    public AggressiveAIPlayerController(AIPlayer player) {
        super(player);
    }

    @Override
    public void mainPhase(){
        Random rnd = new Random();
        List<Card> allCards = new ArrayList<>(player.getBoard().getAllCards());
        while (!allCards.isEmpty()) {
            Card card = allCards.get(rnd.nextInt(allCards.size()));
            allCards.remove(card);
            if (card instanceof Monster) {
                if (rnd.nextInt(2) == 0)
                    noErrorSpecialSummonCard((Monster) card);
                noErrorSummonCard((Monster) card);
                noErrorMonsterActivateEffect((Monster) card);
            }
            if (card instanceof Trap) {
                noErrorSetMagic((Magic) card);
            }
            if (card instanceof Spell) {
                noErrorActivateEffect((Spell) card);
            }
        }
        GameController.getInstance().goNextPhaseAndNotify();
    }

    @Override
    public void controlBattlePhase() {
        Random rnd = new Random();
        List<Card> allCards = new ArrayList<>(player.getBoard().getAllCards());
        while (!allCards.isEmpty()) {
            Card card = allCards.get(rnd.nextInt(allCards.size()));
            allCards.remove(card);
            if (card instanceof Monster) {
                for (Card opponentCard : GameController.getInstance().getGame().getOtherPlayer(player).getBoard().getAllCardsOnBoard()) {
                    if (opponentCard instanceof Monster)
                        noErrorAttack((Monster) card, (Monster) opponentCard);
                }
                noErrorMonsterActivateEffect((Monster) card);
                noErrorDirectAttack((Monster) card);
            } else if (card instanceof Magic) {
                noErrorSetMagic((Magic) card);
            }
            if (card instanceof Spell) {
                noErrorActivateEffect((Spell) card);
            }
        }
        GameController.getInstance().goNextPhaseAndNotify();
    }

    @Override
    public boolean askRespondToQuestion(String question, String yes, String no) {
        if(question.equals("which position you want to summon?"))
            return yes.equals("attacking");
        Random rand = new Random();
        return rand.nextBoolean();
    }
}
