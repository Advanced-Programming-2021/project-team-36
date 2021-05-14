package controller.menu;

import controller.GameController;
import controller.LogicException;
import controller.ProgramController;
import controller.events.GameOver;
import lombok.Getter;
import model.CardAddress;
import model.Game;
import model.card.*;
import model.enums.Phase;
import model.enums.MonsterState;
import model.enums.ZoneType;
import view.*;
import Utils.RoutingException;
import Utils.CustomPrinter;
import java.util.List;

// this class is responsible for passing everything to GameController.
// in the middle of the way it can handle simple api like those involving printing for user
// MenuControllers are only used to answer user queries
// We filter all bad inputs here
// We can pass them to player right now

public class DuelMenuController extends BaseMenuController {
    @Getter
    public static DuelMenuController instance;
    private final Game game;
    private final GameController gameController;

    public DuelMenuController(Game game){
        this.view = new DuelMenuView();
        this.game = game;
        instance = this;
        gameController = new GameController(game);
    }

    public void printCurrentPhase() {
        CustomPrinter.println("phase: " + game.getPhase().verboseName);
    }

    public void goNextPhase() {
        gameController.goNextPhase();
    }

    public void summonCard(Card card) throws LogicException {
        // todo is summon only for monsters?
        if (!game.getPhase().equals(Phase.MAIN_PHASE1) || !game.getPhase().equals(Phase.MAIN_PHASE2))
            throw new LogicException("you can’t do this action in this phase");
        if (!(card instanceof Monster))
            throw new LogicException("summoning is for monsters!");
        gameController.getCurrentPlayerController().summonCard((Monster) card);
    }

    public void setCard(Card card) throws LogicException {
        if (!game.getPhase().equals(Phase.MAIN_PHASE1) || !game.getPhase().equals(Phase.MAIN_PHASE2))
            throw new LogicException("you can’t do this action in this phase");

        gameController.getCurrentPlayerController().setCard(card);
    }

    public void changeCardPosition(Card card, MonsterState monsterState) throws LogicException {
        if (!game.getPhase().equals(Phase.MAIN_PHASE1) || !game.getPhase().equals(Phase.MAIN_PHASE2))
            throw new LogicException("you can’t do this action in this phase");

        gameController.getCurrentPlayerController().changeCardPosition(card, monsterState);
    }

    public void flipSummon(Card card) throws LogicException {
        // todo is this conditions correct?
        if (!game.getPhase().equals(Phase.MAIN_PHASE1) || !game.getPhase().equals(Phase.MAIN_PHASE2))
            throw new LogicException("you can’t do this action in this phase");

        gameController.getCurrentPlayerController().flipSummon(card);
    }

    private void ritualSummon(Card card) throws LogicException {
        if (!game.getPhase().equals(Phase.MAIN_PHASE1) || !game.getPhase().equals(Phase.MAIN_PHASE2))
            throw new LogicException("you can’t do this action in this phase");

        gameController.getCurrentPlayerController().ritualSummon(card);
    }

    public void attack(Card myCard, int id) throws LogicException, GameOver {
        if (!game.getPhase().equals(Phase.BATTLE_PHASE))
            throw new LogicException("you can’t do this action in this phase");
        if (myCard instanceof Magic)
            throw new LogicException("you can’t attack with this card");
        CardAddress cardAddress = new CardAddress(ZoneType.MONSTER, id, true);
        Monster opponentMonster = (Monster) game.getCardByCardAddress(cardAddress);
        if (opponentMonster == null)
            throw new LogicException("there is no card to attack here");

        gameController.getCurrentPlayerController().attack((Monster) myCard, opponentMonster);
    }

    public void directAttack(Card card) {
        // todo age harif chizi baraye defaa dasht error bedim
        gameController.getCurrentPlayerController().directAttack((Monster) card);
    }

    public void activateEffect(Card card) {
        // todo age selected Magic nabood error bedim
        // todo momkene az in monster khafana bashe?
        gameController.getCurrentPlayerController().activateEffect((Magic) card);
    }

    public void showGraveYard() {
        List<Card> graveYard = game.getCurrentPlayer().getBoard().getGraveYard();
        if (graveYard.isEmpty())
            CustomPrinter.println("graveyard empty");
        for (int i = 0; i < graveYard.size(); i++)
            CustomPrinter.println((i + 1) + ". " + graveYard.get(i).toString());
    }

    public void showBoard() {
        CustomPrinter.println(game.getOpponentPlayer().getUser().getNickname() + ":" + game.getOpponentPlayer().getLifePoint());
        CustomPrinter.println(game.getOpponentPlayer().getBoard().toString()); // TODO it should rotate 180 degree
        CustomPrinter.println();
        CustomPrinter.println("--------------------------");
        CustomPrinter.println();
        CustomPrinter.println(game.getCurrentPlayer().getBoard().toString());
        CustomPrinter.println(game.getCurrentPlayer().getUser().getNickname() + ":" + game.getCurrentPlayer().getLifePoint());
    }

    public void showHand() {
        List<Card> cards = game.getCurrentPlayer().getBoard().getCardsOnHand();
        for (int i = 0; i < cards.size(); i++)
            CustomPrinter.println(String.format("%d. %s%n", i + 1, cards.get(i).toString()));
    }

    private void surrender(){
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
        gameController.control();
        ProgramController.getInstance().navigateToMenu(MainMenuController.getInstance());
    }
}
