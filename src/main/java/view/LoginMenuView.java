package view;

import controller.LoginMenuController;
import view.CommandLine.Command;

public class LoginMenuView extends BaseMenuView {
    public LoginMenuView() {
        super();
    }

    @Override
    protected void addCommands() {
        super.addCommands();
        this.cmd.addCommand(new Command(
                "user login",
                mp -> {
                    LoginMenuController.getInstance().login(mp.get("u"), mp.get("p"));
                },
                Options.username(true),
                Options.password(true)
        ));
        this.cmd.addCommand(new Command(
                "user create",
                mp -> {
                    LoginMenuController.getInstance().createUser(mp.get("u"), mp.get("n"), mp.get("p"));
                },
                Options.username(true),
                Options.nickname(true),
                Options.password(true)
        ));
    }

    @Override
    protected String getMenuName() {
        return "Login Menu";
    }
}
