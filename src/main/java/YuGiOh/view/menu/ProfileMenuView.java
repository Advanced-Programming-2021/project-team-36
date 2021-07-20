package YuGiOh.view.menu;

import YuGiOh.ClientApplication;
import YuGiOh.api.ProfileMenuApi;
import YuGiOh.network.ClientConnection;
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

    private ProfileMenuApi api;

    public ProfileMenuView() {
        instance = this;
    }

    public static ProfileMenuView getInstance() {
        if (instance == null)
            instance = new ProfileMenuView();
        return instance;
    }

    public static void init(Stage primaryStage) {
        try {
            Pane root = FXMLLoader.load(ClientApplication.class.getResource("/fxml/ProfileMenu.fxml"));
            ProfileMenuView.getInstance().start(primaryStage, root);
        } catch (IOException ignored) {
        }
    }

    public void start(Stage primaryStage, Pane root) {
        this.stage = primaryStage;
        this.root = root;
        scene.setRoot(root);
        try {
            this.api = new ProfileMenuApi(ClientConnection.getOrCreateInstance());
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "check your connection to server and retry!").showAndWait();
            LoginMenuView.init(stage);
            return;
        }
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
        api.getUserFromServer().thenAccept(user->{
            usernameLabel.setText("  Username: " + user.getUsername() + "  ");
            nicknameLabel.setText("  Nickname: " + user.getNickname() + "  ");
            profilePicture.setImage(user.getProfilePicture());
        });
    }

    @FXML
    private void changeNickname() {
        api.changeNickname(newNicknameTextField.getText())
                .whenComplete((res, ex)->{
                    if(ex == null) {
                        api.getUserFromServer().thenAccept(user->{
                            new Alert(Alert.AlertType.INFORMATION, "Nickname changed successfully!").showAndWait();
                            nicknameLabel.setText("Nickname: " + user.getNickname());
                        });
                    } else {
                        new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
                    }
                });
    }

    @FXML
    private void changePassword() {
        api.changePassword(oldPasswordTextField.getText(), newPasswordTextField.getText())
                .whenComplete((res, ex)-> {
                    if(ex == null) {
                        api.getUserFromServer().thenAccept(user->{
                            new Alert(Alert.AlertType.INFORMATION, "password changed successfully!").showAndWait();
                        });
                    } else {
                        new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
                    }
                });
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
