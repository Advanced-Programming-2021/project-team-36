package Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CustomScanner {
    private static java.util.Scanner scanner = new java.util.Scanner(System.in);
    private static BufferedReader bufferedReader;
    private static int linesToReadFromFile = 0;

    public static void readTestFile(String file, int count) {
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
        } catch (IOException exception) {
            System.out.println("file not found");
            return;
        }
        linesToReadFromFile = count;
    }

    public static String nextLine() {
        while (bufferedReader != null) {
            if (linesToReadFromFile == 0)
                bufferedReader = null;
            linesToReadFromFile--;
            try {
                String line = bufferedReader.readLine();
                if (line != null)
                    return line;
                linesToReadFromFile = 0;
            } catch (Exception exception) {
            }
        }
        return scanner.nextLine();
    }
}
