import initialize.Sample;
import org.junit.Test;

public class ProfileChangeTest extends Sample {
    @Test
    public void profileChangingWithNoNicknameArgument(){
        initializeUser("shayan", "1234", "Shayan.P");
        run("user login -u shayan -p 1234");
        run("menu enter Profile");
        run("profile change --nickname --password 1234");
        run("menu exit   ");
        run("menu enter scoreboard");
        run("scoreboard show");
    }
}
