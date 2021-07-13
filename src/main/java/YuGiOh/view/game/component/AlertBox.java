package YuGiOh.view.game.component;

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
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class AlertBox {
    public static Stage stageGenerator(String title, double minWidth, double minHeight){
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setMinWidth(minWidth);
        stage.setMinHeight(minHeight);
        return stage;
    }

    public CompletableFuture<Integer> displayTextChoicesStandAlone(String question, List<String> texts) {
        return displayButtonChoicesStandAlone(question, texts.stream().map(str -> new CustomButton(str, 17, ()->{})).collect(Collectors.toList()));
    }

    public CompletableFuture<Integer> displayButtonChoicesStandAlone(String question, List<CustomButton> buttons) {
        Stage stage = stageGenerator("alert", 300, 100);
        CompletableFuture<Integer> ret = new CompletableFuture<>();
        Pane root = new Pane();
        Scene scene = new Scene(root);
        int counter = 0;
        for(CustomButton button : buttons){
            EventHandler<MouseEvent> onClickFunction = (EventHandler<MouseEvent>) button.getOnMouseClicked();
            final int finalCounter = counter;
            button.setOnMouseClicked(e->{ onClickFunction.handle(e); stage.close(); ret.complete(finalCounter);});
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
//        stage.showAndWait();

        stage.setOnCloseRequest(e-> ret.complete(-1));
        stage.show();
        // we don't wait it is async

        return ret;
    }

    public CompletableFuture<Boolean> displayYesNoStandAlone(String question, String yes, String no){
        ArrayList<CustomButton> buttons = new ArrayList<>();
        buttons.add(new CustomButton(yes, 20, ()->{}));
        buttons.add(new CustomButton(no, 20, ()->{}));
        return displayButtonChoicesStandAlone(question, buttons).thenApply(res -> res == 0);
    }

    public CompletableFuture<Void> displayMessageStandAlone(String message){
        ArrayList<CustomButton> buttons = new ArrayList<>();
        buttons.add(new CustomButton("Ok!", 20, ()->{}));
        return displayButtonChoicesStandAlone(message, buttons).thenAccept(res -> {});
    }


    public CompletableFuture<Integer> displayChoices(Pane parent, String question, List<String> choices) {
        CompletableFuture<Integer> ret = new CompletableFuture<>();
        List<CustomButton> buttons = new ArrayList<>();
        int counter = 0;
        for(String buttonText : choices){
            final int nowCounter = counter;
            counter++;
            buttons.add(new CustomButton(buttonText, 17, ()-> ret.complete(nowCounter)));
        }
        // can make it draggable pane
        Pane innerRoot = display(parent, question, buttons);
        innerRoot.translateXProperty().bind(parent.widthProperty().divide(2).add(-100));
        innerRoot.translateYProperty().bind(parent.heightProperty().divide(2).add(-100));
        innerRoot.minWidthProperty().bind(parent.widthProperty().divide(5));
        innerRoot.minHeightProperty().bind(parent.heightProperty().divide(5));
        return ret;
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
