package edu.sharif.nameless.in.seattle.yugioh.view.gui;

import edu.sharif.nameless.in.seattle.yugioh.model.Player.Player;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.Constants;
import javafx.beans.binding.Binding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class LifeBar extends StackPane {
    public LifeBar(Player player, DoubleBinding widthProperty){
        Text text = new Text(player.getUser().getUsername());
        text.setFont(Font.font(30));
        LpVisualizer lpVisualizer = new LpVisualizer(player.lifePointProperty(), widthProperty.multiply(0.9));
        getChildren().add(new VBox(text, lpVisualizer));
    }
}

class LpVisualizer extends StackPane {
    LpVisualizer(IntegerProperty lpValue, DoubleBinding widthProperty){
        minWidthProperty().bind(widthProperty);
        setMinHeight(30);
        widthProperty = widthProperty().multiply(1);

        Pane outer = new Pane();
        outer.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(1))));
        getChildren().add(outer);
        outer.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(10), Insets.EMPTY)));

        Rectangle inner = new Rectangle();
        inner.heightProperty().bind(heightProperty().multiply(0.6));
        inner.yProperty().bind(heightProperty().multiply(0.2));
        inner.xProperty().bind(widthProperty.multiply(0.07));
        DoubleBinding totalLifeLength = widthProperty.multiply(0.86);
        inner.widthProperty().bind(totalLifeLength.multiply(lpValue).divide(Constants.InitialLifePoint.val));
        ObjectProperty<Paint> interactivePaint = new SimpleObjectProperty<>();
        inner.fillProperty().bind(interactivePaint);
        interactivePaint.set(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.RED), new Stop(1, Color.BLUE)));
//        lpValue.addListener(e->{
            // todo we can change color from here
//        });

        outer.getChildren().add(inner);

        Text text = new Text();
        text.textProperty().bind(lpValue.asString());
        text.yProperty().bind(heightProperty().multiply(0.7));
        text.xProperty().bind(widthProperty.multiply(0.07).add(20));
        text.setFill(Color.WHEAT);
        outer.getChildren().add(text);
    }
}
