package controller.events;

import model.Player.Player;
import model.enums.GameResult;

public class GameOver extends GameEvent{
    public GameResult gameResult;
    public Player looser;
    public Player winner;

    public GameOver(GameResult gameResult, Player looser, Player winner){
        super();
        this.gameResult = gameResult;
        this.looser = looser;
        this.winner = winner;
    }
}
