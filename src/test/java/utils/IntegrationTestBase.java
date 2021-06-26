package utils;

import YuGiOh.utils.CustomPrinter;
import YuGiOh.utils.CustomScanner;
import YuGiOh.controller.ProgramController;
import YuGiOh.controller.menu.BaseMenuController;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import YuGiOh.utils.Debugger;

public class IntegrationTestBase {
    public final ProgramController controller;
    private String outputBuffer;
    private String lastPrintedLine;

    {
        outputBuffer = "";
        lastPrintedLine = "";
        controller = new ProgramController();
    }

    public void runUntilNoInput() {
        try {
            controller.control();
        } catch (Error error) {
            if (error.getMessage() == null || !error.getMessage().equals("end of test"))
                throw error;
        } finally {
            lastPrintedLine = CustomPrinter.getLastBuffer();
            outputBuffer += lastPrintedLine;
        }
    }

    public String getBuffer(){
        return outputBuffer;
    }
    public void clearBuffer(){
        outputBuffer = "";
    }
    public void checkEqualExact(String a) {
        Assertions.assertEquals(a, lastPrintedLine);
    }
    public void checkEqualIgnoreNewLine(String a) {
        Assertions.assertEquals(a.replaceAll("\n", ""), lastPrintedLine.replaceAll("\n", ""));
    }
    public void checkNoInvalidCommandsInBuffer(){
        Assertions.assertFalse(getBuffer().contains("invalid"));
    }
    public void run(String data) {
        CustomScanner.injectString(data);
        runUntilNoInput();
    }
    public void checkCurrentMenu(Class<? extends BaseMenuController> clazz) {
        Assertions.assertEquals(ProgramController.getInstance().getCurrentController().getClass(), clazz);
    }

    @Before
    public void setTestMode() {
        Debugger.setTestMode(true);
    }
}
