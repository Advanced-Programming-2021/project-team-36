package Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CustomScanner {
    private static java.util.Scanner scanner = new java.util.Scanner(System.in);
    private static BufferedReader bufferedReader;
    private static int LinesToReadFromFile = 0;

    public static void readTestFile(String file, int count) {
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
        } catch (IOException exception){
            System.out.println("file not found");
            return;
        }
        LinesToReadFromFile = count;
    }

    public static String nextLine() {
        while (bufferedReader != null) {
            if (LinesToReadFromFile == 0)
                bufferedReader = null;
            LinesToReadFromFile--;
            try {
                return bufferedReader.readLine();
            } catch (Exception exception) {
            }
        }
        return scanner.nextLine();
    }
}
