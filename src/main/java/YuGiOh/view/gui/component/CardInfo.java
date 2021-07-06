package YuGiOh.view.gui.component;

import YuGiOh.view.gui.component.CardFrame;
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

    public CardInfo() {
        cardFrameProperty = new SimpleObjectProperty<>(null);
    }

    public void setCardFrame(CardFrame cardFrame) {
        if(cardFrame == null || !cardFrame.isFacedUp()) {
            getChildren().clear();
            cardFrameProperty.set(null);
        } else {
            cardFrameProperty.set(cardFrame);
            imageView = new ImageView(cardFrame.getImage());
            imageView.fitWidthProperty().bind(widthProperty());
            imageView.setPreserveRatio(true);
            text = new Text(cardFrame.getCard().getDescription());
            text.wrappingWidthProperty().bind(widthProperty().multiply(0.9));
            StackPane innerTextPane = new StackPane(text);
            StackPane outerTextPane = new StackPane(innerTextPane);
            outerTextPane.setBackground(
                    new Background(
                            new BackgroundFill(
                                    Color.WHEAT,
                                    new CornerRadii(0, 0, 0.2, 0.2, true),
                                    Insets.EMPTY
                            )
                    )
            );
            outerTextPane.setPadding(new Insets(0, 0, 20, 0));
            getChildren().addAll(new VBox(imageView, outerTextPane));
        }
    }
}
