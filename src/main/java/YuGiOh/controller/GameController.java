package YuGiOh.controller;

import YuGiOh.controller.events.RoundOverExceptionEvent;
import YuGiOh.controller.menu.DuelMenuController;
import YuGiOh.controller.player.AIPlayerController;
import YuGiOh.controller.player.AggressiveAIPlayerController;
import YuGiOh.controller.player.HumanPlayerController;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.Player.Player;
import YuGiOh.model.enums.Color;
import YuGiOh.model.enums.GameResult;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.view.gui.GuiReporter;
import YuGiOh.view.gui.event.RoundOverEvent;
import lombok.Getter;
import YuGiOh.model.Game;
import YuGiOh.model.Player.AIPlayer;
import YuGiOh.model.Player.HumanPlayer;
import YuGiOh.model.card.Card;
import YuGiOh.model.enums.Phase;
import lombok.Setter;

// this controller provides functions for player controller to access to.
// so for example players don't attack each other directly!

public class GameController {
    @Getter
    public static GameController instance;
    @Getter
    private final Game game;

    // todo remove this in production
    // private final PlayerController playerController1, playerController2;

    @Setter
    private PlayerController playerController1, playerController2;

    private Phase previousIterationPhase;

    public GameController(Game game) {
        this.game = game;
        instance = this;

        this.playerController1 = game.getFirstPlayer() instanceof HumanPlayer ?
                new HumanPlayerController((HumanPlayer) game.getFirstPlayer()) :
                new AIPlayerController((AIPlayer) game.getFirstPlayer());
        this.playerController2 = game.getSecondPlayer() instanceof HumanPlayer ?
                new HumanPlayerController((HumanPlayer) game.getSecondPlayer()) :
                new AIPlayerController((AIPlayer) game.getSecondPlayer());
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

    public void decreaseLifePoint(Player player, int amount) {
        // also you can do some extra things here
        player.decreaseLifePoint(amount);
        CustomPrinter.println(String.format("User <%s>'s life point decreased by <%d> and it is <%d> now", player.getUser().getNickname(), amount, player.getLifePoint()), Color.Blue);
        checkBothLivesEndGame();
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

    public void endRound(RoundOverExceptionEvent event) {
        if(event.gameResult.equals(GameResult.DRAW)) {
            CustomPrinter.println(String.format("game is a draw and the score is %d-%d", game.totalScore(game.getFirstPlayer()), game.totalScore(game.getSecondPlayer())), Color.Blue);
            game.addFirstPlayerLastRoundScore(0);
            game.addSecondPlayerLastRoundScore(0);
        }
        else {
            if (event.winner.getUser().getUsername().equals(game.getFirstPlayer().getUser().getUsername())) {
                game.addFirstPlayerLastRoundScore(event.winnersLP);
                game.addSecondPlayerLastRoundScore(0);
            }
            else {
                game.addFirstPlayerLastRoundScore(0);
                game.addSecondPlayerLastRoundScore(event.winnersLP);
            }
            CustomPrinter.println(String.format("%s won the game and the score is: %d-%d", event.winner.getUser().getUsername(), game.totalScore(game.getFirstPlayer()), game.totalScore(game.getSecondPlayer())), Color.Blue);
        }
        if (game.totalScore(game.getFirstPlayer()) > game.getRounds() / 2)
            DuelMenuController.getInstance().endDuel(game.getFirstPlayer(), game.getSecondPlayer(), game.getRounds(), game.getMaxLP(game.getFirstPlayer()), game.totalScore(game.getFirstPlayer()), game.totalScore(game.getSecondPlayer()));
        else if (game.totalScore(game.getSecondPlayer()) > game.getRounds() / 2)
            DuelMenuController.getInstance().endDuel(game.getSecondPlayer(), game.getFirstPlayer(), game.getRounds(), game.getMaxLP(game.getSecondPlayer()), game.totalScore(game.getFirstPlayer()), game.totalScore(game.getSecondPlayer()));
        else {
            game.getFirstPlayer().refresh();
            game.getSecondPlayer().refresh();
        }
    }

    private void goNextPhase(){
        boolean mustChangeTurn = game.getPhase() == Phase.END_PHASE;
        game.setPhase(game.getPhase().nextPhase());
        if(mustChangeTurn){
            game.changeTurn();
            playerController1.refresh();
            playerController2.refresh();
        }
        DuelMenuController.getInstance().getGraphicView().resetSelector();
    }

    public void goNextPhaseAndNotify() {
        synchronized (game.phaseProperty()) {
            game.phaseProperty().notifyAll(); // todo any problem for AI?
            goNextPhase();
        }
    }

    public PlayerController getPlayerControllerByPlayer(Player player){
        if(playerController1.getPlayer().equals(player))
            return playerController1;
        return playerController2;
    }

    public void control() {
        while (true) {
            if(!game.getPhase().equals(previousIterationPhase)){
                previousIterationPhase = game.getPhase();
                DuelMenuController.getInstance().printCurrentPhase();
            }
            try {
                if (game.getPhase().equals(Phase.DRAW_PHASE)) {
                    CustomPrinter.println(String.format("its %s's turn%n", game.getCurrentPlayer().getUser().getNickname()), Color.Blue);
                    // todo : check player can draw or not (effects)
                    drawCard();
                    goNextPhase();
                } else if (game.getPhase().equals(Phase.STANDBY_PHASE)) {
                    getCurrentPlayerController().controlStandbyPhase();
                } else if (game.getPhase().equals(Phase.MAIN_PHASE1)) {
                    getCurrentPlayerController().controlMainPhase1();
                } else if (game.getPhase().equals(Phase.BATTLE_PHASE)) {
                    getCurrentPlayerController().controlBattlePhase();
                } else if (game.getPhase().equals(Phase.MAIN_PHASE2)) {
                    getCurrentPlayerController().controlMainPhase2();
                } else if (game.getPhase().equals(Phase.END_PHASE)) {
                    goNextPhase();
                }
            } catch (RoundOverExceptionEvent roundOverEvent) {
                GuiReporter.getInstance().report(new RoundOverEvent(roundOverEvent));
            }
        }
    }
}
