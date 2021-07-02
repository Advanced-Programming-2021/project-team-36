package YuGiOh.view.gui;

import YuGiOh.controller.menu.DuelMenuController;
import YuGiOh.model.CardAddress;
import YuGiOh.model.Game;
import YuGiOh.model.enums.Phase;
import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import lombok.Setter;

public class DuelInfoBox extends BorderPane {
    private final DoubleBinding widthProperty, heightProperty;
    private final CardInfo cardInfo;
    private final Game game;

    @Setter
    private GameField gameField;

    public DuelInfoBox(Game game, DoubleBinding widthProperty, DoubleBinding heightProperty){
        this.widthProperty = widthProperty;
        this.heightProperty = heightProperty;
        this.game = game;
        setBackground(new Background(new BackgroundImage(
                new Image(Utils.getAsset("Texture/wood.png").toURI().toString()),
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT
        )));

        ImageView background = new ImageView(new Image(Utils.getAsset("Texture/wood.png").toURI().toString()));
        background.fitWidthProperty().bind(widthProperty);
        background.fitHeightProperty().bind(heightProperty);

        minWidthProperty().bind(widthProperty);
        minHeightProperty().bind(heightProperty);

        cardInfo = new CardInfo(widthProperty, heightProperty.multiply(0.55));

        CustomButton nextPhaseButton = new CustomButton("next phase", 23, ()->
                gameField.addRunnableToMainThread(()-> DuelMenuController.getInstance().goNextPhase())
        );
        CustomButton surrenderButton = new CustomButton("surrender", 23, ()->
                gameField.addRunnableToMainThread(()-> DuelMenuController.getInstance().surrender())
        );

        BorderPane insideBorderPane = new BorderPane();

        insideBorderPane.setBottom(new VBox(nextPhaseButton, surrenderButton));
        insideBorderPane.setCenter(cardInfo);

        setTop(new LifeBar(game.getSecondPlayer(), widthProperty));
        setCenter(insideBorderPane);
        setBottom(new LifeBar(game.getFirstPlayer(), widthProperty));
    }

    public void addInfo(CardFrame cardFrame){
        Platform.runLater(()-> {
            cardInfo.setCardFrame(cardFrame);
        });
    }
    public void clear(){
        Platform.runLater(()-> {
            cardInfo.setCardFrame(null);
        });
    }
}
