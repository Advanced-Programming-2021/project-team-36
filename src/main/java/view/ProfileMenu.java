package view;

import view.CommandLine.Command;

import java.util.Scanner;

public class ProfileMenu extends BaseMenu {
    ProfileMenu(Scanner scanner){
        super(scanner);
        this.cmd.addCommand(new Command(
                "profile change",
                mp -> {
                    controller.ProfileMenu.changeNickname(mp.get("nickname"));
                },
                Options.nickname(true)
        ));
        this.cmd.addCommand(new Command(
                "profile change",
                mp -> {
                    controller.ProfileMenu.changePassword(mp.get("current"), mp.get("new"));
                },
                Options.requirePassword(true),
                Options.currentPassword(true),
                Options.newPassword(true)
        ));

    }
}
