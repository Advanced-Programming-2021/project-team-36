package edu.sharif.nameless.in.seattle.yugioh.view.gui;

import edu.sharif.nameless.in.seattle.yugioh.controller.menu.DuelMenuController;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;

public class DuelInfoBox extends BorderPane {
    DoubleBinding widthProperty, heightProperty;
    ImageView imageView;
    VBox buttonBox;

    public DuelInfoBox(DoubleBinding widthProperty, DoubleBinding heightProperty){
        this.widthProperty = widthProperty;
        this.heightProperty = heightProperty;
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

        CustomButton nextPhaseButton = new CustomButton("next phase", 23, ()->{
            DuelMenuController.getInstance().goNextPhase();
        }).setNewShape(new Rectangle());

        CustomButton surrenderButton = new CustomButton("surrender", 23, ()->{
            DuelMenuController.getInstance().surrender();
        });

        setBottom(new VBox(nextPhaseButton, surrenderButton));
        setTop(imageView);
        setCenter(buttonBox);
    }

    public void addInfo(Image image, CustomButton... buttons){
        for(CustomButton button : buttons){
            buttonBox.getChildren().add(button);
        }
        imageView.setImage(image);
    }
    public void clear(){
        buttonBox.getChildren().clear();
        imageView.setImage(null);
    }
}
