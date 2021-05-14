import Utils.CustomPrinter;
import Utils.CustomScanner;
import controller.ProgramController;
import lombok.Getter;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import view.Debugger;

public class Shayan {
    public final ProgramController controller;
    private String outputBuffer;

    {
        outputBuffer = "";
        controller = new ProgramController();
    }

    public void runUntilNoInput() {
        try {
            controller.control();
        } catch (Error error) {
            if (!error.getMessage().equals("end of test"))
                throw error;
        }
        outputBuffer += CustomPrinter.getLastBuffer();
    }

    public String getBuffer(){
        return outputBuffer;
    }
    public void clearBuffer(){
        outputBuffer = "";
    }
    public void checkEqualExact(String a) {
        Assertions.assertEquals(a, getBuffer());
    }
    public void checkEqualIgnoreNewLine(String a) {
        String b = CustomPrinter.getLastBuffer();
        Assertions.assertEquals(a.replaceAll("\n", ""), getBuffer().replaceAll("\n", ""));
    }
    public void checkNoInvalidCommandsInBuffer(){
        Assertions.assertFalse(getBuffer().matches(".*invalid command.*"));
    }
    public void inject(String data) {
        CustomScanner.injectString(data);
    }

    @Before
    public void setTestMode() {
        Debugger.setTestMode(true);
    }
}
