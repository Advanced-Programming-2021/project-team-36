package utils;

import model.User;
import com.google.gson.*;
import model.card.Card;
import model.card.Monster;
import model.card.Utils;
import model.deck.Deck;

import java.io.*;
import java.util.ArrayList;
import java.util.TreeMap;

public class DatabaseHandler {
    private static String databaseFilePath = "database/database.dat";
    private static String databaseAsJSONFilePath = "database/database.json";
    private static String databaseFileName = "database";
    private static String monsterDatabase = "database/Monster.csv";

    public static void importFromDatabase() {
        //importUsersFromDatabase();
        importMonstersFromDatabase();
    }

    public static void saveToDatabase(String fileName) {
        if (fileName == null)
            fileName = databaseFileName;
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(
                    "database/" + fileName + ".dat"));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(
                    "database/" + fileName + ".json"));
            for (User user : User.retrieveUsersBasedOnScore()) {
                objectOutputStream.writeObject(user);
                bufferedWriter.write(jsonFormatter(new Gson().toJson(user)));
            }
            objectOutputStream.flush();
            objectOutputStream.close();
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException exception) {
            CustomPrinter.println("failed to save to database");
            return;
        }
    }

    public static void loadFromDatabase(String fileName) {
        if (fileName == null)
            fileName = databaseFileName;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(
                    "database/" + fileName + ".dat"));
            try {
                while (true) {
                    Object object = objectInputStream.readObject();
                    assert object instanceof User;
                    if (object instanceof User) {
                        User user = (User)object;
                        user.save();
                    }
                }
            } catch (Exception exception) {
            }
        } catch (IOException exception) {
            CustomPrinter.println("failed to read from the database");
            return;
        }
    }

    public static void exportToDatabase() {
        saveToDatabase(databaseFileName);
    }

    public static void importUsersFromDatabase() {
        loadFromDatabase(databaseFileName);
    }

    private static void importMonstersFromDatabase() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(monsterDatabase));
            ArrayList<String> header = monsterCSVParser(bufferedReader.readLine());
            while (true) {
                try {
                    TreeMap<String, String> monsterData = new TreeMap<String, String>();
                    String line = bufferedReader.readLine();
                    if (line == null)
                        break;
                    ArrayList<String> data = monsterCSVParser(line);
                    if (data.size() > 0) {
                        for (int i = 0; i < data.size(); i++) {
                            monsterData.put(header.get(i).trim(), data.get(i).trim());
                        }
                        Utils.addMonsterData(monsterData);
                    } else
                        break;
                } catch (EOFException exception) {
                    break;
                }
            }
        } catch (IOException exception) {
            CustomPrinter.println("fatal error : Monster database was not found");
            return;
        }
    }

    private static ArrayList<String> monsterCSVParser(String row) {
        String[] fields = row.replaceAll("\\s+", " ").split(",");
        ArrayList<String> results = new ArrayList<>();
        for (int i = 0; i < fields.length; i ++) {
            fields[i] = fields[i].trim();
            int right = i, count = countInString(fields[i], '\"');
            while (count % 2 == 1) {
                right ++;
                fields[i] += "," + fields[right];
                count += countInString(fields[right], '\"');
            }
            results.add(fields[i].replaceAll("\"", "").trim());
            i = right;
        }
        return results;
    }

    private static String jsonFormatter(String json) {
        json = json.replaceAll(",", ",\n\t");
        json = json.replaceAll("\\{", "{\n\t");
        json = json.replaceAll("\\}", "\n}\n");
        return json;
    }

    private static int countInString(String string, char ch) {
        int count = 0;
        for (int i = 0; i < string.length(); i ++)
            if (string.charAt(i) == ch)
                count ++;
        return count;
    }
}
