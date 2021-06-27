package YuGiOh.controller.menu;

import YuGiOh.controller.ProgramController;
import YuGiOh.model.User;
import YuGiOh.model.enums.Color;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.utils.RoutingException;
import YuGiOh.view.ScoreboardMenuView;
import lombok.Getter;

import java.util.ArrayList;

public class ScoreboardMenuController extends BaseMenuController {
    @Getter
    public static ScoreboardMenuController instance;
    private final User user;

    public ScoreboardMenuController(User user){
        this.view = new ScoreboardMenuView();
        this.user = user;
        instance = this;
    }

    public void showScoreboard() {
        ArrayList<User> users = User.retrieveUsersBasedOnScore();
        int rank = 1;
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (i > 0 && users.get(i - 1).getScore() > user.getScore())
                rank = i + 1;
            CustomPrinter.println(rank + "- " + user.getNickname() + ": " + user.getScore(), Color.Default);
        }
    }

    @Override
    public void exitMenu() throws RoutingException {
        ProgramController.getInstance().navigateToMenu(MainMenuController.class);
    }

    @Override
    public BaseMenuController getNavigatingMenuObject(Class<? extends BaseMenuController> menu) throws RoutingException {
        if (menu.equals(this.getClass()))
            throw new RoutingException("can't navigate to your current menu!");
        if (menu.equals(LoginMenuController.class))
            throw new RoutingException("you must logout for that!");
        if (menu.equals(MainMenuController.class))
            return MainMenuController.getInstance();
        throw new RoutingException("menu navigation is not possible");
    }

    @Override
    public String[] possibleNavigates() {
        return new String[]{"Main"};
    }
}