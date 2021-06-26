import initialize.Sample;
import org.junit.Test;

public class ScoreBoardTest extends Sample {
    @Test
    public void showScoreBoard() {
        initializeUser("shayan", "1234", "Shayan.P");
        run("user login -u shayan -p 1234");
        run("menu enter ScoreBoard");
        run("scoreboard show");
    }
}
