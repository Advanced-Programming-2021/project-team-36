package controller.events;

import model.Player.Player;
import model.enums.GameResult;

public class GameOverEvent extends GameEvent{
    public GameResult gameResult;
    public Player looser;
    public Player winner;
    public int winnersLP;

    public GameOverEvent(GameResult gameResult, Player looser, Player winner, int winnersLP){
        super();
        this.gameResult = gameResult;
        this.looser = looser;
        this.winner = winner;
        this.winnersLP = winnersLP;
    }
}
