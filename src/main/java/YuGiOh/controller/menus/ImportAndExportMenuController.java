package YuGiOh.controller.menus;

import YuGiOh.controller.LogicException;
import YuGiOh.model.GeneralCard;
import YuGiOh.model.User;
import YuGiOh.model.card.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ImportAndExportMenuController extends BaseMenuController {

    private static ImportAndExportMenuController instance;
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(GeneralCard.class, new GeneralCard.Deserializer())
            .create();

    private final User user;

    public ImportAndExportMenuController(User user) {
        instance = this;
        this.user = user;
    }

    public static List<Card> importFromFile(Path path) throws IOException {
        Type listOfMyClassObject = new TypeToken<ArrayList<GeneralCard>>() {}.getType();
        ArrayList<GeneralCard> ret = gson.fromJson(Files.readString(path), listOfMyClassObject);
        return ret.stream().map(GeneralCard::getCard).collect(Collectors.toList());
    }
    public static void exportToFile(Path path, List<Card> cards) throws IOException {
        ArrayList<GeneralCard> clonedCards = cards.stream().map(GeneralCard::new).collect(Collectors.toCollection(ArrayList::new));
        Files.writeString(path, gson.toJson(clonedCards));
    }
    public void saveMyCards(Path path) throws IOException {
        exportToFile(path, Utils.getInventedCards());
    }
    public void loadMyCards(Path path) throws IOException, LogicException {
        LogicException exception = null;
        for(Card card : importFromFile(path)){
            try {
                Utils.addCardToInvented(card);
            } catch (LogicException e) {
                exception = e;
            }
        }
        if(exception != null)
            throw exception;
    }
}
