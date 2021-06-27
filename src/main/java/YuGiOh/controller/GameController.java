package YuGiOh.controller;

import YuGiOh.controller.events.RoundOverExceptionEvent;
import YuGiOh.controller.menu.DuelMenuController;
import YuGiOh.controller.player.AIPlayerController;
import YuGiOh.controller.player.HumanPlayerController;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.Player.Player;
import YuGiOh.model.enums.Color;
import YuGiOh.model.enums.GameResult;
import YuGiOh.utils.CustomPrinter;
import lombok.Getter;
import YuGiOh.model.Game;
import YuGiOh.model.Player.AIPlayer;
import YuGiOh.model.Player.HumanPlayer;
import YuGiOh.model.enums.Phase;
import lombok.Setter;

public class GameController {
    @Getter
    public static GameController instance;
    @Getter
    private final Game game;

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

    public void goNextPhase(){
        boolean mustChangeTurn = game.getPhase() == Phase.END_PHASE;
        game.setPhase(game.getPhase().nextPhase());
        if(mustChangeTurn){
            game.changeTurn();
            playerController1.refresh();
            playerController2.refresh();
        }
        DuelMenuController.getInstance().getView().resetSelector();
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
            if (game.getPhase().equals(Phase.DRAW_PHASE)) {
                CustomPrinter.println(String.format("its %s's turn%n", game.getCurrentPlayer().getUser().getUsername()), Color.Blue);
                drawCard();
                goNextPhase();
            } else if (game.getPhase().equals(Phase.STANDBY_PHASE)) {
                goNextPhase();
            } else if (game.getPhase().equals(Phase.MAIN_PHASE1)) {
                getCurrentPlayerController().controlMainPhase1();
            } else if (game.getPhase().equals(Phase.BATTLE_PHASE)) {
                getCurrentPlayerController().controlBattlePhase();
            } else if (game.getPhase().equals(Phase.MAIN_PHASE2)) {
                getCurrentPlayerController().controlMainPhase2();
            } else if (game.getPhase().equals(Phase.END_PHASE)) {
                goNextPhase();
            }
        }
    }
}
