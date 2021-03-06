package YuGiOh.view.game.component;

import YuGiOh.model.Player.Player;
import YuGiOh.model.enums.Constants;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class LifeBar extends Pane {
    public LifeBar(Player player){
        ImageView imageView = new ImageView(player.getUser().getProfilePicture());
        Text text = new Text(player.getUser().getUsername());
        text.setFont(Font.font(25));
        text.autosize();
        imageView.setPreserveRatio(true);
        imageView.fitWidthProperty().bind(widthProperty().multiply(0.5));
        LpVisualizer lpVisualizer = new LpVisualizer(player.lifePointProperty());
        lpVisualizer.prefWidthProperty().bind(widthProperty().multiply(0.4));
        VBox vBox = new VBox(lpVisualizer, imageView, text);
        vBox.prefWidthProperty().bind(widthProperty());
        getChildren().add(vBox);
    }
}

class LpVisualizer extends Pane {
    LpVisualizer(IntegerProperty lpValue){
        setMinHeight(30);
        Pane outer = new Pane();
        outer.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(1))));
        getChildren().add(outer);
        outer.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(10), Insets.EMPTY)));

        Rectangle inner = new Rectangle();
        inner.heightProperty().bind(heightProperty().multiply(0.6));
        inner.yProperty().bind(heightProperty().multiply(0.2));
        inner.xProperty().bind(widthProperty().multiply(0.07));
        DoubleBinding totalLifeLength = widthProperty().multiply(0.86);
        inner.widthProperty().bind(totalLifeLength.multiply(lpValue).divide(Constants.InitialLifePoint.val));
        ObjectProperty<Paint> interactivePaint = new SimpleObjectProperty<>();
        inner.fillProperty().bind(interactivePaint);
        interactivePaint.set(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.RED), new Stop(1, Color.BLUE)));
        outer.getChildren().add(inner);

        Text text = new Text();
        text.textProperty().bind(lpValue.asString());
        text.yProperty().bind(heightProperty().multiply(0.7));
        text.xProperty().bind(widthProperty().multiply(0.07).add(20));
        text.setFill(Color.WHEAT);
        outer.getChildren().add(text);
    }
}
