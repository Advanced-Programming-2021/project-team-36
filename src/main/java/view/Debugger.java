package view;

import Utils.CustomPrinter;
import Utils.CustomScanner;
import lombok.Getter;
import lombok.Setter;
import view.CommandLine.InvalidCommandException;

import java.io.*;

public class Debugger {
    @Getter
    @Setter
    private static boolean testMode = false;
    private static boolean mode = false;
    private static boolean captureMode = false;
    private static int currentTestBatch = 1;
    final private static String testFileHeader = "tests/TestBatch";
    private static BufferedWriter bufferedWriter;

    public static void setDebugMode(String modeAsString) throws InvalidCommandException {
        if (!modeAsString.equals("on") && !modeAsString.equals("off"))
            throw new InvalidCommandException();
        boolean mode = modeAsString.equals("on");
        if (Debugger.mode == mode) {
            CustomPrinter.println(String.format("debug mode is already set to %s", modeAsString));
            return;
        }
        Debugger.mode = mode;
        if (mode)
            initializeDebuggingMode();
        else
            finalizeDebuggingMode();
    }

    private static void initializeDebuggingMode() {
        // TODO : Add some cool stuff.
    }

    private static void finalizeDebuggingMode() {
        // TODO : Add some cool stuff.
    }

    public static void setCaptureMode(String captureModeAsString) throws InvalidCommandException {
        if (!captureModeAsString.equals("on") && !captureModeAsString.equals("off"))
            throw new InvalidCommandException();
        boolean captureMode = captureModeAsString.equals("on");
        if (Debugger.captureMode == captureMode) {
            CustomPrinter.println(String.format("debug capture mode is already set to %", captureModeAsString));
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
            CustomPrinter.println(exception);
            CustomPrinter.println("failed to create a test file");
            captureMode = false;
            return;
        }
    }

    private static void finalizeCapturingMode() {
        try {
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException exception) {
            CustomPrinter.println("failed to close the test file");
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
            CustomPrinter.println("failed to write into the test file");
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
