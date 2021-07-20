package YuGiOh.view.menu;

import YuGiOh.ClientApplication;
import YuGiOh.api.FactoryMenuApi;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Utils;
import YuGiOh.model.enums.MonsterAttribute;
import YuGiOh.model.enums.MonsterCardType;
import YuGiOh.model.enums.MonsterType;
import YuGiOh.model.exception.LogicException;
import YuGiOh.network.ClientConnection;
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
    private Monster selectedMonster;

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

    private FactoryMenuApi api;

    public FactoryView() {
        instance = this;
    }

    public static FactoryView getInstance() {
        if (instance == null)
            instance = new FactoryView();
        return instance;
    }

    public static void init(Stage primaryStage, Monster monster) {
        try {
            Pane root = FXMLLoader.load(ClientApplication.class.getResource("/fxml/FactoryView.fxml"));
            FactoryView.getInstance().start(primaryStage, root, monster);
        } catch (IOException ignored) {
        }
    }

    public void start(Stage primaryStage, Pane root, Monster monster) {
        this.stage = primaryStage;
        this.root = root;
        this.selectedMonster = monster;
        try {
            this.api = new FactoryMenuApi(ClientConnection.getOrCreateInstance());
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
    }

    private void renderInitialSettings() {
        selectedCardImageView.setImage(Utils.getCardImageView(selectedMonster));
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
            int defense = Integer.parseInt(defenseField.getText());
            setAttackDamage(attack);
            setDefenseRate(defense);
            setLevel(levelChoiceBox.getSelectionModel().getSelectedItem());
            setCardTypeMonster(monsterCardTypeChoiceBox.getSelectionModel().getSelectedItem());
            api.getPrice(selectedMonster).thenAccept(price-> priceLabel.setText(price + ""));
        } catch (Exception ignored) {
            priceLabel.setText("");
        }
    }
    @FXML
    private void createCard() {
        int attack = 0, defense = 0;
        try {
            attack = Integer.parseInt(attackField.getText());
            defense = Integer.parseInt(defenseField.getText());
        } catch (Exception exception) {
            new Alert(Alert.AlertType.ERROR, "Attack and Defense field should be integers.").showAndWait();
            return;
        }
        try {
            setCardName(nameField.getText());
            setAttackDamage(attack);
            setDefenseRate(defense);
            setDescription(descriptionField.getText());
            setLevel(levelChoiceBox.getSelectionModel().getSelectedItem());
            setMonsterType(monsterTypeChoiceBox.getSelectionModel().getSelectedItem());
            setMonsterAttribute(monsterAttributeChoiceBox.getSelectionModel().getSelectedItem());
            setCardTypeMonster(monsterCardTypeChoiceBox.getSelectionModel().getSelectedItem());
        }  catch (Exception exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
            return;
        }
        api.submitThisMonster(selectedMonster)
                .whenComplete((res, ex)->{
                    if(ex == null)
                        new Alert(Alert.AlertType.INFORMATION, "Card was successfully created!").showAndWait();
                    exit();
                });
    }

    public void selectBaseMonster(Monster monster) throws LogicException {
        if(selectedMonster != null)
            throw new LogicException("if you want to select another monster you have to discard this one first!");
        selectedMonster = monster.clone();
    }

    private void checkCardIsSelected() throws LogicException {
        if(selectedMonster == null)
            throw new LogicException("no card is selected!");
    }

    public void setAttackDamage(int value) throws LogicException {
        checkCardIsSelected();
        if(value < 0)
            throw new LogicException("attack damage cannot be negative");
        if(value > 10000)
            throw new LogicException("attack damage is at most 10000");
        selectedMonster.setAttackDamage(value);
    }
    public void setDefenseRate(int value) throws LogicException {
        checkCardIsSelected();
        if(value < 0)
            throw new LogicException("defense rate cannot be negative");
        if(value > 10000)
            throw new LogicException("defense rate is at most 10000");
        selectedMonster.setDefenseRate(value);
    }
    public void setLevel(int level) throws LogicException {
        checkCardIsSelected();
        if(level < 1)
            throw new LogicException("level must be between 1 and 10");
        if(level > 10)
            throw new LogicException("level must be between 1 and 10");
        selectedMonster.setLevel(level);
    }
    public void setMonsterType (MonsterType type) throws LogicException {
        checkCardIsSelected();
        selectedMonster.setMonsterType(type);
    }
    public void setMonsterAttribute (MonsterAttribute attribute) throws LogicException {
        checkCardIsSelected();
        selectedMonster.setAttribute(attribute);
    }
    public void setCardTypeMonster(MonsterCardType cardType) throws LogicException {
        checkCardIsSelected();
        selectedMonster.setMonsterCardType(cardType);
    }
    public void setDescription(String description) throws LogicException {
        checkCardIsSelected();
        selectedMonster.setDescription(description);
    }
    public void setCardName(String name) throws LogicException {
        checkCardIsSelected();
        selectedMonster.setName(name);
    }

    @FXML
    private void exit() {
        try {
            selectedMonster = null;
        } catch (Exception ignored) {
        }
        CardFactoryMenuView.getInstance().run();
    }
}
