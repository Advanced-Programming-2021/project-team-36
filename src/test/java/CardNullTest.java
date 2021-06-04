import initialize.Sample;
import org.junit.Test;

public class CardNullTest extends Sample {
    @Test
    public void test(){
        initializeUser("shayan", "1234", "shayan.p");
        run("user login -u shayan -p 1234");
        run("menu enter shop");
        run("shop buy Suijin");
        checkNoInvalidCommandsInBuffer();
//        run("duel --new --second_player shayan --round 1");
//        run("show hand");
    }
}
