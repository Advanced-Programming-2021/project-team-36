package view;

import controller.*;
import model.CardAddress;
import model.User;
import model.card.Card;
import model.card.Utils;
import model.deck.Deck;
import model.enums.MonsterState;
import model.enums.ZoneType;

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

    // TODO : Proof-reading
    public static CardAddress cardAddressParser(String zoneName, String idString, boolean opponent) throws ParserException {
        ZoneType zone = ZoneType.getZoneByName(zoneName);
        if (zone == null)
            throw new ParserException("invalid selection");
        int id = IntegerParser(idString);
        return new CardAddress(zone, id, opponent);
    }

    // TODO : Proof-reading
    public static MonsterState cardStateParser(String state) throws ParserException {
        MonsterState ret = MonsterState.getOccupiedStateByName(state);
        if (ret == null)
            throw new ParserException("invalid card state!");
        return ret;
    }

    // TODO : Proof-reading
    public static int monsterPositionIdParser(String number) throws ParserException {
        if (number.equals("1"))
            return 1;
        if (number.equals("2"))
            return 1;
        if (number.equals("3"))
            return 3;
        if (number.equals("4"))
            return 4;
        if (number.equals("5"))
            return 5;
        throw new ParserException("monster id is not valid!");
    }

    // TODO : Proof-reading
    public static Card cardParser(String cardName) throws ParserException {
        for (Card c : Utils.getAllCards())
            if (c.getName().equalsIgnoreCase(cardName))
                return c;
        throw new ParserException("There is no card with this name");
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
        if (menuName.equalsIgnoreCase("Import/Export"))
            return ImportAndExportMenuController.class;
        throw new ParserException("invalid menu name");
    }
}