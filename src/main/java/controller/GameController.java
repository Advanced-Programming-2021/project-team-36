package controller;

import Utils.CustomPrinter;
import controller.events.GameOver;
import controller.player.AIPlayerController;
import controller.player.HumanPlayerController;
import controller.player.PlayerController;
import lombok.Getter;
import model.Game;
import model.Player.AIPlayer;
import model.Player.HumanPlayer;
import model.Player.Player;
import model.card.Card;
import model.enums.GameResult;
import model.enums.Phase;

// this controller provides functions for player controller to access to.
// so for example players don't attack each other directly!

public class GameController {
    @Getter
    public static GameController instance;
    @Getter
    private final Game game;
    private final PlayerController playerController1, playerController2;

    public GameController(Game game){
        this.game = game;
        instance = this;

        this.playerController1 = game.getFirstPlayer() instanceof HumanPlayer ?
                new HumanPlayerController((HumanPlayer) game.getFirstPlayer()) :
                new AIPlayerController((AIPlayer) game.getFirstPlayer());
        this.playerController2 = game.getSecondPlayer() instanceof HumanPlayer ?
                new HumanPlayerController((HumanPlayer) game.getSecondPlayer()) :
                new AIPlayerController((AIPlayer) game.getSecondPlayer());
        new CardSelector(game);
    }

    public void drawCard() throws GameOver {
        Card card = game.getCurrentPlayer().getMainDeck().getTopCard();
        if (card == null)
            throw new GameOver(GameResult.LOOSE, game.getCurrentPlayer(), game.getOpponentPlayer());
        game.getCurrentPlayer().getBoard().drawCardFromDeck();
        CustomPrinter.println(String.format("new card added to the hand : %s%n", card.getName()));
    }
    public void checkBothLivesEndGame() throws GameOver {
        if (game.getCurrentPlayer().getLifePoint() <= 0 && game.getOpponentPlayer().getLifePoint() <= 0)
            throw new GameOver(GameResult.DRAW, game.getCurrentPlayer(), game.getOpponentPlayer());
        if (game.getCurrentPlayer().getLifePoint() <= 0)
            throw new GameOver(GameResult.WIN, game.getCurrentPlayer(), game.getOpponentPlayer());
        if (game.getOpponentPlayer().getLifePoint() <= 0)
            throw new GameOver(GameResult.LOOSE, game.getOpponentPlayer(), game.getCurrentPlayer());
    }

    public void moveCardToGraveYard(Card card){
        // also you can do some extra things here
        game.moveCardToGraveYard(card);
    }
    public void decreaseLifePoint(Player player, int amount){
        // also you can do some extra things here
        player.decreaseLifePoint(amount);
    }


    public PlayerController getCurrentPlayerController(){
        if(game.getCurrentPlayer().equals(playerController1.getPlayer()))
            return playerController1;
        return playerController2;
    }
    public PlayerController getOtherPlayerController(PlayerController playerController){
        if(playerController.getPlayer().equals(playerController1.getPlayer()))
            return playerController2;
        return playerController1;
    }

    private void changeTurn() {
        game.changeTurn();

        // todo tell shayan what is this?
        game.setSummonedInThisTurn(false);
        new CardSelector(game);
    }

    private void endGame(Player winner, Player loser) {
        winner.getUser().increaseScore(1000);
        winner.getUser().increaseBalance(1000 + winner.getLifePoint());
        loser.getUser().increaseBalance(100);
    }

    public void goNextPhase() {
        // todo. discuss this phases. why only main1, main2, battle is shown?
        // todo tell shayan what is this
        if (game.isFirstTurn() && game.getPhase().equals(Phase.BATTLE_PHASE))
            game.setPhase(Phase.MAIN_PHASE2);
        if (game.getPhase() == Phase.END_PHASE)
            changeTurn();
        game.setPhase(game.getPhase().nextPhase());
        new CardSelector(game);
    }

    public void control(){
        while(true){
            new CardSelector(game);
            try {
                if(game.getPhase().equals(Phase.DRAW_PHASE)) {
                    CustomPrinter.println(String.format("its %s's turn%n", game.getCurrentPlayer().getUser().getNickname()));
                    drawCard();
                    getCurrentPlayerController().controlDrawPhase();
                } else if(game.getPhase().equals(Phase.STANDBY_PHASE)){
                    getCurrentPlayerController().controlStandbyPhase();
                } else if(game.getPhase().equals(Phase.MAIN_PHASE1)){
                    getCurrentPlayerController().controlMainPhase1();
                } else if(game.getPhase().equals(Phase.BATTLE_PHASE)){
                    getCurrentPlayerController().controlBattlePhase();
                } else if(game.getPhase().equals(Phase.MAIN_PHASE2)){
                    getCurrentPlayerController().controlMainPhase2();
                } else if(game.getPhase().equals(Phase.END_PHASE)) {
                    getCurrentPlayerController().controlEndPhase();
                }
            } catch (GameOver gameOver){
                // todo handle draw
                endGame(gameOver.winner, gameOver.looser);
                break;
            }
        }
    }
}