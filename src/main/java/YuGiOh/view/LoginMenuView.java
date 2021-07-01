package YuGiOh.view;

import YuGiOh.controller.menu.LoginMenuController;
import YuGiOh.utils.DatabaseHandler;
import YuGiOh.utils.Debugger;
import YuGiOh.view.CommandLine.Command;

public class LoginMenuView extends BaseMenuView {
    public LoginMenuView() {
        super();
    }

    @Override
    protected void addCommands() {
        super.addCommands();
        this.cmd.addCommand(new Command(
                "debug",
                mp -> {
                    Debugger.setDebugMode(mp.get("mode"));
                },
                Options.mode(true)
        ));
        this.cmd.addCommand(new Command(
                "debug",
                mp -> {
                    Debugger.setCaptureMode(mp.get("capture"));
                },
                Options.captureMode(true)
        ));
        this.cmd.addCommand(new Command(
                "debug import test",
                mp -> {
                    Debugger.importTest(mp.get("file"), mp.get("count"));
                },
                Options.file(true),
                Options.count(false)
        ));
        this.cmd.addCommand(new Command(
                "debug",
                mp -> {
                    Debugger.setAutomaticSave(mp.get("automatic_save"));
                },
                Options.automatic_save(true)
        ));
        this.cmd.addCommand(new Command(
                "load from database",
                mp -> {
                    DatabaseHandler.loadFromDatabase(mp.get("file"));
                    System.out.println("successfully loaded from database");
                },
                Options.file(false)
        ));
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
        this.cmd.addCommand(new Command(
                "user cheat",
                mp -> {
                    LoginMenuController.getInstance().cheatLogin(mp.get("u"), mp.get("n"), mp.get("p"));
                },
                Options.username(true),
                Options.nickname(true),
                Options.password(true)
        ));
    }

    @Override
    public String getMenuName() {
        return "Login Menu";
    }
}
