package YuGiOh.graphicView;

import YuGiOh.Main;
import YuGiOh.graphicController.LoginMenuController;
import YuGiOh.graphicController.MainMenuController;
import YuGiOh.model.ModelException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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

    public void run() {
        renderScene();
        stage.setScene(scene);
        stage.show();
    }

    public void renderScene() {
        for (Node node : root.getChildren())
            if (node.getId().equals("mainPane"))
                mainPane = (Pane)node;
            else if (node.getId().equals("loginPane"))
                loginPane = (Pane)node;
            else if (node.getId().equals("registerPane"))
                registerPane = (Pane)node;
        mainPane.toFront();
        VBox registerVBox = (VBox)((HBox)((VBox)registerPane.getChildren().get(1)).getChildren().get(0)).getChildren().get(1);
        registerUsernameTextField = (TextField)registerVBox.getChildren().get(0);
        registerNicknameTextField = (TextField)registerVBox.getChildren().get(1);
        registerPasswordTextField = (TextField)registerVBox.getChildren().get(2);
        VBox loginVBox = (VBox)((HBox)((VBox)loginPane.getChildren().get(1)).getChildren().get(0)).getChildren().get(1);
        loginUsernameTextField = (TextField)loginVBox.getChildren().get(0);
        loginPasswordTextField = (TextField)loginVBox.getChildren().get(1);
        scene = new Scene(root);
        System.out.println(registerNicknameTextField.getText());
    }

    public void register(MouseEvent mouseEvent) {
        try {
            LoginMenuController.getInstance().createUser(registerUsernameTextField.getText(),
                    registerNicknameTextField.getText(), registerPasswordTextField.getText());
        } catch (ModelException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
            return;
        }
        new Alert(Alert.AlertType.INFORMATION, "user created successfully!").showAndWait();
        mainPane.toFront();
    }

    public void login(MouseEvent mouseEvent) {
        try {
            LoginMenuController.getInstance().login(loginUsernameTextField.getText(),
                    loginPasswordTextField.getText());
        } catch (ModelException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
            return;
        }
        new Alert(Alert.AlertType.INFORMATION, "user logged in successfully!").showAndWait();
        mainPane.toFront();
        MainMenuController.getInstance().start(stage);
    }

    public void loadRegisterMenu(MouseEvent mouseEvent) {
        registerPane.toFront();
    }

    public void loadLoginMenu(MouseEvent mouseEvent) {
        loginPane.toFront();
    }

    public void back(MouseEvent mouseEvent) {
        mainPane.toFront();
    }

    public void exit(MouseEvent mouseEvent) {
        System.exit(0);
    }
}
