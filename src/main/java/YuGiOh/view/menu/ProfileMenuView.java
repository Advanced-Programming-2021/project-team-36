package YuGiOh.view.menu;

import YuGiOh.MainApplication;
import YuGiOh.controller.menu.*;
import YuGiOh.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ProfileMenuView extends BaseMenuView {
    private static final String backgroundImageAddress = "assets/Backgrounds/GUI_T_TowerBg1.dds.png";
    private static ProfileMenuView instance;

    @FXML
    private ImageView backgroundImageView;
    @FXML
    private ImageView profilePicture;
    @FXML
    private Label usernameLabel, nicknameLabel;
    @FXML
    private TextField newNicknameTextField, newPasswordTextField, oldPasswordTextField;
    @FXML
    private Node mainPane, passwordPane, nicknamePane;

    public ProfileMenuView() {
        instance = this;
    }

    public static ProfileMenuView getInstance() {
        if (instance == null)
            instance = new ProfileMenuView();
        return instance;
    }

    public static void init(Stage primaryStage, User user) {
        try {
            Pane root = FXMLLoader.load(MainApplication.class.getResource("/fxml/ProfileMenu.fxml"));
            ProfileMenuView.getInstance().start(primaryStage, root, user);
        } catch (IOException ignored) {
        }
    }

    public void start(Stage primaryStage, Pane root, User user) {
        this.stage = primaryStage;
        this.root = root;
        scene.setRoot(root);
        new ProfileMenuController(user);
        try {
            backgroundImageView.setImage(new Image(new FileInputStream(backgroundImageAddress)));
            backgroundImageView.toBack();
        } catch (FileNotFoundException ignored) {
        }
        run();
    }

    public void run() {
        renderScene();
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
        relocateNodeFromCenter(mainPane, root.getWidth() / 2, root.getHeight() * 0.5);
        relocateNodeFromCenter(nicknamePane, root.getWidth() / 2, root.getHeight() * 0.5);
        relocateNodeFromCenter(passwordPane, root.getWidth() / 2, root.getHeight() * 0.5);
        back();
    }

    private void renderScene() {
        usernameLabel.setText("  Username: " + ProfileMenuController.getInstance().getUser().getUsername() + "  ");
        nicknameLabel.setText("  Nickname: " + ProfileMenuController.getInstance().getUser().getNickname() + "  ");
        profilePicture.setImage(ProfileMenuController.getInstance().getUser().getProfilePicture());

    }

    @FXML
    private void changeNickname() {
        try {
            ProfileMenuController.getInstance().changeNickname(newNicknameTextField.getText());
            new Alert(Alert.AlertType.INFORMATION, "Nickname changed successfully!").showAndWait();
            nicknameLabel.setText("Nickname: " + ProfileMenuController.getInstance().getUser().getNickname());
        } catch (Exception exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
        }
    }

    @FXML
    private void changePassword() {
        try {
            ProfileMenuController.getInstance().changePassword(oldPasswordTextField.getText(), newPasswordTextField.getText());
            new Alert(Alert.AlertType.INFORMATION, "password changed successfully!").showAndWait();
        } catch (Exception exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
        }
    }

    @FXML
    private void loadNicknameChangePane() {
        nicknamePane.setOpacity(1);
        mainPane.setOpacity(0);
        passwordPane.setOpacity(0);
        nicknamePane.toFront();
    }

    @FXML
    private void loadPasswordChangePane() {
        passwordPane.setOpacity(1);
        mainPane.setOpacity(0);
        nicknamePane.setOpacity(0);
        passwordPane.toFront();
    }

    @FXML
    private void back() {
        mainPane.setOpacity(1);
        nicknamePane.setOpacity(0);
        passwordPane.setOpacity(0);
        mainPane.toFront();
    }

    @FXML
    private void exit() {
        MainMenuView.getInstance().run();
    }
}
