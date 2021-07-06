package YuGiOh.graphicView;

import YuGiOh.Main;
import YuGiOh.graphicController.DeckMenuController;
import YuGiOh.model.User;
import YuGiOh.model.deck.Deck;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class DeckMenuView extends BaseMenuView {
    private static DeckMenuView instance;

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private ListView<String> listView;
    @FXML
    private HBox buttonHBox;

    public DeckMenuView() {
        instance = this;
    }

    public static DeckMenuView getInstance() {
        if (instance == null)
            instance = new DeckMenuView();
        return instance;
    }

    public static void init(Stage primaryStage, User user) {
        try {
            Pane root = FXMLLoader.load(Main.class.getResource("/fxml/DeckMenu.fxml"));
            DeckMenuView.getInstance().start(primaryStage, root, user);
        } catch (IOException ignored) {
        }
    }

    public void start(Stage primaryStage, Pane root, User user) {
        this.stage = primaryStage;
        this.root = root;
        new DeckMenuController(user);
        scene = new Scene(root);
        run();
    }

    public void run() {
        renderInitialSettings();
        stage.setScene(scene);
        stage.show();
        relocateNodeFromCenter(scrollPane, scene.getWidth() * 0.5, 0);
        relocateFromUp(scrollPane, 30);
        relocateNodeFromCenter(buttonHBox, scene.getWidth() * 0.5, 0);
        relocateFromDown(buttonHBox, 30);
    }

    private void renderInitialSettings() {
        listView.getItems().clear();
        for (Deck deck : DeckMenuController.getInstance().getUser().getDecks()) {
            if (DeckMenuController.getInstance().getUser().getActiveDeck() == deck)
                listView.getItems().add(0, "Active: " + deck.getName());
            else
                listView.getItems().add(deck.getName());
        }
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    @FXML
    private void viewDeck() {
        if (listView.getSelectionModel().getSelectedItems().size() == 0) {
            new Alert(Alert.AlertType.ERROR, "You have not selected any deck.").showAndWait();
            return;
        }
        String selectedDeck = listView.getSelectionModel().getSelectedItem();
        if (selectedDeck.startsWith("Active: "))
            selectedDeck = selectedDeck.substring(8);
        DeckView.init(stage, DeckMenuController.getInstance().deckParser(selectedDeck));
    }
    @FXML
    private void deleteDeck() {
        if (listView.getSelectionModel().getSelectedItems().size() == 0) {
            new Alert(Alert.AlertType.ERROR, "You have not selected any deck.").showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this deck?",
                ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();
        if (!alert.getResult().getText().equalsIgnoreCase("Yes"))
            return;
        String selectedDeck = listView.getSelectionModel().getSelectedItem();
        if (selectedDeck.startsWith("Active: "))
            selectedDeck = selectedDeck.substring(8);
        DeckMenuController.getInstance().deleteDeck(selectedDeck);
        listView.getItems().remove(listView.getSelectionModel().getSelectedItem());
    }
    @FXML
    private void createDeck() {
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.getEditor().setPromptText("deck name");
        textInputDialog.setTitle("Deck Creation");
        textInputDialog.setHeaderText("Enter your desired deck name below:");
        while (textInputDialog.getEditor().getText() == null ||
                textInputDialog.getEditor().getText().equals(""))
            textInputDialog.showAndWait();
        try {
            DeckMenuController.getInstance().createDeck(textInputDialog.getEditor().getText());
            new Alert(Alert.AlertType.INFORMATION, "Deck created successfully!").showAndWait();
            listView.getItems().add(textInputDialog.getEditor().getText());
        } catch (Exception exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
        }

    }
    @FXML
    private void setActiveDeck() {
        if (listView.getSelectionModel().getSelectedItems().size() == 0) {
            new Alert(Alert.AlertType.ERROR, "You have not selected any deck.").showAndWait();
            return;
        }
        String activeDeck = null;
        if (DeckMenuController.getInstance().getUser().getActiveDeck() != null)
            activeDeck = DeckMenuController.getInstance().getUser().getActiveDeck().getName();
        String selectedItem = listView.getSelectionModel().getSelectedItem();
        if (selectedItem.startsWith("Active:")) {
            new Alert(Alert.AlertType.ERROR, "The deck is already active.").showAndWait();
            return;
        }
        DeckMenuController.getInstance().setActiveDeck(selectedItem);
        new Alert(Alert.AlertType.INFORMATION, "Deck activated successfully!").showAndWait();
        listView.getItems().remove(selectedItem);
        listView.getItems().add(0, "Active: " + selectedItem);
        if (activeDeck != null) {
            listView.getItems().remove("Active: " + activeDeck);
            listView.getItems().add(1, activeDeck);
        }
    }
    @FXML
    private void showAllCards() {
        ShowAllCardsView.init(stage);
    }
    @FXML
    private void exit() {
        MainMenuView.getInstance().run();
    }
}
