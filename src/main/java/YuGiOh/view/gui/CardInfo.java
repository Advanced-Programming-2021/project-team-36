package YuGiOh.view.gui;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class CardInfo extends StackPane {
    private SimpleObjectProperty<CardFrame> cardFrameProperty;
    private ImageView imageView;
    private Text text;
    private final DoubleBinding widthProperty, heightProperty;

    public CardInfo(DoubleBinding widthProperty, DoubleBinding heightProperty) {
        this.widthProperty = widthProperty;
        this.heightProperty = heightProperty;
        cardFrameProperty = new SimpleObjectProperty<>(null);
    }

    public void setCardFrame(CardFrame cardFrame) {
        if(cardFrame == null) {
            getChildren().clear();
            cardFrameProperty.set(null);
        } else {
            cardFrameProperty.set(cardFrame);
            imageView = new ImageView(cardFrame.getImage());
            imageView.fitWidthProperty().bind(widthProperty);
            imageView.fitHeightProperty().bind(heightProperty.multiply(0.8));
            text = new Text(cardFrame.getCard().getDescription());
            text.wrappingWidthProperty().bind(widthProperty.multiply(0.9));
            StackPane innerTextPane = new StackPane(text);
            StackPane outerTextPane = new StackPane(innerTextPane);
            outerTextPane.setBackground(
                    new Background(
                            new BackgroundFill(
                                    Color.WHEAT,
                                    new CornerRadii(0, 0, 0.2, 0.2, true),
                                    Insets.EMPTY)
                    )
            );
            outerTextPane.minHeightProperty().bind(innerTextPane.heightProperty().add(10));
            getChildren().addAll(new VBox(imageView, outerTextPane));
        }
    }
}
