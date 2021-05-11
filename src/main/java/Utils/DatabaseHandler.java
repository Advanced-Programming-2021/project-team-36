package Utils;

import model.User;
import com.google.gson.*;
import java.io.*;

public class DatabaseHandler {
    private static String databaseFilePath = "database/database.dat";
    private static String databaseAsJSONFilePath = "database/database.json";

    public static void importFromDatabase() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(databaseFilePath));
            try {
                while (true) {
                    User user = (User)objectInputStream.readObject();
                    user.save();
                }
            } catch (Exception exception) {
            }
        } catch (IOException exception) {
            System.out.println("failed to read from the database");
            return;
        }
    }

    public static void exportToDatabase() {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(databaseFilePath));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(databaseAsJSONFilePath));
            for (User user : User.retrieveUsersBasedOnScore()) {
                objectOutputStream.writeObject(user);
                bufferedWriter.write(jsonFormatter(new Gson().toJson(user)));
            }
            objectOutputStream.flush();
            objectOutputStream.close();
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException exception) {
            System.out.println("failed to save to database");
            return;
        }
    }

    private static String jsonFormatter(String json) {
        json = json.replaceAll(",", ",\n\t");
        json = json.replaceAll("\\{", "{\n\t");
        json = json.replaceAll("\\}", "\n}\n");
        return json;
    }
}
