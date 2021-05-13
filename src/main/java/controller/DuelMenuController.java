package controller;

import lombok.Getter;
import model.Board;
import model.CardAddress;
import model.Game;
import model.Player.AIPlayer;
import model.Player.Player;
import model.card.*;
import model.enums.Phase;
import model.enums.MonsterState;
import model.enums.ZoneType;
import view.*;
import Utils.RoutingException;
import java.util.List;

public class DuelMenuController extends BaseMenuController {
    @Getter
    public static DuelMenuController instance;
    private final Game game;

    public DuelMenuController(Game game){
        this.view = new DuelMenuView();
        this.game = game;
        instance = this;
    }

    public void selectCard(CardAddress cardAddress) throws LogicException {
        if (game.getCardByCardAddress(cardAddress) == null)
            throw new LogicException("no card found in the given position");
        game.selectCard(cardAddress);
        System.out.println("card selected");
    }

    public void deselectCard() throws LogicException {
        if (game.getSelectedCardAddress() == null)
            throw new LogicException("no card is selected");
        game.unselectCard();
        System.out.println("card deselected");
    }

    public Card getSelectedCard() throws LogicException {
        if (!game.isAnyCardSelected())
            throw new LogicException("no card is selected");
        return game.getCardByCardAddress(game.getSelectedCardAddress());
    }

    public void printCurrentPhase() {
        if (game.getPhase() == Phase.DRAW_PHASE)
            System.out.println("phase: draw phase");
        else if (game.getPhase() == Phase.STANDBY_PHASE)
            System.out.println("phase: standby phase");
        else if (game.getPhase() == Phase.MAIN_PHASE1)
            System.out.println("phase: main phase 1");
        else if (game.getPhase() == Phase.BATTLE_PHASE)
            System.out.println("phase: battle phase");
        else if (game.getPhase() == Phase.MAIN_PHASE2)
            System.out.println("phase: main phase 2");
        else
            System.out.println("phase: end phase");
    }

    public void goNextPhase() throws LogicException {
        if (game.getPhase() == Phase.DRAW_PHASE)
            game.setPhase(Phase.STANDBY_PHASE);
        else if (game.getPhase() == Phase.STANDBY_PHASE)
            game.setPhase(Phase.MAIN_PHASE1);
        else if (game.getPhase() == Phase.MAIN_PHASE1)
            game.setPhase(Phase.BATTLE_PHASE);
        else if (game.getPhase() == Phase.BATTLE_PHASE)
            game.setPhase(Phase.MAIN_PHASE2);
        else if (game.getPhase() == Phase.MAIN_PHASE2)
            game.setPhase(Phase.END_PHASE);
        else
            game.setPhase(Phase.DRAW_PHASE);

        if (game.isFirstTurn() && game.getPhase().equals(Phase.BATTLE_PHASE))
            game.setPhase(Phase.MAIN_PHASE2);

        printCurrentPhase();
        if (game.getPhase() == Phase.END_PHASE) {
            changeTurn();
            game.setPhase(Phase.DRAW_PHASE);
            printCurrentPhase();
            drawCard();
        }
    }

    public void drawCard() throws LogicException {
        Card card = game.getCurrentPlayer().getMainDeck().getTopCard();
        if (card == null) {
            endGame(game.getOpponentPlayer(), game.getCurrentPlayer());
            return;
        }
        game.getCurrentPlayer().getBoard().drawCardFromDeck();
        System.out.println(String.format("new card added to the hand : %s", card.getName()));
    }

    public void summonCard() throws LogicException {
        if (!game.isAnyCardSelected())
            throw new LogicException("no card is selected yet");

        CardAddress cardAddress = game.getSelectedCardAddress();
        if (!cardAddress.isInHand() || getSelectedCard() instanceof Magic)
            throw new LogicException("you can't summon this card");

        Monster monster = (Monster) getSelectedCard();

        if (!game.getPhase().equals(Phase.MAIN_PHASE1) && !game.getPhase().equals(Phase.MAIN_PHASE2))
            throw new LogicException("action not allowed in this phase");
        if (game.getCurrentPlayer().getBoard().isMonsterCardZoneFull())
            throw new LogicException("monster card zone is full");
        if (game.isSummonedInThisTurn())
            throw new LogicException("you already summoned/set on this turn");
        // TODO : monster with higher level than 4
        game.setSummonedInThisTurn(true);
        System.out.println("summoned successfully");
        Board board = game.getCurrentPlayer().getBoard();
        for (int i = 1; i <= 5; i++) {
            if (board.getMonsterCardZone().get(i) == null) {
                board.addCardToBoard(monster, new CardAddress(ZoneType.HAND, i, false));
                board.getCardsOnHand().remove(monster);
                monster.setMonsterState(MonsterState.OFFENSIVE_OCCUPIED);
                game.unselectCard();
                break;
            }
        }
    }

    public void setCard(Card card) {
    }

