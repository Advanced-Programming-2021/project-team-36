package controller.events;

import model.Player.Player;
import model.enums.GameResult;

public class GameOverEvent extends GameEvent{
    public GameResult gameResult;
    public Player looser;
    public Player winner;

    public GameOverEvent(GameResult gameResult, Player looser, Player winner){
        super();
        this.gameResult = gameResult;
        this.looser = looser;
        this.winner = winner;
    }
}
