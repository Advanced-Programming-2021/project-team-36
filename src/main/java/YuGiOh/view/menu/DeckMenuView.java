package YuGiOh.view.menu;

import YuGiOh.ClientApplication;
import YuGiOh.api.DeckMenuApi;
import YuGiOh.api.ProfileMenuApi;
import YuGiOh.controller.menu.DeckMenuController;
import YuGiOh.model.User;
import YuGiOh.model.deck.Deck;
import YuGiOh.network.ClientConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DeckMenuView extends BaseMenuView {
    private static final String backgroundImageAddress = "assets/Backgrounds/GUI_T_TowerBg4.dds.png";
    private static DeckMenuView instance;

    @FXML
    private ImageView backgroundImageView;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private ListView<String> listView;
    @FXML
    private HBox buttonHBox;

    private DeckMenuApi api;

    public DeckMenuView() {
        instance = this;
    }

    public static DeckMenuView getInstance() {
        if (instance == null)
            instance = new DeckMenuView();
        return instance;
    }

    public static void init(Stage primaryStage) {
        try {
            Pane root = FXMLLoader.load(ClientApplication.class.getResource("/fxml/DeckMenu.fxml"));
            DeckMenuView.getInstance().start(primaryStage, root);
        } catch (IOException ignored) {
        }
    }

    public void start(Stage primaryStage, Pane root) {
        this.stage = primaryStage;
        this.root = root;
        try {
            this.api = new DeckMenuApi(ClientConnection.getOrCreateInstance());
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "check your connection to server and retry!").showAndWait();
            LoginMenuView.init(stage);
            return;
        }
        scene.setRoot(root);
        try {
            backgroundImageView.setImage(new Image(new FileInputStream(backgroundImageAddress)));
            backgroundImageView.toBack();
        } catch (FileNotFoundException ignored) {
        }
        run();
    }

    public void run() {
        renderInitialSettings();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        relocateNodeFromCenter(scrollPane, scene.getWidth() * 0.5, 0);
        relocateFromUp(scrollPane, 30);
        relocateNodeFromCenter(buttonHBox, scene.getWidth() * 0.5, 0);
        relocateFromDown(buttonHBox, 30);
    }

    private void renderInitialSettings() {
        listView.getItems().clear();
        api.getUserFromServer().thenAccept(user->{
            for (Deck deck : user.getDecks()) {
                if (user.getActiveDeck() == deck)
                    listView.getItems().add(0, "Active: " + deck.getName());
                else
                    listView.getItems().add(deck.getName());
            }
            listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        });
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
        api.deckParser(selectedDeck).thenAccept(deck -> {
            DeckView.init(stage, deck);
        });
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
        api.deleteDeck(selectedDeck);
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
        api.createDeck(textInputDialog.getEditor().getText())
                .whenComplete((res, ex)-> {
                    if (ex == null) {
                        new Alert(Alert.AlertType.INFORMATION, "Deck created successfully!").showAndWait();
                        listView.getItems().add(textInputDialog.getEditor().getText());
                    } else {
                        new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
                    }
                });
    }
    @FXML
    private void setActiveDeck() {
        if (listView.getSelectionModel().getSelectedItems().size() == 0) {
            new Alert(Alert.AlertType.ERROR, "You have not selected any deck.").showAndWait();
            return;
        }
        api.getUserFromServer().thenAccept(user->{
            String activeDeck = null;
            if (user.getActiveDeck() != null)
                activeDeck = user.getActiveDeck().getName();
            String selectedItem = listView.getSelectionModel().getSelectedItem();
            if (selectedItem.startsWith("Active:")) {
                new Alert(Alert.AlertType.ERROR, "The deck is already active.").showAndWait();
                return;
            }
            api.setActiveDeck(selectedItem);
            new Alert(Alert.AlertType.INFORMATION, "Deck activated successfully!").showAndWait();
            listView.getItems().remove(selectedItem);
            listView.getItems().add(0, "Active: " + selectedItem);
            if (activeDeck != null) {
                listView.getItems().remove("Active: " + activeDeck);
                listView.getItems().add(1, activeDeck);
            }
        });
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
