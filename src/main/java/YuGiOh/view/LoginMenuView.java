package YuGiOh.view;

import YuGiOh.MainApplication;
import YuGiOh.controller.menus.LoginMenuController;
import YuGiOh.model.ModelException;
import YuGiOh.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class LoginMenuView extends BaseMenuView {
    private static final String backgroundImageAddress = "assets/Backgrounds/GUI_T_TowerBg3.dds.png";
    private static LoginMenuView instance;

    @FXML
    private ImageView backgroundImageView;
    @FXML
    private TextField registerUsernameTextField, registerPasswordTextField, registerNicknameTextField,
            loginUsernameTextField, loginPasswordTextField;
    @FXML
    private Pane mainPane, loginPane, registerPane;

    public LoginMenuView() {
        instance = this;
    }

    public static LoginMenuView getInstance() {
        if (instance == null)
            instance = new LoginMenuView();
        return instance;
    }

    public static void init(Stage primaryStage) {
        try {
            Pane root = FXMLLoader.load(MainApplication.class.getResource("/fxml/LoginMenu.fxml"));
            LoginMenuView.getInstance().start(primaryStage, root);
        } catch (IOException ignored) {
        }
    }

    public void start(Stage primaryStage, Pane root) {
        this.root = root;
        this.stage = primaryStage;
        new LoginMenuController();
        scene.setRoot(root);
        try {
            backgroundImageView.setImage(new Image(new FileInputStream(backgroundImageAddress)));
            backgroundImageView.toBack();
        } catch (FileNotFoundException ignored) {
        }
        run();
    }

    public void run() {
        backgroundImageView.toFront();
        mainPane.toFront();
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

    public void register() {
        try {
            LoginMenuController.getInstance().createUser(registerUsernameTextField.getText(), registerNicknameTextField.getText(),
                    registerPasswordTextField.getText());
        } catch (ModelException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
            return;
        }
        new Alert(Alert.AlertType.INFORMATION, "user created successfully!").showAndWait();
        backgroundImageView.toFront();
        mainPane.toFront();
    }

    public void login() {
        try {
            User user = LoginMenuController.getInstance().login(loginUsernameTextField.getText(),
                    loginPasswordTextField.getText());
            new Alert(Alert.AlertType.INFORMATION, "user logged in successfully!").showAndWait();
            backgroundImageView.toFront();
            mainPane.toFront();
            MainMenuView.init(stage, user);
        } catch (ModelException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
            return;
        }
    }

    public void loadRegisterMenu() {
        registerUsernameTextField.clear();
        registerNicknameTextField.clear();
        registerPasswordTextField.clear();
        backgroundImageView.toFront();
        registerPane.toFront();
    }

    public void loadLoginMenu() {
        loginUsernameTextField.clear();
        loginPasswordTextField.clear();
        backgroundImageView.toFront();
        loginPane.toFront();
    }

    public void back() {
        backgroundImageView.toFront();
        mainPane.toFront();
    }

    public void exit() {
        System.exit(0);
    }
}
