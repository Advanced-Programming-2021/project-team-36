package YuGiOh.view.gui;

import YuGiOh.controller.GameController;
import YuGiOh.model.enums.Phase;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.Effect;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Stack;

public class PhaseLamps extends Pane {
    PhaseLamps(DoubleBinding widthProperty, DoubleBinding heightProperty, DoubleBinding leftProperty, DoubleBinding upProperty, DoubleBinding verticalDistanceProperty, Phase... phases) {
        minWidthProperty().bind(widthProperty);
        minHeightProperty().bind(heightProperty.multiply(phases.length).add(verticalDistanceProperty.multiply(phases.length-1)));
        layoutXProperty().bind(leftProperty);
        layoutYProperty().bind(upProperty);

        for (int i = 0; i < phases.length; i++) {
            Text text = new Text(phases[i].getShortName());
            text.setFont(Font.font(30));
            text.fillProperty().bind(Bindings
                    .when(GameController.getInstance().getGame().phaseProperty().isEqualTo(phases[i]))
                    .then(Color.RED)
                    .otherwise(Color.BLUE)
            );
            StackPane innerPane = new StackPane(text);
            innerPane.layoutYProperty().bind(heightProperty.add(verticalDistanceProperty).multiply(i));
            innerPane.setBackground(new Background(new BackgroundFill(Color.WHEAT, CornerRadii.EMPTY, Insets.EMPTY)));
            getChildren().add(innerPane);
        }
    }
}
