package YuGiOh.utils;

import YuGiOh.model.enums.Color;
import lombok.Getter;
import lombok.Setter;
import YuGiOh.view.CommandLine.InvalidCommandException;

import java.io.*;

public class Debugger {
    @Getter
    @Setter
    private static boolean testMode = false;
    private static boolean mode = false;
    private static boolean captureMode = false;
    private static boolean automaticSave = false;
    private static int currentTestBatch = 1;
    final private static String testFileHeader = "tests/TestBatch";
    private static BufferedWriter bufferedWriter;

    public static void setDebugMode(String modeAsString) throws InvalidCommandException {
        if (!modeAsString.equals("on") && !modeAsString.equals("off"))
            throw new InvalidCommandException();
        boolean mode = modeAsString.equals("on");
        if (Debugger.mode == mode) {
            CustomPrinter.println(String.format("debug mode is already set to %s", modeAsString), Color.Default);
            return;
        }
        Debugger.mode = mode;
    }

    public static void setAutomaticSave(String mode) throws InvalidCommandException {
        if (!mode.equals("on") && !mode.equals("off"))
            throw new InvalidCommandException();
        boolean automaticSave = mode.equals("on");
        if (Debugger.automaticSave == automaticSave) {
            CustomPrinter.println(String.format("debug automatic database mode is already set to %s", automaticSave), Color.Default);
            return;
        }
        Debugger.automaticSave = automaticSave;
    }

    public static boolean getAutomaticSave() {
        return Debugger.automaticSave;
    }

    public static void setCaptureMode(String captureModeAsString) throws InvalidCommandException {
        if (!captureModeAsString.equals("on") && !captureModeAsString.equals("off"))
            throw new InvalidCommandException();
        boolean captureMode = captureModeAsString.equals("on");
        if (Debugger.captureMode == captureMode) {
            CustomPrinter.println(String.format("debug capture mode is already set to %s", captureModeAsString), Color.Default);
            return;
        }
        Debugger.captureMode = captureMode;
        if (captureMode)
            initializeCapturingMode();
        else
            finalizeCapturingMode();
    }

    private static void initializeCapturingMode() {
        while (new File(testFileHeader + currentTestBatch + ".txt").exists())
            currentTestBatch++;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(testFileHeader + currentTestBatch + ".txt"));
        } catch (IOException exception) {
            CustomPrinter.println(exception, Color.Default);
            CustomPrinter.println("failed to create a test file", Color.Default);
            captureMode = false;
            return;
        }
    }

    private static void finalizeCapturingMode() {
        try {
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException exception) {
            CustomPrinter.println("failed to close the test file", Color.Default);
            captureMode = true;
            return;
        }
    }

    public static void captureCommand(String command) {
        if (command.startsWith("debug"))
            return;
        try {
            bufferedWriter.write(command + "\n");
        } catch (IOException exception) {
            CustomPrinter.println("failed to write into the test file", Color.Default);
            return;
        }
    }

    public static void importTest(String fileAsString, String countAsString) throws InvalidCommandException {
        if (countAsString == null)
            countAsString = "9999999"; // Just a large number.
        try {
            CustomScanner.readTestFile(fileAsString, Integer.parseInt(countAsString));
        } catch (Exception exception) {
            throw new InvalidCommandException();
        }
    }

    public static boolean getMode() {
        return Debugger.mode;
    }

    public static boolean getCaptureMode() {
        return Debugger.captureMode;
    }
}
