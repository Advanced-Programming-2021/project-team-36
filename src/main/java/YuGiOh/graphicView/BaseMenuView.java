package YuGiOh.graphicView;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public abstract class BaseMenuView {
    protected Stage stage;
    protected Scene scene;
    protected Pane root;

    public abstract void run();

    public static void relocateNodeFromCenter(Node node, double centerX, double centerY) {
        node.setLayoutX(node.getLayoutX() +
                (centerX - node.getBoundsInParent().getCenterX()));
        node.setLayoutY(node.getLayoutY() +
                (centerY - node.getBoundsInParent().getCenterY()));
    }
}
