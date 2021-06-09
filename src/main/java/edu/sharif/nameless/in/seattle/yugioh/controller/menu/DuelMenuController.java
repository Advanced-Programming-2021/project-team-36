package edu.sharif.nameless.in.seattle.yugioh.controller.menu;

import edu.sharif.nameless.in.seattle.yugioh.controller.GameController;
import edu.sharif.nameless.in.seattle.yugioh.controller.LogicException;
import edu.sharif.nameless.in.seattle.yugioh.controller.ProgramController;
import edu.sharif.nameless.in.seattle.yugioh.view.cardSelector.ResistToChooseCard;
import edu.sharif.nameless.in.seattle.yugioh.controller.events.RoundOverEvent;
import edu.sharif.nameless.in.seattle.yugioh.model.CardAddress;
import edu.sharif.nameless.in.seattle.yugioh.model.Game;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Card;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Magic;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Monster;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Spell;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.Color;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.ZoneType;
import edu.sharif.nameless.in.seattle.yugioh.utils.CustomPrinter;
import edu.sharif.nameless.in.seattle.yugioh.utils.Debugger;
import edu.sharif.nameless.in.seattle.yugioh.utils.RoutingException;
import edu.sharif.nameless.in.seattle.yugioh.view.DuelMenuView;
import lombok.Getter;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.MonsterState;

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

    // todo remove this and put in daddy
    @Getter
    private final DuelMenuView graphicView;

    public DuelMenuController(Game game){
        this.graphicView = new DuelMenuView(game);
        this.game = game;
        instance = this;
        gameController = new GameController(game);
    }

    public void printCurrentPhase() {
        CustomPrinter.println("phase: " + game.getPhase().getVerboseName(), Color.Blue);
    }

    public void goNextPhase() {
        gameController.goNextPhaseAndNotify();
    }

    public void summonCard(Card card) throws LogicException, ResistToChooseCard {
        gameController.getCurrentPlayerController().normalSummon((Monster) card);
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

    public void flipSummon(Card card) throws LogicException {
        if (!(card instanceof Monster))
            throw new LogicException("you can only flip summon a monster card");
        gameController.getCurrentPlayerController().flipSummon((Monster) card);
        graphicView.resetSelector();
    }

    public void attack(Card card, int id) throws LogicException, RoundOverEvent {
        if(!(card instanceof Monster))
            throw new LogicException("only a monster can attack");
        CardAddress cardAddress = new CardAddress(ZoneType.MONSTER, id, gameController.getGame().getOpponentPlayer());
        Monster opponentMonster = (Monster) game.getCardByCardAddress(cardAddress);
        if (opponentMonster == null)
            throw new LogicException("there is no card to attack here");
        gameController.getCurrentPlayerController().attack((Monster) card, opponentMonster);
    }

    public void directAttack(Card card) throws LogicException, RoundOverEvent {
        if(!(card instanceof Monster))
            throw new LogicException("only a monster can attack");
        gameController.getCurrentPlayerController().directAttack((Monster) card);
    }

    public void activateEffect(Card card) throws LogicException, RoundOverEvent {
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

    public void surrender() throws RoundOverEvent {
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
//        ProgramController.getInstance().navigateToMenu(MainMenuController.getInstance());
    }
}