    public void changeCardPosition(Card card, MonsterState monsterState) {
    }

    public void flipSummon(Card card) {
    }

    private void ritualSummon(Card card) {
    }

    public void attack(int id) throws LogicException {
        Card card = getSelectedCard();

        if (card instanceof Magic)
            throw new LogicException("you can’t attack with this card");
        if (!game.getPhase().equals(Phase.BATTLE_PHASE))
            throw new LogicException("you can’t do this action in this phase");
        // TODO : check one card don't attack twice in a turn
        // error should be : this card already attacked
        CardAddress cardAddress = new CardAddress(ZoneType.MONSTER, id, true);
        Card opponentCard = game.getCardByCardAddress(cardAddress);
        if (opponentCard == null)
            throw new LogicException("there is no card to attack here");

        damageStep((Monster) card, (Monster) opponentCard);
        if (game.getCurrentPlayer().getLifePoint() == 0)
            endGame(game.getOpponentPlayer(), game.getCurrentPlayer());
        if (game.getOpponentPlayer().getLifePoint() == 0)
            endGame(game.getCurrentPlayer(), game.getOpponentPlayer());
    }

    public void damageStep(Monster attacker, Monster defender) {
        if (defender.getState().equals(MonsterState.OFFENSIVE_OCCUPIED)) {
            if (attacker.getAttackDamage() > defender.getAttackDamage()) {
                int difference = attacker.getAttackDamage() - defender.getAttackDamage();
                System.out.println(String.format("your opponent’s monster is destroyed and your opponent receives %d battle damage", difference));
                game.getOpponentPlayer().getBoard().moveCardToGraveYard(defender);
                game.getOpponentPlayer().decreaseLifePoint(difference);
            }
            else if (attacker.getAttackDamage() == defender.getAttackDamage()) {
                System.out.println("both you and your opponent monster cards are destroyed and no one receives damage");
                game.getCurrentPlayer().getBoard().moveCardToGraveYard(attacker);
                game.getOpponentPlayer().getBoard().moveCardToGraveYard(defender);
            }
            else {
                int difference = defender.getAttackDamage() - attacker.getAttackDamage();
                System.out.println(String.format("Your monster card is destroyed and you received %s battle damage", difference));
                game.getCurrentPlayer().getBoard().moveCardToGraveYard(attacker);
                game.getCurrentPlayer().decreaseLifePoint(difference);
            }
        }
    }

    public void directAttack(Card card) {
    }

    public void activateEffect(Card card) {
    }

    public void showGraveYard() {
        List<Card> graveYard = game.getCurrentPlayer().getBoard().getGraveYard();
        if (graveYard.isEmpty())
            System.out.println("graveyard empty");
        for (int i = 0; i < graveYard.size(); i++)
            System.out.println((i + 1) + ". " + graveYard.get(i).toString());
    }

    public void showBoard() {
        System.out.println(game.getOpponentPlayer().getUser().getNickname() + ":" + game.getOpponentPlayer().getLifePoint());
        System.out.println(game.getOpponentPlayer().getBoard().toString()); // TODO it should rotate 180 degree
        System.out.println();
        System.out.println("--------------------------");
        System.out.println();
        System.out.println(game.getCurrentPlayer().getBoard().toString());
        System.out.println(game.getCurrentPlayer().getUser().getNickname() + ":" + game.getCurrentPlayer().getLifePoint());
    }

    public void showSelectedCard() throws LogicException {
        Card card = getSelectedCard();
        System.out.println(card.toString());
    }

    public void showHand() {
        List<Card> cards = game.getCurrentPlayer().getBoard().getCardsOnHand();
        for (int i = 0; i < cards.size(); i++)
            System.out.println(String.format("%d. %s", i + 1, cards.get(i).toString()));
    }

    private void surrender(){

    }

    private void changeTurn() {
        game.changeTurn();
        System.out.println(String.format("its %s's turn", game.getCurrentPlayer().getUser().getNickname()));
        game.setSummonedInThisTurn(false);
        game.unselectCard();
    }

    private void endGame(Player winner, Player loser) {
        winner.getUser().increaseScore(1000);
        winner.getUser().increaseBalance(1000 + winner.getLifePoint());
        loser.getUser().increaseBalance(100);
    }

    @Override
    public void exitMenu() throws RoutingException {
        ProgramController.getInstance().navigateToMenu(MainMenuController.class);
    }

    @Override
    public BaseMenuController getNavigatingMenuObject(Class<? extends BaseMenuController> menu) throws RoutingException {
        // you will not be able to exit until end of the game todo
        if (Debugger.getMode())
            throw new RoutingException("you cannot navigate out of an ongoing game");
        throw new RoutingException("menu navigation is not possible");
    }

    @Override
    public void control(){
        if(game.getCurrentPlayer() instanceof AIPlayer) {
            try {
                ((AIPlayer) game.getCurrentPlayer()).play(game);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        else {
            this.view.runNextCommand();
        }
    }
}
