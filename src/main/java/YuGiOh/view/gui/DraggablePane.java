package YuGiOh.view.gui;

import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.layout.Pane;

abstract public class DraggablePane extends Pane {
    private double mouseDifX, mouseDifY;

    public DraggablePane(){
        setOnMousePressed(e->{
            mouseDifX = getTranslateX() - e.getSceneX();
            mouseDifY = getTranslateY() - e.getSceneY();
            setCursor(Cursor.MOVE);
        });
        setOnMouseDragged(e->{
            onDrag();
            moveByTranslateValue(e.getSceneX() + mouseDifX, e.getSceneY() + mouseDifY);
        });
        setOnMouseReleased(e->{
            Bounds bounds = getBoundsInParent();
            onDrop(bounds);
            setCursor(Cursor.HAND);
            moveByTranslateValue(0, 0);
        });
    }

    protected void moveByTranslateValue(double x, double y){
        setTranslateX(x);
        setTranslateY(y);
    }

    abstract void onDrop(Bounds bounds);
    abstract void onDrag();
}
