package edu.sharif.nameless.in.seattle.yugioh.controller.events;

import edu.sharif.nameless.in.seattle.yugioh.model.Player.Player;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.GameResult;

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
