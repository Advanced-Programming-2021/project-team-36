package YuGiOh.controller.menu;

import YuGiOh.controller.MainGameThread;
import YuGiOh.controller.events.RoundOverExceptionEvent;
import YuGiOh.controller.GameController;
import YuGiOh.controller.LogicException;
import YuGiOh.controller.ProgramController;
import YuGiOh.model.Player.Player;
import YuGiOh.view.cardSelector.ResistToChooseCard;
import YuGiOh.model.CardAddress;
import YuGiOh.model.Game;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Magic;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Spell;
import YuGiOh.model.enums.Color;
import YuGiOh.model.enums.ZoneType;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.utils.Debugger;
import YuGiOh.utils.RoutingException;
import YuGiOh.view.DuelMenuView;
import YuGiOh.view.gui.event.DuelOverEvent;
import YuGiOh.view.gui.event.RoundOverEvent;
import javafx.concurrent.Task;
import lombok.Getter;
import YuGiOh.model.enums.MonsterState;

import java.util.List;

public class DuelMenuController extends BaseMenuController {
    @Getter
    public static DuelMenuController instance;
    private final Game game;
    private final GameController gameController;

    // todo remove this and put in daddy
    @Getter
    private final DuelMenuView graphicView;

    public DuelMenuController(Game game){
        this.graphicView = new DuelMenuView(game);
        this.game = game;
        instance = this;
        gameController = new GameController(game);
    }

    public void addEventListeners(){
        graphicView.addEventListenerOnGameField(RoundOverEvent.MY_TYPE, e->{
            gameController.endRound(e.getExceptionEvent());
            graphicView.announce("round is over!");
        });
        graphicView.addEventListenerOnGameField(DuelOverEvent.MY_TYPE, e->{
            graphicView.announce("duel is over!");
        });
    }

    public void printCurrentPhase() {
        CustomPrinter.println("phase: " + game.getPhase().getVerboseName(), Color.Blue);
    }

    public void goNextPhase() {
        gameController.goNextPhaseAndNotify();
    }

    public void summonCard(Card card) throws LogicException, ResistToChooseCard {
        gameController.getCurrentPlayerController().normalSummon(card);
        graphicView.resetSelector();
    }

    public void setCard(Card card) throws LogicException, ResistToChooseCard {
        if (card instanceof Monster)
            gameController.getCurrentPlayerController().setMonster((Monster) card);
        else
            gameController.getCurrentPlayerController().setMagic((Magic) card);
        graphicView.resetSelector();
    }

    public void changeCardPosition(Card card, MonsterState monsterState) throws LogicException {
        if (!(card instanceof Monster))
            throw new LogicException("you can only change position of a monster card");
        gameController.getCurrentPlayerController().changeMonsterPosition((Monster) card, monsterState);
        graphicView.resetSelector();
    }

    public void flipSummon(Card card) throws LogicException, ResistToChooseCard {
        if (!(card instanceof Monster))
            throw new LogicException("you can only flip summon a monster card");
        gameController.getCurrentPlayerController().flipSummon((Monster) card);
        graphicView.resetSelector();
    }

    public void attack(Card card, CardAddress defenderAddress) throws LogicException, RoundOverExceptionEvent, ResistToChooseCard {
        if(!(card instanceof Monster))
            throw new LogicException("only a monster can attack");
        // todo is this okay?
        if(!(defenderAddress.isInMonsterZone()))
            throw new LogicException("you can only attack monsters!");
        Monster opponentMonster = (Monster) game.getCardByCardAddress(defenderAddress);
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
        if (!(card instanceof Spell))
            throw new LogicException("activate effect is only for spell cards");
        gameController.getCurrentPlayerController().activateEffect((Spell) card);
    }

    public void showGraveYard() {
        List<Card> graveYard = game.getCurrentPlayer().getBoard().getGraveYard();
        if (graveYard.isEmpty())
            CustomPrinter.println("graveyard empty", Color.Default);
        for (int i = 0; i < graveYard.size(); i++)
            CustomPrinter.println((i + 1) + ". " + graveYard.get(i).toString(), Color.Default);
    }

    public void showBoard() {
        CustomPrinter.println(game.getOpponentPlayer().getUser().getNickname() + ":" + game.getOpponentPlayer().getLifePoint(), Color.Purple);
        CustomPrinter.println(game.getOpponentPlayer().getBoard().toRotatedString(), Color.Purple);
        CustomPrinter.println();
        CustomPrinter.println("--------------------------", Color.Purple);
        CustomPrinter.println();
        CustomPrinter.println(game.getCurrentPlayer().getBoard().toString(), Color.Purple);
        CustomPrinter.println(game.getCurrentPlayer().getUser().getNickname() + ":" + game.getCurrentPlayer().getLifePoint(), Color.Purple);
    }

    public void endDuel(Player winner, Player looser, int rounds, int maxWinnerLP, int firstPlayerScore, int secondPlayerScore) {
        winner.getUser().increaseScore(rounds * 1000);
        winner.getUser().increaseBalance(rounds * (1000 + maxWinnerLP));
        looser.getUser().increaseBalance(rounds * 100);
        CustomPrinter.println(String.format("%s won the whole match with score: %d-%d", winner.getUser().getUsername(), firstPlayerScore, secondPlayerScore), Color.Blue);
        graphicView.fireEventOnGameField(new DuelOverEvent());
    }

    public void surrender() throws RoundOverExceptionEvent {
        gameController.getCurrentPlayerController().surrender();
    }

    @Override
    public void exitMenu() throws RoutingException {
        ProgramController.getInstance().navigateToMenu(MainMenuController.class);
    }

    @Override
    public BaseMenuController getNavigatingMenuObject(Class<? extends BaseMenuController> menu) throws RoutingException {
        // you will not be able to exit until end of the game
        if(menu.equals(MainMenuController.class))
            return MainMenuController.getInstance();
        if(!Debugger.getMode())
            throw new RoutingException("you cannot navigate out of an ongoing game");
        throw new RoutingException("menu navigation is not possible");
    }

    @Override
    public void control(){
        MainGameThread mainGameThread = new MainGameThread(()-> {
            addEventListeners();
            gameController.control();
        });
        mainGameThread.start();
        //        ProgramController.getInstance().navigateToMenu(MainMenuController.getInstance());
    }
}
