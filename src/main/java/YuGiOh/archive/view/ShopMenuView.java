package YuGiOh.archive.view;

import YuGiOh.archive.menu.ShopMenuController;
import YuGiOh.archive.view.CommandLine.Command;

public class ShopMenuView extends BaseMenuView {
    public ShopMenuView() {
        super();
    }

    @Override
    protected void addCommands() {
        super.addCommands();
        this.cmd.addCommand(new Command(
                "shop buy [cardName]",
                mp -> {
                    ShopMenuController.getInstance().buyCard(Parser.cardParser(mp.get("cardName")));
                }
        ));
        this.cmd.addCommand(new Command(
                "shop show",
                mp -> {
                    ShopMenuController.getInstance().showAll();
                },
                Options.all(true)
        ));
    }

    @Override
    public String getMenuName() {
        return "Shop Menu";
    }
}
