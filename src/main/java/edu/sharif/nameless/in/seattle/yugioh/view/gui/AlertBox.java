package edu.sharif.nameless.in.seattle.yugioh.view.gui;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class AlertBox {
    public static Stage stageGenerator(String title, double minWidth, double minHeight){
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setMinWidth(minWidth);
        stage.setMinHeight(minHeight);
        return stage;
    }


    public int displayChoicesStandAlone(String question, List<CustomButton> buttons) {
        Stage stage = stageGenerator("alert", 300, 100);
        // todo now if the user closes, the answer will be false. is it ok?
        AtomicInteger ret = new AtomicInteger(-1);
        Pane root = new Pane();
        Scene scene = new Scene(root);
        int counter = 0;
        for(CustomButton button : buttons){
            EventHandler<MouseEvent> onClickFunction = (EventHandler<MouseEvent>) button.getOnMouseClicked();
            final int finalCounter = counter;
            button.setOnMouseClicked(e->{ onClickFunction.handle(e); stage.close(); ret.set(finalCounter);});
            counter++;
        }
        Pane innerRoot = display(root, question, buttons);
        innerRoot.translateXProperty().unbind();
        innerRoot.translateYProperty().unbind();
        innerRoot.setTranslateX(0);
        innerRoot.setTranslateY(0);
        innerRoot.minWidthProperty().bind(scene.widthProperty());
        innerRoot.minHeightProperty().bind(scene.heightProperty());
        stage.setScene(scene);
        stage.showAndWait();
        return ret.get();
    }

    public boolean displayYesNoStandAlone(String question, String yes, String no){
        ArrayList<CustomButton> buttons = new ArrayList<>();
        buttons.add(new CustomButton(yes, 20, ()->{}));
        buttons.add(new CustomButton(no, 20, ()->{}));
        return displayChoicesStandAlone(question, buttons) == 1;
    }

    public Pane display(Pane parent, String question, Runnable yesRun, Runnable noRun){
        Font font = Font.font("Courier New", FontWeight.BOLD, 20);

        Button yes = new Button("yes");
        Button no = new Button("no");

        BorderPane root = new BorderPane();
        parent.getChildren().add(root);
        root.translateXProperty().bind(parent.widthProperty().divide(2).add(-100));
        root.translateYProperty().bind(parent.heightProperty().divide(2));

        yes.setOnMouseClicked(e->{ yesRun.run(); parent.getChildren().remove(root); });
        yes.setFont(font);
        yes.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));

        no.setOnMouseClicked(e->{ noRun.run(); parent.getChildren().remove(root); });
        no.setFont(font);
        no.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));

        root.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        root.setBottom(new HBox(yes, no));
        Label label = new Label(question);
        label.setFont(Font.font(15));
        root.setTop(label);
        return root;
    }

    public Pane display(Pane parent, String alert){
        Font font = Font.font("Courier New", FontWeight.BOLD, 20);

        Button ok = new Button("OK!");

        BorderPane root = new BorderPane();
        parent.getChildren().add(root);
        root.translateXProperty().bind(parent.widthProperty().divide(2).add(-100));
        root.translateYProperty().bind(parent.heightProperty().divide(2));

        ok.setOnMouseClicked(e->{ parent.getChildren().remove(root); });
        ok.setFont(font);
        ok.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        root.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        root.setBottom(new HBox(ok));
        Label label = new Label(alert);
        label.setFont(Font.font(15));
        root.setTop(label);
        return root;
    }

    public Pane display(Pane parent, String message, List<CustomButton> choices){
        BorderPane root = new BorderPane();
        parent.getChildren().add(root);
        root.translateXProperty().bind(parent.widthProperty().divide(2).add(-100));
        root.translateYProperty().bind(parent.heightProperty().divide(2));

        VBox vBox = new VBox();

        for(CustomButton button : choices){
            EventHandler<MouseEvent> onClickFunction = (EventHandler<MouseEvent>) button.getOnMouseClicked();
            button.setOnMouseClicked(e->{ onClickFunction.handle(e); parent.getChildren().remove(root); });
            button.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
            vBox.getChildren().add(button);
        }

        root.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        root.setBottom(vBox);
        Label label = new Label(message);
        label.setFont(Font.font(15));
        root.setTop(label);
        return root;
    }

}
