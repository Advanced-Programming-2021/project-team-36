package view;

import Utils.Parser;
import view.CommandLine.Command;

import java.util.Scanner;

public class ShopMenu extends BaseMenu {
    ShopMenu(Scanner scanner){
        super(scanner);
        this.cmd.addCommand(new Command(
                "shop buy [cardName]",
                mp -> {
                    controller.ShopMenu.buyCard(Parser.cardParser(mp.get("cardName")));
                }
        ));
        this.cmd.addCommand(new Command(
                "shop show",
                mp -> {
                    controller.ShopMenu.shopAll();
                },
                Options.all(true)
        ));
    }
}
