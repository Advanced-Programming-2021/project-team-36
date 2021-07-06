package YuGiOh.graphicView;

import YuGiOh.Main;
import YuGiOh.graphicController.LoginMenuController;
import YuGiOh.model.ModelException;
import YuGiOh.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginMenuView extends BaseMenuView {
    private static LoginMenuView instance;

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
            Pane root = FXMLLoader.load(Main.class.getResource("/fxml/LoginMenu.fxml"));
            LoginMenuView.getInstance().start(primaryStage, root);
        } catch (IOException ignored) {
        }
    }

    public void start(Stage primaryStage, Pane root) {
        this.root = root;
        this.stage = primaryStage;
        new LoginMenuController();
        run();
    }

    public void run() {
        renderScene();
        stage.setScene(scene);
        stage.show();
    }

    public void renderScene() {
        mainPane.toFront();
        if (scene == null)
            scene = new Scene(root);
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
        mainPane.toFront();
    }

    public void login() {
        try {
            User user = LoginMenuController.getInstance().login(loginUsernameTextField.getText(),
                    loginPasswordTextField.getText());
            new Alert(Alert.AlertType.INFORMATION, "user logged in successfully!").showAndWait();
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
        registerPane.toFront();
    }

    public void loadLoginMenu() {
        loginUsernameTextField.clear();
        loginPasswordTextField.clear();
        loginPane.toFront();
    }

    public void back() {
        mainPane.toFront();
    }

    public void exit() {
        System.exit(0);
    }
}
