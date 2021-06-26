package YuGiOh.controller.menu;

import YuGiOh.controller.cardSelector.CardSelector;
import YuGiOh.controller.GameController;
import YuGiOh.controller.LogicException;
import YuGiOh.controller.ProgramController;
import YuGiOh.controller.cardSelector.ResistToChooseCard;
import YuGiOh.controller.events.GameOverEvent;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Magic;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Spell;
import YuGiOh.view.DuelMenuView;
import lombok.Getter;
import YuGiOh.model.CardAddress;
import YuGiOh.model.Game;
import model.card.*;
import YuGiOh.model.enums.Color;
import YuGiOh.model.enums.MonsterState;
import YuGiOh.model.enums.ZoneType;
import view.*;
import YuGiOh.utils.RoutingException;
import YuGiOh.utils.CustomPrinter;
import java.util.List;
import YuGiOh.utils.Debugger;

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
        new CardSelector(game);
    }

    public void printCurrentPhase() {
        CustomPrinter.println("phase: " + game.getPhase().verboseName, Color.Blue);
    }

    public void goNextPhase() {
        gameController.goNextPhase();
    }

    public void summonCard(Card card) throws LogicException, ResistToChooseCard {
        gameController.getCurrentPlayerController().normalSummon((Monster) card);
        new CardSelector(game);
    }

    public void setCard(Card card) throws LogicException, ResistToChooseCard {
        if (card instanceof Monster)
            gameController.getCurrentPlayerController().setMonster((Monster) card);
        else
            gameController.getCurrentPlayerController().setMagic((Magic) card);
        new CardSelector(game);
    }

    public void changeCardPosition(Card card, MonsterState monsterState) throws LogicException {
        if (!(card instanceof Monster))
            throw new LogicException("you can only change position of a monster card");
        gameController.getCurrentPlayerController().changeMonsterPosition((Monster) card, monsterState);
        new CardSelector(game);
    }

    public void flipSummon(Card card) throws LogicException {
        if (!(card instanceof Monster))
            throw new LogicException("you can only flip summon a monster card");
        gameController.getCurrentPlayerController().flipSummon((Monster) card);
        new CardSelector(game);
    }

    public void attack(Card card, int id) throws LogicException, GameOverEvent {
        if(!(card instanceof Monster))
            throw new LogicException("only a monster can attack");
        CardAddress cardAddress = new CardAddress(ZoneType.MONSTER, id, true);
        Monster opponentMonster = (Monster) game.getCardByCardAddress(cardAddress);
        if (opponentMonster == null)
            throw new LogicException("there is no card to attack here");
        gameController.getCurrentPlayerController().attack((Monster) card, opponentMonster);
    }

    public void directAttack(Card card) throws LogicException, GameOverEvent {
        if(!(card instanceof Monster))
            throw new LogicException("only a monster can attack");
        gameController.getCurrentPlayerController().directAttack((Monster) card);
    }

    public void activateEffect(Card card) throws LogicException, GameOverEvent {
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

    public void showHand() {
        List<Card> cards = game.getCurrentPlayer().getBoard().getCardsOnHand();
        for (int i = 0; i < cards.size(); i++)
            CustomPrinter.println(String.format("%d. %s%n", i + 1, cards.get(i).toString()), Color.Purple);
    }

    public void showSelectedCard() throws LogicException {
        CardAddress cardAddress = CardSelector.getInstance().getSelectedCardAddress();
        if (cardAddress.isOpponentAddress()) {
            Card card = CardSelector.getInstance().getSelectedCard();
            if (!card.isFacedUp())
                throw new LogicException("you can't see your opponent face down cards");
        }
        CardSelector.getInstance().showSelectedCard();
    }

    public void surrender() throws GameOverEvent {
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
