package YuGiOh.view.gui.component;

import YuGiOh.graphicController.DuelMenuController;
import YuGiOh.model.Game;
import YuGiOh.model.Player.Player;
import YuGiOh.view.gui.Utils;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import lombok.Setter;

public class DuelInfoBox extends BorderPane {
    private CardInfo cardInfo;

    private GameField gameField;

    public void init(GameField gameField, Game game) {
        setBackground(new Background(new BackgroundImage(
                new Image(Utils.getAsset("Texture/wood.png").toURI().toString()),
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT
        )));

        cardInfo = new CardInfo();
        cardInfo.prefWidthProperty().bind(widthProperty());
        cardInfo.prefHeightProperty().bind(heightProperty().multiply(0.55));

        CustomButton nextPhaseButton = new CustomButton("next phase", 23, ()->
                gameField.addRunnableToMainThread(()-> DuelMenuController.getInstance().goNextPhase())
        );
        CustomButton surrenderButton = new CustomButton("surrender", 23, ()->
                gameField.addRunnableToMainThread(()-> DuelMenuController.getInstance().surrender())
        );

        BorderPane insideBorderPane = new BorderPane();

        insideBorderPane.setBottom(new VBox(nextPhaseButton, surrenderButton));
        insideBorderPane.setCenter(cardInfo);

        setTop(getLifeBar(game.getSecondPlayer()));
        setCenter(insideBorderPane);
        setBottom(getLifeBar(game.getFirstPlayer()));
    }

    private LifeBar getLifeBar(Player player) {
        LifeBar lifeBar = new LifeBar(player);
        lifeBar.prefWidthProperty().bind(widthProperty());
        return lifeBar;
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
