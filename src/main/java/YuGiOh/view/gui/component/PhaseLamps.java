package YuGiOh.view.gui.component;

import YuGiOh.controller.GameController;
import YuGiOh.model.enums.Phase;
import YuGiOh.view.gui.RatioLocation;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Map;

public class PhaseLamps extends Pane {
    public PhaseLamps(DoubleBinding widthProperty, DoubleBinding heightProperty, Map<Phase, RatioLocation> phaseToLocation) {
        phaseToLocation.forEach((phase, location) -> {
            Text text = new Text(phase.getShortName());
            text.setFont(Font.font(30));
            text.fillProperty().bind(Bindings
                    .when(GameController.getInstance().getGame().phaseProperty().isEqualTo(phase))
                    .then(Color.RED)
                    .otherwise(Color.BLUE)
            );
            StackPane innerPane = new StackPane(text);
            innerPane.layoutXProperty().bind(widthProperty.multiply(location.xRatio));
            innerPane.layoutYProperty().bind(heightProperty.multiply(location.yRatio));
            innerPane.setBackground(new Background(new BackgroundFill(Color.WHEAT, CornerRadii.EMPTY, Insets.EMPTY)));
            getChildren().add(innerPane);
        });
    }
}
