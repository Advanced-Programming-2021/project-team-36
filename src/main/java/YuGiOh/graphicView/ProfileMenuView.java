package YuGiOh.graphicView;

import YuGiOh.Main;
import YuGiOh.graphicController.LoginMenuController;
import YuGiOh.graphicController.MainMenuController;
import YuGiOh.graphicController.ProfileMenuController;
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

public class ProfileMenuView extends BaseMenuView {
    private static ProfileMenuView instance;

    @FXML
    private TextField registerUsernameTextField, registerPasswordTextField, registerNicknameTextField;
    private TextField loginUsernameTextField, loginPasswordTextField;
    @FXML
    private Pane mainPane, loginPane, registerPane;

    public ProfileMenuView() {
        instance = this;
    }

    public static ProfileMenuView getInstance() {
        if (instance == null)
            instance = new ProfileMenuView();
        return instance;
    }

    public void start(Stage primaryStage) {
        stage = primaryStage;
        //new MainMenuController();
        run();
    }

    public void run() {
        renderScene();
        stage.setScene(scene);
        stage.show();
    }

    public void renderScene() {
        try {
            Pane root = FXMLLoader.load(Main.class.getResource("/fxml/MainMenu.fxml"));
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
        } catch (IOException ignored) {
            System.out.println(ignored);
            ignored.printStackTrace();
        }
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
            LoginMenuController.getInstance().login(registerUsernameTextField.getText(),
                    registerPasswordTextField.getText());
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

    public void loadProfileMenu(MouseEvent mouseEvent) {
        new ProfileMenuController(MainMenuController.getInstance().getUser()).start(stage);
    }

    public void logout(MouseEvent mouseEvent) {
        MainMenuController.getInstance().logout();
    }
}
