package YuGiOh.view;

import YuGiOh.controller.GameController;
import YuGiOh.controller.menu.*;
import YuGiOh.model.CardAddress;
import YuGiOh.model.Player.Player;
import YuGiOh.model.User;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Utils;
import YuGiOh.model.enums.Color;
import YuGiOh.model.enums.MonsterState;
import YuGiOh.model.enums.ZoneType;
import YuGiOh.controller.menu.*;
import YuGiOh.utils.CustomPrinter;

public class Parser {
    public static User UserParser(String username) throws ParserException {
        User user = User.getUserByUsername(username);
        if (user == null) {
            throw new ParserException("there is no user with this username!");
        }
        return user;
    }

    public static Integer IntegerParser(String number) throws ParserException {
        try {
            return Integer.parseInt(number);
        } catch (Exception e) {
            throw new ParserException("invalid number");
        }
    }

    public static int RoundParser(String number) throws ParserException {
        if (number.equals("1"))
            return 1;
        if (number.equals("3"))
            return 3;
        throw new ParserException("number of rounds is not supported");
    }

    public static CardAddress cardAddressParser(String zoneName, String idString, boolean opponent) throws ParserException {
        ZoneType zone = ZoneType.getZoneByName(zoneName);
        if (zone == null)
            throw new ParserException("invalid selection");
        try {
            int id = IntegerParser(idString);
            Player owner = opponent ? GameController.getInstance().getGame().getOpponentPlayer() : GameController.getInstance().getGame().getCurrentPlayer();
            return new CardAddress(zone, id, owner);
        } catch (Exception ignored) {
            throw new ParserException("invalid selection");
        }
    }

    public static MonsterState cardStateParser(String state) throws ParserException {
        MonsterState ret = MonsterState.getOccupiedStateByName(state);
        if (ret == null)
            throw new ParserException("invalid card state!");
        return ret;
    }

    public static CardAddress monsterZoneParser(String number) throws ParserException {
        int id = -1;
        if (number.equals("1"))
            id = 1;
        if (number.equals("2"))
            id = 2;
        if (number.equals("3"))
            id = 3;
        if (number.equals("4"))
            id = 4;
        if (number.equals("5"))
            id = 5;
        if (id == -1)
            throw new ParserException("monster id is not valid!");
        return new CardAddress(ZoneType.MONSTER, id, GameController.getInstance().getGame().getCurrentPlayer());
    }

    public static Card cardParser(String cardName) throws ParserException {
        Card card = Utils.getCard(cardName);
        if (card != null)
            return card;
        throw new ParserException(String.format("There is no card with name %s", cardName));
    }

    public static Class<? extends BaseMenuController> menuParser(String menuName) throws ParserException {
        if (menuName.equalsIgnoreCase("Login"))
            return LoginMenuController.class;
        if (menuName.equalsIgnoreCase("Main"))
            return MainMenuController.class;
        if (menuName.equalsIgnoreCase("Duel"))
            return DuelMenuController.class;
        if (menuName.equalsIgnoreCase("Deck"))
            return DeckMenuController.class;
        if (menuName.equalsIgnoreCase("Scoreboard"))
            return ScoreboardMenuController.class;
        if (menuName.equalsIgnoreCase("Profile"))
            return ProfileMenuController.class;
        if (menuName.equalsIgnoreCase("Shop"))
            return ShopMenuController.class;
        throw new ParserException("invalid menu name");
    }
}
