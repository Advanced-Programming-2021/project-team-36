package YuGiOh.controller.menu;

import YuGiOh.controller.*;
import YuGiOh.controller.events.PlayerReadyExceptionEvent;
import YuGiOh.controller.events.RoundOverExceptionEvent;
import YuGiOh.controller.player.HumanPlayerController;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.Duel;
import YuGiOh.model.ModelException;
import YuGiOh.model.Player.HumanPlayer;
import YuGiOh.model.Player.Player;
import YuGiOh.model.card.*;
import YuGiOh.view.cardSelector.ResistToChooseCard;
import YuGiOh.model.CardAddress;
import YuGiOh.model.Game;
import YuGiOh.model.enums.Color;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.utils.Debugger;
import YuGiOh.utils.RoutingException;
import YuGiOh.view.DuelMenuView;
import lombok.Getter;
import YuGiOh.model.enums.MonsterState;

import java.util.List;

public class DuelMenuController extends BaseMenuController {
    @Getter
    public static DuelMenuController instance;
    private final Duel duel;
    private Game game;
    private GameController gameController;

    public DuelMenuController(Duel duel){
        this.duel = duel;
        this.view = new DuelMenuView();
        instance = this;
    }

    public DuelMenuView getView(){
        return (DuelMenuView) view;
    }

    public void printCurrentPhase() {
        CustomPrinter.println("phase: " + game.getPhase().getVerboseName(), Color.Cyan);
    }

    public void goNextPhase() {
        gameController.goNextPhase();
    }

    public void summonCard(Card card) throws LogicException, ResistToChooseCard {
        if (card instanceof Magic)
            throw new LogicException("this card is magic and you can't summon it");
        gameController.getCurrentPlayerController().normalSummon((Monster) card);
        getView().resetSelector();
    }

    public void specialSummon(Card card) throws LogicException, ResistToChooseCard {
        if (card instanceof Magic)
            throw new LogicException("this card is magic and you can't special summon it");
        gameController.getCurrentPlayerController().specialSummon((Monster) card);
        getView().resetSelector();
    }

    public void setCard(Card card) throws LogicException, ResistToChooseCard {
        if (card instanceof Monster)
            gameController.getCurrentPlayerController().setMonster((Monster) card);
        else
            gameController.getCurrentPlayerController().setMagic((Magic) card);
        getView().resetSelector();
    }

    public void changeCardPosition(Card card, MonsterState monsterState) throws LogicException {
        if (!(card instanceof Monster))
            throw new LogicException("you can only change position of a monster card");
        gameController.getCurrentPlayerController().changeMonsterPosition((Monster) card, monsterState);
        getView().resetSelector();
    }

    public void flipSummon(Card card) throws LogicException, ResistToChooseCard {
        if (!(card instanceof Monster))
            throw new LogicException("you can only flip summon a monster card");
        gameController.getCurrentPlayerController().flipSummon((Monster) card);
        getView().resetSelector();
    }

    public void attack(Card card, CardAddress defenderAddress) throws LogicException, RoundOverExceptionEvent, ResistToChooseCard {
        if(!(card instanceof Monster))
            throw new LogicException("only a monster can attack");
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
        List<Card> graveYard = game.getCurrentPlayer().getBoard().getGraveYard();
        if (graveYard.isEmpty())
            CustomPrinter.println("graveyard empty", Color.Default);
        for (int i = 0; i < graveYard.size(); i++)
            CustomPrinter.println((i + 1) + ". " + graveYard.get(i).toString(), Color.Default);
    }

    public void showHand() {
        List<Card> cards = game.getCurrentPlayer().getBoard().getCardsOnHand();
        for (int i = 0; i < cards.size(); i++)
            CustomPrinter.println(String.format("%d. %s%n", i + 1, cards.get(i).toString()), Color.Purple);
    }

    public void showSelectedCard() throws LogicException {
        if (!getView().getCardSelector().getSelectedCard().owner.equals(game.getCurrentPlayer())) {
            Card card = getView().getCardSelector().getSelectedCard();
            if (!card.isFacedUp())
                throw new LogicException("you can't see your opponent face down cards");
        }
        getView().getCardSelector().showSelectedCard();
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

    public void surrender(Player player) throws RoundOverExceptionEvent {
        gameController.getPlayerControllerByPlayer(player).surrender();
    }

    @Override
    public void exitMenu() throws RoutingException {
        ProgramController.getInstance().navigateToMenu(MainMenuController.class);
    }

    @Override
    public BaseMenuController getNavigatingMenuObject(Class<? extends BaseMenuController> menu) throws RoutingException {
        if (menu.equals(MainMenuController.class))
            return MainMenuController.getInstance();
        if (!Debugger.getMode())
            throw new RoutingException("you cannot navigate out of an ongoing game");
        throw new RoutingException("menu navigation is not possible");
    }

    public void increaseLP(String valueAsString) {
        try {
            int value = Integer.parseInt(valueAsString);
            game.getCurrentPlayer().increaseLifePoint(value);
        } catch (Exception exception) {
            System.out.println("lp should be an integer");
        }
    }

    public void ultimateCheat() throws RoundOverExceptionEvent {
        surrender(game.getOpponentPlayer());
    }

    @Override
    public void control(){
        while (!duel.isFinished()){
            this.game = duel.getCurrentGame();
            ((DuelMenuView) view).startNewGame(game);
            this.gameController = new GameController(game);
            try {
                gameController.control();
            } catch (RoundOverExceptionEvent roundOverEvent) {
                try {
                    duel.goNextRound(roundOverEvent);
                } catch (ModelException e) {
                }
                if (!duel.isFinished()) {
                    PlayerController firstPlayerController = GameController.getInstance().getPlayerControllerByPlayer(duel.getFirstPlayer());
                    if (firstPlayerController instanceof HumanPlayerController) {
                        HalfTimeMenuController halfTimeMenuController = new HalfTimeMenuController(firstPlayerController);
                        try {
                            halfTimeMenuController.control();
                        } catch (PlayerReadyExceptionEvent ignored) {
                        }
                    }
                    PlayerController secondPlayerController = GameController.getInstance().getPlayerControllerByPlayer(duel.getSecondPlayer());
                    if (secondPlayerController instanceof HumanPlayerController) {
                        HalfTimeMenuController halfTimeMenuController = new HalfTimeMenuController(firstPlayerController);
                        try {
                            halfTimeMenuController.control();
                        } catch (PlayerReadyExceptionEvent ignored) {
                        }
                    }
                }
            }
        }
        ProgramController.getInstance().navigateToMenu(MainMenuController.getInstance());
    }
}
