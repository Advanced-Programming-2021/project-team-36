package YuGiOh.controller.menu;

import YuGiOh.model.User;
import lombok.Getter;

public class MainMenuController extends BaseMenuController {
    @Getter
    public static MainMenuController instance;
    @Getter
    private final User user;

    public MainMenuController(User user){
        this.user = user;
        instance = this;
    }
}
