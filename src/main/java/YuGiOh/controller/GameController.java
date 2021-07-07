package YuGiOh.controller;

import YuGiOh.controller.events.RoundOverExceptionEvent;
import YuGiOh.controller.menus.DuelMenuController;
import YuGiOh.controller.player.HumanPlayerController;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.Player.Player;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Magic;
import YuGiOh.model.card.Monster;
import YuGiOh.model.enums.*;
import YuGiOh.utils.CustomPrinter;
import lombok.Getter;
import YuGiOh.model.Game;
import YuGiOh.model.Player.AIPlayer;
import YuGiOh.model.Player.HumanPlayer;

public class GameController {
    @Getter
    public static GameController instance;
    @Getter
    private final Game game;

    private final PlayerController playerController1, playerController2;

    private Phase previousIterationPhase;

    public GameController(Game game) {
        this.game = game;
        instance = this;

        this.playerController1 = createPlayerControllerByPlayer(game.getFirstPlayer());
        this.playerController2 = createPlayerControllerByPlayer(game.getSecondPlayer());
    }

    private PlayerController createPlayerControllerByPlayer(Player player) {
        if(player instanceof HumanPlayer) {
            HumanPlayer humanPlayer = (HumanPlayer) player;
            return new HumanPlayerController(humanPlayer);
        } else {
            AIPlayer aiPlayer = (AIPlayer) player;
            return aiPlayer.getAiMode().getNewInstance(aiPlayer);
        }
    }

    public void drawCard() throws RoundOverExceptionEvent {
        try {
            getCurrentPlayerController().drawCard();
        }
        catch (LogicException ignored) {
            throw new RoundOverExceptionEvent(GameResult.NOT_DRAW, game.getCurrentPlayer(), game.getOpponentPlayer(), game.getOpponentPlayer().getLifePoint());
        }
    }

    public void checkBothLivesEndGame() throws RoundOverExceptionEvent {
        if (game.getCurrentPlayer().getLifePoint() <= 0 && game.getOpponentPlayer().getLifePoint() <= 0)
            throw new RoundOverExceptionEvent(GameResult.DRAW, game.getCurrentPlayer(), game.getOpponentPlayer(), 0);
        if (game.getCurrentPlayer().getLifePoint() <= 0)
            throw new RoundOverExceptionEvent(GameResult.NOT_DRAW, game.getCurrentPlayer(), game.getOpponentPlayer(), game.getOpponentPlayer().getLifePoint());
        if (game.getOpponentPlayer().getLifePoint() <= 0)
            throw new RoundOverExceptionEvent(GameResult.NOT_DRAW, game.getOpponentPlayer(), game.getCurrentPlayer(), game.getCurrentPlayer().getLifePoint());
    }

    public void increaseLifePoint(Player player, int amount) {
        player.increaseLifePoint(amount);
        CustomPrinter.println(String.format("<%s>'s life point increased by <%d> and it is <%d> now", player.getUser().getUsername(), amount, player.getLifePoint()), Color.Yellow);
    }

    public void decreaseLifePoint(Player player, int amount, boolean checkEndGame) {
        player.decreaseLifePoint(amount);
        CustomPrinter.println(String.format("<%s>'s life point decreased by <%d> and it is <%d> now", player.getUser().getUsername(), amount, player.getLifePoint()), Color.Yellow);
        if (checkEndGame)
            checkBothLivesEndGame();
    }

    public void moveCardToGraveYard(Card card) {
        Player player = card.getOwner();
        player.moveCardToGraveYard(card);
    }

    public void addCardToBoard(Card card) {
        Player player = card.getOwner();
        if (card instanceof Monster) {
            player.getBoard().moveCardNoError(card, ZoneType.MONSTER);
        } else if(card instanceof Magic) {
            if((((Magic) card).getIcon().equals(Icon.FIELD)))
                player.getBoard().moveCardNoError(card, ZoneType.FIELD);
            else
                player.getBoard().moveCardNoError(card, ZoneType.MAGIC);
        }
    }

    public void setSummoned(Player player) {
        player.setSummonedInLastTurn(true);
    }

    public PlayerController getOpponentPlayerController() {
        return getOtherPlayerController(getCurrentPlayerController());
    }

    public PlayerController getCurrentPlayerController() {
        if (game.getCurrentPlayer().equals(playerController1.getPlayer()))
            return playerController1;
        return playerController2;
    }

    public PlayerController getOtherPlayerController(PlayerController playerController) {
        if (playerController.getPlayer().equals(playerController1.getPlayer()))
            return playerController2;
        return playerController1;
    }

    private void goNextPhase(){
        boolean mustChangeTurn = game.getPhase() == Phase.END_PHASE;
        game.setPhase(game.getPhase().nextPhase());
        if(mustChangeTurn){
            game.changeTurn();
            playerController1.refresh();
            playerController2.refresh();
        }
        DuelMenuController.getInstance().getView().resetSelector();
    }

    public void goNextPhaseAndNotify() {
        MainGameThread.getInstance().stopRunningQueuedTasks();
        goNextPhase();
    }

    public PlayerController getPlayerControllerByPlayer(Player player){
        if(playerController1.getPlayer().equals(player))
            return playerController1;
        return playerController2;
    }

    public void control() {
        CustomPrinter.println(String.format("its %s's turn%n", game.getCurrentPlayer().getUser().getUsername()), Color.Blue);
        while (true) {
            if(!game.getPhase().equals(previousIterationPhase)){
                previousIterationPhase = game.getPhase();
                DuelMenuController.getInstance().printCurrentPhase();
            }
            if (game.getPhase().equals(Phase.DRAW_PHASE)) {
                CustomPrinter.println(String.format("its %s's turn%n", game.getCurrentPlayer().getUser().getUsername()), Color.Blue);
                drawCard();
                goNextPhase();
            } else if (game.getPhase().equals(Phase.STANDBY_PHASE)) {
                goNextPhase();
            } else if (game.getPhase().equals(Phase.MAIN_PHASE1)) {
                DuelMenuController.getInstance().showBoard();
                getCurrentPlayerController().controlMainPhase1();
            } else if (game.getPhase().equals(Phase.BATTLE_PHASE)) {
                DuelMenuController.getInstance().showBoard();
                getCurrentPlayerController().controlBattlePhase();
            } else if (game.getPhase().equals(Phase.MAIN_PHASE2)) {
                DuelMenuController.getInstance().showBoard();
                getCurrentPlayerController().controlMainPhase2();
            } else if (game.getPhase().equals(Phase.END_PHASE)) {
                goNextPhase();
            }
        }
    }
}
