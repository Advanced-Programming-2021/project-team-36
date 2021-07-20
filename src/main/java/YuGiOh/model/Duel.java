package YuGiOh.model;

import YuGiOh.model.exception.ModelException;
import YuGiOh.model.exception.eventException.RoundOverExceptionEvent;
import YuGiOh.model.Player.Player;
import YuGiOh.model.enums.Color;
import YuGiOh.model.enums.GameResult;
import YuGiOh.utils.CustomPrinter;
import lombok.Getter;

import java.util.*;

public class Duel {
    @Getter
    private final int rounds;

    @Getter
    private int currentRound;

    @Getter
    private Game currentGame;

    @Getter
    private final Player firstPlayer;
    @Getter
    private final Player secondPlayer;

    @Getter
    private boolean finished;

    @Getter
    private String lastGameState;

    private final List<Integer> firstPlayerScores = new ArrayList<>();
    private final List<Integer> secondPlayerScores = new ArrayList<>();
    private static final List<Duel> duels = new ArrayList<>();

    public Duel(Player firstPlayer, Player secondPlayer, int rounds) throws ModelException {
        if (new Random().nextInt(2) == 0) {
            this.firstPlayer = firstPlayer;
            this.secondPlayer = secondPlayer;
        } else {
            this.firstPlayer = secondPlayer;
            this.secondPlayer = firstPlayer;
        }
        this.rounds = rounds;
        this.currentRound = 0;
        this.currentGame = new Game(firstPlayer, secondPlayer);
        duels.add(this);
    }

    public static Optional<Duel> getUserLastDuel(User user) {
        return duels.stream().filter(duel ->
                duel.getFirstPlayer().getUser().getUserId() == user.getUserId() || duel.getSecondPlayer().getUser().getUserId() == user.getUserId()
        ).findAny();
    }

    private void addFirstPlayerLastRoundScore(int score) {
        firstPlayerScores.add(score);
    }

    private void addSecondPlayerLastRoundScore(int score) {
        secondPlayerScores.add(score);
    }

    private int countNonZero(List<Integer> arrayList) {
        int cnt = 0;
        for (int x : arrayList)
            if (x > 0)
                cnt++;
        return cnt;
    }

    private int getMaxLP(Player player) {
        if (player == firstPlayer)
            return Collections.max(firstPlayerScores);
        else
            return Collections.max(secondPlayerScores);
    }

    private int totalScore(Player player) {
        if (player == firstPlayer)
            return countNonZero(firstPlayerScores);
        else
            return countNonZero(secondPlayerScores);
    }

    public void goNextRound(RoundOverExceptionEvent event) throws ModelException {
        if (!finished) {
            if (event.gameResult.equals(GameResult.DRAW)) {
                lastGameState = String.format("game is a draw and the score is %d-%d", totalScore(getFirstPlayer()), totalScore(getSecondPlayer()));
                CustomPrinter.println(lastGameState, Color.Blue);
                addFirstPlayerLastRoundScore(0);
                addSecondPlayerLastRoundScore(0);
            } else {
                if (event.winner.getUser().getUsername().equals(getFirstPlayer().getUser().getUsername())) {
                    addFirstPlayerLastRoundScore(event.winnersLP);
                    addSecondPlayerLastRoundScore(0);
                } else {
                    addFirstPlayerLastRoundScore(0);
                    addSecondPlayerLastRoundScore(event.winnersLP);
                }
                lastGameState = String.format("%s won the game and the score is: %d-%d", event.winner.getUser().getUsername(), totalScore(getFirstPlayer()), totalScore(getSecondPlayer()));
                CustomPrinter.println(lastGameState, Color.Blue);
            }
            if (totalScore(getFirstPlayer()) > getRounds() / 2) {
                endDuel(getFirstPlayer(), getSecondPlayer(), getRounds(), getMaxLP(getFirstPlayer()), totalScore(getFirstPlayer()), totalScore(getSecondPlayer()));
            } else if (totalScore(getSecondPlayer()) > getRounds() / 2) {
                endDuel(getSecondPlayer(), getFirstPlayer(), getRounds(), getMaxLP(getSecondPlayer()), totalScore(getFirstPlayer()), totalScore(getSecondPlayer()));
            } else {
                currentRound = currentRound + 1;
                firstPlayer.refresh();
                secondPlayer.refresh();
                currentGame = new Game(firstPlayer, secondPlayer);
            }
        }
    }

    private void endDuel(Player winner, Player looser, int rounds, int maxWinnerLP, int firstPlayerScore, int secondPlayerScore) {
        winner.getUser().increaseScore(rounds * 1000);
        winner.getUser().increaseBalance(rounds * (1000 + maxWinnerLP));
        looser.getUser().increaseBalance(rounds * 100);
        finished = true;
        lastGameState = String.format("%s won the whole match with score: %d-%d", winner.getUser().getUsername(), firstPlayerScore, secondPlayerScore);
        CustomPrinter.println(lastGameState, Color.Blue);
    }
}
