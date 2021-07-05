package YuGiOh.view.gui.component;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

public class GameNavBar extends Pane {
    private final HBox items;

    public GameNavBar() {
        setBackground(new Background(new BackgroundFill(
                new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, new Stop(0, Color.LIGHTBLUE), new Stop(1, Color.SILVER)),
                CornerRadii.EMPTY,
                Insets.EMPTY
        )));
        items = new HBox();
        getChildren().add(items);
    }
    public void addItem(Node node) {
        items.getChildren().add(node);
    }
}
