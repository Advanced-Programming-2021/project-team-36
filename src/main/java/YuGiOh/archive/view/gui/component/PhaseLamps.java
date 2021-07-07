package YuGiOh.archive.view.gui.component;

import YuGiOh.controller.GameController;
import YuGiOh.model.enums.Phase;
import YuGiOh.archive.view.gui.GameMapLocation;
import YuGiOh.archive.view.gui.RatioLocation;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class PhaseLamps extends Pane {
    public PhaseLamps(GameField gameField, GameMapLocation mapLocation, Phase... phases) {
        for (Phase phase : phases) {
            RatioLocation location = mapLocation.getPhaseLocation(phase);
                Text text = new Text(phase.getShortName());
                text.setFont(Font.font(25));
                text.fillProperty().bind(Bindings
                        .when(GameController.getInstance().getGame().phaseProperty().isEqualTo(phase))
                        .then(Color.RED)
                        .otherwise(Color.BLUE)
                );
                StackPane innerPane = new StackPane(text);
                innerPane.layoutXProperty().bind(gameField.widthProperty().multiply(location.xRatio));
                innerPane.layoutYProperty().bind(gameField.heightProperty().multiply(location.yRatio));
                innerPane.setBackground(new Background(new BackgroundFill(Color.WHEAT, CornerRadii.EMPTY, Insets.EMPTY)));
                getChildren().add(innerPane);
        }
    }
}
