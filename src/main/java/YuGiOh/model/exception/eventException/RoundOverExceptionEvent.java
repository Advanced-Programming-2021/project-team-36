package YuGiOh.model.exception.eventException;

import YuGiOh.model.Player.Player;
import YuGiOh.model.enums.GameResult;

public class RoundOverExceptionEvent extends GameExceptionEvent {
    public GameResult gameResult;
    public Player looser;
    public Player winner;
    public int winnersLP;

    public RoundOverExceptionEvent(GameResult gameResult, Player looser, Player winner, int winnersLP){
        super();
        this.gameResult = gameResult;
        this.looser = looser;
        this.winner = winner;
        this.winnersLP = winnersLP;
    }
}
