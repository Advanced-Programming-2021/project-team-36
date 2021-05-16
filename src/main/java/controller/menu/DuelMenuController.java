package controller.menu;

import controller.cardSelector.CardSelector;
import controller.GameController;
import controller.LogicException;
import controller.ProgramController;
import controller.events.GameOver;
import controller.player.PlayerController;
import lombok.Getter;
import model.CardAddress;
import model.Game;
import model.card.*;
import model.enums.Phase;
import model.enums.MonsterState;
import model.enums.ZoneType;
import view.*;
import utils.RoutingException;
import utils.CustomPrinter;
import java.util.List;
import utils.Debugger;

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
        CustomPrinter.println("phase: " + game.getPhase().verboseName);
    }

    public void goNextPhase() {
        gameController.goNextPhase();
    }

    public void canSummonOrSetMonster(Card card) throws LogicException {
        if (!game.getPhase().equals(Phase.MAIN_PHASE1) && !game.getPhase().equals(Phase.MAIN_PHASE2))
            throw new LogicException("action not allowed in this phase");
        if (!game.canCardBeSummoned((Monster) card))
            throw new LogicException("you can't summon this card");
        if (game.getCurrentPlayer().getBoard().isMonsterCardZoneFull())
            throw new LogicException("monster card zone is full");
        if (game.isSummonedInThisTurn())
            throw new LogicException("you already summoned/set on this turn");
    }

    public void summonCard(Card card) throws LogicException {
        canSummonOrSetMonster(card);
        gameController.getCurrentPlayerController().summonCard((Monster) card);
        new CardSelector(game);
    }

    public void setCard(Card card) throws LogicException {
        if (card instanceof Monster) {
            canSummonOrSetMonster(card);
            gameController.getCurrentPlayerController().setMonster((Monster) card);
        }
        else
            gameController.getCurrentPlayerController().setMagic((Magic) card);
        new CardSelector(game);
    }

    public void changeCardPosition(Card card, MonsterState monsterState) throws LogicException {
        if (!GameController.getInstance().getCurrentPlayerController().getPlayer().getBoard().getMonsterCardZone().containsValue((Monster) card))
            throw new LogicException("you can't change this card position");
        if (!game.getPhase().equals(Phase.MAIN_PHASE1) && !game.getPhase().equals(Phase.MAIN_PHASE2))
            throw new LogicException("you can’t do this action in this phase");
        Monster monster = (Monster) card;
        if (monster.getMonsterState().equals(MonsterState.DEFENSIVE_HIDDEN) || monster.getMonsterState().equals(monsterState))
            throw new LogicException("this card is already in the wanted position (maybe it's defensive hidden)");
        gameController.getCurrentPlayerController().changeMonsterPosition(monster, monsterState);
        new CardSelector(game);
    }

    public void flipSummon(Card card) throws LogicException {
        if (!GameController.getInstance().getCurrentPlayerController().getPlayer().getBoard().getMonsterCardZone().containsValue((Monster) card))
            throw new LogicException("you can't change this card position");
        if (!game.getPhase().equals(Phase.MAIN_PHASE1) && !game.getPhase().equals(Phase.MAIN_PHASE2))
            throw new LogicException("you can’t do this action in this phase");
        Monster monster = (Monster) card;
        if (!monster.getMonsterState().equals(MonsterState.DEFENSIVE_HIDDEN) || game.isSummonedInThisTurn())
            throw new LogicException("you can't flip summon this card");
        gameController.getCurrentPlayerController().flipSummon(card);
        new CardSelector(game);
    }

    private void ritualSummon(Card card) throws LogicException {
        if (!game.getPhase().equals(Phase.MAIN_PHASE1) && !game.getPhase().equals(Phase.MAIN_PHASE2))
            throw new LogicException("you can’t do this action in this phase");
        gameController.getCurrentPlayerController().ritualSummon(card);
        new CardSelector(game);
    }

    public void canAttack(Card card) throws LogicException {
        PlayerController playerController = gameController.getCurrentPlayerController();
        if (!playerController.getPlayer().getBoard().getMonsterCardZone().containsValue((Monster) card))
            throw new LogicException("you can’t attack with this card");
        if (!game.getPhase().equals(Phase.BATTLE_PHASE))
            throw new LogicException("you can’t do this action in this phase");
        if (playerController.hasAttackedByCard((Monster) card))
            throw new LogicException("this card already attacked");
    }

    public void attack(Card card, int id) throws LogicException, GameOver {
        canAttack(card);
        CardAddress cardAddress = new CardAddress(ZoneType.MONSTER, id, true);
        Monster opponentMonster = (Monster) game.getCardByCardAddress(cardAddress);
        if (opponentMonster == null)
            throw new LogicException("there is no card to attack here");
        gameController.getCurrentPlayerController().attack((Monster) card, opponentMonster);
    }

    public void directAttack(Card card) throws LogicException, GameOver {
        canAttack(card);
        PlayerController playerController = gameController.getCurrentPlayerController();
        if (gameController.getOtherPlayerController(playerController).getPlayer().getBoard().getMonsterCardZone().size() != 0)
            throw new LogicException("you can’t attack the opponent directly");
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
