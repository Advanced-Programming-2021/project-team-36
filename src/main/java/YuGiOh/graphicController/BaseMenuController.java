package YuGiOh.graphicController;

import YuGiOh.utils.RoutingException;
import YuGiOh.graphicView.BaseMenuView;
import javafx.stage.Stage;
import lombok.Getter;

public abstract class BaseMenuController {
    abstract public void exitMenu() throws RoutingException;
    abstract public BaseMenuController getNavigatingMenuObject(Class<? extends BaseMenuController> menu) throws RoutingException;

    @Getter
    protected BaseMenuView view;

}
