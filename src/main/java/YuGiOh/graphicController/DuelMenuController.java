package YuGiOh.graphicController;

import YuGiOh.controller.*;
import YuGiOh.controller.events.RoundOverExceptionEvent;
import YuGiOh.graphicView.DuelMenuView;
import YuGiOh.model.Duel;
import YuGiOh.model.card.*;
import YuGiOh.view.cardSelector.ResistToChooseCard;
import YuGiOh.model.CardAddress;
import YuGiOh.model.enums.Color;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.view.gui.GuiReporter;
import YuGiOh.view.gui.event.RoundOverEvent;
import lombok.Getter;
import YuGiOh.model.enums.MonsterState;
import lombok.Setter;

import java.util.List;

public class DuelMenuController extends BaseMenuController {
    @Getter
    public static DuelMenuController instance;
    @Getter
    private final Duel duel;
    private GameController gameController;


    @Setter @Getter
    private DuelMenuView view;

    public DuelMenuController(Duel duel){
        this.duel = duel;
        instance = this;
    }

    public void printCurrentPhase() {
        CustomPrinter.println("phase: " + duel.getCurrentGame().getPhase().getVerboseName(), Color.Blue);
    }

    public void goNextPhase() {
        gameController.goNextPhaseAndNotify();
    }

    public void summonCard(Card card) throws LogicException, ResistToChooseCard {
        if (card instanceof Magic)
            throw new LogicException("this card is magic and you can't summon it");
        gameController.getCurrentPlayerController().normalSummon((Monster) card);
        view.resetSelector();
    }

    public void specialSummon(Card card) throws LogicException, ResistToChooseCard {
        if (card instanceof Magic)
            throw new LogicException("this card is magic and you can't special summon it");
        gameController.getCurrentPlayerController().specialSummon((Monster) card);
        view.resetSelector();
    }

    public void setCard(Card card) throws LogicException, ResistToChooseCard {
        if (card instanceof Monster)
            gameController.getCurrentPlayerController().setMonster((Monster) card);
        else
            gameController.getCurrentPlayerController().setMagic((Magic) card);
        view.resetSelector();
    }

    public void changeCardPosition(Card card, MonsterState monsterState) throws LogicException {
        if (!(card instanceof Monster))
            throw new LogicException("you can only change position of a monster card");
        gameController.getCurrentPlayerController().changeMonsterPosition((Monster) card, monsterState);
        view.resetSelector();
    }

    public void flipSummon(Card card) throws LogicException, ResistToChooseCard {
        if (!(card instanceof Monster))
            throw new LogicException("you can only flip summon a monster card");
        gameController.getCurrentPlayerController().flipSummon((Monster) card);
        view.resetSelector();
    }

    public void attack(Card card, CardAddress defenderAddress) throws LogicException, RoundOverExceptionEvent, ResistToChooseCard {
        if(!(card instanceof Monster))
            throw new LogicException("only a monster can attack");
        if(!(defenderAddress.isInMonsterZone()))
            throw new LogicException("you can only attack monsters!");
        Monster opponentMonster = (Monster) duel.getCurrentGame().getCardByCardAddress(defenderAddress);
        if (opponentMonster == null)
            throw new LogicException("there is no card to attack here");
        gameController.getCurrentPlayerController().attack((Monster) card, opponentMonster);
    }

    public void directAttack(Card card) throws LogicException, RoundOverExceptionEvent, ResistToChooseCard {
        if(!(card instanceof Monster))
            throw new LogicException("only a monster can attack");
        gameController.getCurrentPlayerController().directAttack((Monster) card);
    }

    public void activateEffect(Card card) throws LogicException, RoundOverExceptionEvent, ResistToChooseCard {
        if (!card.hasEffect())
            throw new LogicException("activate effect is only for spell cards and monsters that have effect");
        if (card instanceof Trap)
            throw new LogicException("trap's can't be activated");
        if (card instanceof Monster)
            gameController.getCurrentPlayerController().activateMonsterEffect((Monster) card);
        if (card instanceof Spell)
            gameController.getCurrentPlayerController().activateSpellEffect((Spell) card);
    }

    public void showGraveYard() {
        List<Card> graveYard = duel.getCurrentGame().getCurrentPlayer().getBoard().getGraveYard();
        if (graveYard.isEmpty())
            CustomPrinter.println("graveyard empty", Color.Default);
        for (int i = 0; i < graveYard.size(); i++)
            CustomPrinter.println((i + 1) + ". " + graveYard.get(i).toString(), Color.Default);
    }

    public void showBoard() {
        CustomPrinter.println(duel.getCurrentGame().getOpponentPlayer().getUser().getNickname() + ":" + duel.getCurrentGame().getOpponentPlayer().getLifePoint(), Color.Purple);
        CustomPrinter.println(duel.getCurrentGame().getOpponentPlayer().getBoard().toRotatedString(), Color.Purple);
        CustomPrinter.println();
        CustomPrinter.println("--------------------------", Color.Purple);
        CustomPrinter.println();
        CustomPrinter.println(duel.getCurrentGame().getCurrentPlayer().getBoard().toString(), Color.Purple);
        CustomPrinter.println(duel.getCurrentGame().getCurrentPlayer().getUser().getNickname() + ":" + duel.getCurrentGame().getCurrentPlayer().getLifePoint(), Color.Purple);
    }

    public void surrender() throws RoundOverExceptionEvent {
        gameController.getCurrentPlayerController().surrender();
    }

    public MainGameThread getNewGameThread() throws LogicException {
        if(duel.isFinished())
            throw new LogicException("duel has ended!");
        this.gameController = new GameController(duel.getCurrentGame());
        return new MainGameThread(()->{
            try {
                gameController.control();
            } catch (RoundOverExceptionEvent roundOverEvent) {
                GuiReporter.getInstance().report(new RoundOverEvent(roundOverEvent));
            }
        });
    }

    public void control() {
        while (!duel.isFinished()) {
            this.gameController = new GameController(duel.getCurrentGame());
            gameController.control();
        }
    }
}