package YuGiOh.view.gui;

import YuGiOh.controller.menu.DuelMenuController;
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
    private final ImageView imageView;
    private final VBox buttonBox;
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

        imageView = new ImageView();
        buttonBox = new VBox();
        imageView.fitWidthProperty().bind(widthProperty.add(-10));
        imageView.fitHeightProperty().bind(heightProperty.divide(2));

        CustomButton nextPhaseButton = new CustomButton("next phase", 23, ()->
                gameField.runAndAlert(()-> DuelMenuController.getInstance().goNextPhase(), ()->{})
        );
        CustomButton surrenderButton = new CustomButton("surrender", 23, ()->
                gameField.runAndAlert(()-> DuelMenuController.getInstance().surrender(), ()->{})
        );

        BorderPane insideBorderPane = new BorderPane();

        insideBorderPane.setBottom(new VBox(nextPhaseButton, surrenderButton));
        insideBorderPane.setTop(imageView);
        insideBorderPane.setCenter(buttonBox);

        Text currentPhase = new Text(game.getPhase().getVerboseName());
        currentPhase.setFont(Font.font(30));
        currentPhase.setFill(Color.BLACK);
        // todo I don't know why binding the text does not work. maybe because phase changes too fast?
        game.phaseProperty().addListener((observable -> {
            Platform.runLater(()-> {
                currentPhase.setText(((SimpleObjectProperty<Phase>) observable).getValue().getVerboseName());
            });
        }));
        insideBorderPane.setRight(currentPhase);


        setTop(new LifeBar(game.getSecondPlayer(), widthProperty));
        setCenter(insideBorderPane);
        setBottom(new LifeBar(game.getFirstPlayer(), widthProperty));
    }

    public void addInfo(Image image, CustomButton... buttons){
        Platform.runLater(()-> {
            for (CustomButton button : buttons) {
                buttonBox.getChildren().add(button);
            }
            imageView.setImage(image);
        });
    }
    public void clear(){
        Platform.runLater(()-> {
            buttonBox.getChildren().clear();
            imageView.setImage(null);
        });
    }
}
