package YuGiOh.utils;

import YuGiOh.model.enums.Color;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class CustomScanner {
    private static final java.util.Scanner scanner;
    private static BufferedReader bufferedReader;
    private static int linesToReadFromFile = 0;

    private static String lastBuffer;

    private final static Queue<String> injections;

    static {
        scanner = new java.util.Scanner(System.in);
        injections = new LinkedList<>();
        lastBuffer = "";
    }

    public static String getLastBuffer(){
        String cp = lastBuffer;
        lastBuffer = "";
        return cp;
    }
    public static void injectString(String injection){
        injections.add(injection);
    }

    public static void readTestFile(String file, int count) {
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
        } catch (IOException exception) {
            CustomPrinter.println("file not found", Color.Default);
            return;
        }
        linesToReadFromFile = count;
    }

    public static String nextLine() {
        if(!injections.isEmpty()){
            String ret = injections.poll();
            lastBuffer += ret + "\n";
            return ret;
        }
        while (bufferedReader != null) {
            if (linesToReadFromFile == 0)
                bufferedReader = null;
            linesToReadFromFile--;
            try {
                String line = bufferedReader.readLine();
                if (line != null) {
                    lastBuffer += line + "\n";
                    return line;
                }
                else {
                    linesToReadFromFile = 0;
                }
            } catch (Exception ignored) {
            }
        }
        if(Debugger.isTestMode())
            throw new Error("end of test");
        String ret = scanner.nextLine();
        lastBuffer += ret + "\n";
        return ret;
    }
}
