package YuGiOh.archive.view.gui.component;

import YuGiOh.controller.menus.DuelMenuController;
import YuGiOh.model.Game;
import YuGiOh.model.Player.Player;
import YuGiOh.archive.view.gui.Utils;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class DuelInfoBox extends BorderPane {
    private CardInfo cardInfo;

    public void init(GameField gameField, Game game) {
        setBackground(new Background(new BackgroundImage(
                new Image(Utils.getAsset("Texture/wood.png").toURI().toString()),
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT
        )));

        cardInfo = new CardInfo();
        cardInfo.prefWidthProperty().bind(widthProperty().multiply(1));
        cardInfo.prefHeightProperty().bind(heightProperty().multiply(0.6));

        setTop(getLifeBar(game.getSecondPlayer()));
        setBottom(getLifeBar(game.getFirstPlayer()));
        setCenter(cardInfo);
    }

    private LifeBar getLifeBar(Player player) {
        LifeBar lifeBar = new LifeBar(player);
        lifeBar.minWidthProperty().bind(widthProperty().multiply(0.9));
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
