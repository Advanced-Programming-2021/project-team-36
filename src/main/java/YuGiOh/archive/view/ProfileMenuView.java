package YuGiOh.archive.view;

import YuGiOh.archive.menu.ProfileMenuController;
import YuGiOh.archive.view.CommandLine.Command;

public class ProfileMenuView extends BaseMenuView {
    public ProfileMenuView() {
        super();
    }

    @Override
    protected void addCommands() {
        super.addCommands();
        this.cmd.addCommand(new Command(
                "profile change",
                mp -> {
                    ProfileMenuController.getInstance().changeNickname(mp.get("n"));
                },
                Options.nickname(true)
        ));
        this.cmd.addCommand(new Command(
                "profile change",
                mp -> {
                    ProfileMenuController.getInstance().changePassword(mp.get("current"), mp.get("new"));
                },
                Options.requirePassword(true),
                Options.currentPassword(true),
                Options.newPassword(true)
        ));
    }
    @Override
    public String getMenuName() {
        return "Profile Menu";
    }
}
