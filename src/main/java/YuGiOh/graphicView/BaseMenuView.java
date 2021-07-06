package YuGiOh.graphicView;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

public abstract class BaseMenuView {
    protected Stage stage;
    protected Scene scene;
    protected Pane root;

    public abstract void run();

    public static void relocateNodeFromCenter(Node node, double centerX, double centerY) {
        node.setLayoutX(node.getLayoutX() + (centerX - node.getBoundsInParent().getCenterX()));
        node.setLayoutY(node.getLayoutY() + (centerY - node.getBoundsInParent().getCenterY()));
    }

    public static void relocateFromLeft(Node node, double v1) {
        node.setLayoutX(v1);
    }

    public static void relocateFromRight(Node node, double v1) {
        node.setLayoutX(node.getLayoutX() + (node.getScene().getWidth() - v1 - node.getBoundsInParent().getMaxX()));
    }

    public static void relocateFromUp(Node node, double v2) {
        node.setLayoutY(v2);
    }

    public static void relocateFromDown(Node node, double v2) {
        node.setLayoutY(node.getLayoutY() + (node.getScene().getHeight() - v2 - node.getBoundsInParent().getMaxY()));
    }

    public static void rescale(Node node, double v1, double v2) {
        node.getTransforms().setAll(new Scale(v1, v2, 0, 0));
    }

    public static Node getTriangle(String direction, double size, Color color, EventHandler<? super MouseEvent> eventHandler) {
        Polygon polygon = new Polygon();
        if (direction.equalsIgnoreCase("left"))
            polygon.getPoints().addAll(0.0, size / 2,
                    size * Math.sqrt(3) / 2, 0.0,
                    size * Math.sqrt(3) / 2, size);
        else
            polygon.getPoints().addAll(size * Math.sqrt(3) / 2, size / 2,
                    0.0, 0.0,
                    0.0, size);
        polygon.setFill(color);
        polygon.setOnMouseClicked(eventHandler);
        return polygon;
    }
}
