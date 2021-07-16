package YuGiOh.view.menu;

import YuGiOh.ClientApplication;
import YuGiOh.controller.menu.FactoryMenuController;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Utils;
import YuGiOh.model.enums.MonsterAttribute;
import YuGiOh.model.enums.MonsterCardType;
import YuGiOh.model.enums.MonsterType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FactoryView extends BaseMenuView {
    private static final double imageWidth = 90, imageHeight = imageWidth * 614.0 / 421.0, HGAP = 1.8;
    private static final String backgroundImageAddress = "assets/Backgrounds/GUI_T_TowerBg2.dds.png";

    private static FactoryView instance;
    private Card card;

    @FXML
    private TextField nameField, attackField, defenseField, descriptionField;
    @FXML
    private ChoiceBox<Integer> levelChoiceBox;
    @FXML
    private ChoiceBox<MonsterType> monsterTypeChoiceBox;
    @FXML
    private ChoiceBox<MonsterAttribute> monsterAttributeChoiceBox;
    @FXML
    private ChoiceBox<MonsterCardType> monsterCardTypeChoiceBox;
    @FXML
    private Label priceLabel;
    @FXML
    private ImageView backgroundImageView, selectedCardImageView;

    public FactoryView() {
        instance = this;
    }

    public static FactoryView getInstance() {
        if (instance == null)
            instance = new FactoryView();
        return instance;
    }

    public static void init(Stage primaryStage, Card card) {
        try {
            Pane root = FXMLLoader.load(ClientApplication.class.getResource("/fxml/FactoryView.fxml"));
            FactoryView.getInstance().start(primaryStage, root, card);
        } catch (IOException ignored) {
        }
    }

    public void start(Stage primaryStage, Pane root, Card card) {
        this.stage = primaryStage;
        this.root = root;
        this.card = card;
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
    }

    private void renderInitialSettings() {
        selectedCardImageView.setImage(Utils.getCardImageView(card));
        for (int i = 1; i <= 10; i ++)
            levelChoiceBox.getItems().add(i);
        monsterTypeChoiceBox.getItems().addAll(MonsterType.values());
        monsterAttributeChoiceBox.getItems().addAll(MonsterAttribute.values());
        monsterCardTypeChoiceBox.getItems().addAll(MonsterCardType.values());
        levelChoiceBox.getSelectionModel().select(0);
        monsterTypeChoiceBox.getSelectionModel().select(0);
        monsterAttributeChoiceBox.getSelectionModel().select(0);
        monsterCardTypeChoiceBox.getSelectionModel().select(0);
        priceLabel.setText("");
        scene.addEventHandler(MouseEvent.ANY, event -> {updatePrice();});
        scene.addEventHandler(KeyEvent.ANY, event -> {updatePrice();});
    }

    private void updatePrice() {
        try {
            int attack = Integer.parseInt(attackField.getText());
            int defense = Integer.parseInt(attackField.getText());
            FactoryMenuController.getInstance().setAttackDamage(attack);
            FactoryMenuController.getInstance().setDefenseRate(defense);
            FactoryMenuController.getInstance().setLevel(levelChoiceBox.getSelectionModel().getSelectedItem());
            FactoryMenuController.getInstance().setMonster(monsterCardTypeChoiceBox.getSelectionModel().getSelectedItem());
            priceLabel.setText(FactoryMenuController.getInstance().getPrice() + "");
        } catch (Exception ignored) {
            priceLabel.setText("");
        }
    }
    @FXML
    private void createCard() {
        int attack = 0, defense = 0;
        try {
            attack = Integer.parseInt(attackField.getText());
            defense = Integer.parseInt(attackField.getText());
        } catch (Exception exception) {
            new Alert(Alert.AlertType.ERROR, "Attack and Defense field should be integers.").showAndWait();
            return;
        }
        try {
            FactoryMenuController.getInstance().setCardName(nameField.getText());
            FactoryMenuController.getInstance().setAttackDamage(attack);
            FactoryMenuController.getInstance().setDefenseRate(defense);
            FactoryMenuController.getInstance().setDescription(descriptionField.getText());
            FactoryMenuController.getInstance().setLevel(levelChoiceBox.getSelectionModel().getSelectedItem());
            FactoryMenuController.getInstance().setMonsterType(monsterTypeChoiceBox.getSelectionModel().getSelectedItem());
            FactoryMenuController.getInstance().setMonsterAttribute(monsterAttributeChoiceBox.getSelectionModel().getSelectedItem());
            FactoryMenuController.getInstance().setMonster(monsterCardTypeChoiceBox.getSelectionModel().getSelectedItem());
            FactoryMenuController.getInstance().submitThisMonster();
            new Alert(Alert.AlertType.INFORMATION, "Card was successfully created!").showAndWait();
            exit();
        } catch (Exception exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
        }
    }
    @FXML
    private void exit() {
        try {
            FactoryMenuController.getInstance().discardThisMonster();
        } catch (Exception ignored) {
        }
        CardFactoryMenuView.getInstance().run();
    }
}
