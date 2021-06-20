package YuGiOh.graphicView;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public abstract class BaseMenuView {
    protected Stage stage;
    protected Scene scene;
    protected Pane root;
    public void start(Stage primaryStage, Pane root) {
        this.stage = primaryStage;
        this.root = root;
        run();
    }

    public abstract void run();

}
