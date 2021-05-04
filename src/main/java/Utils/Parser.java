package Utils;

import model.CardAddress;
import model.User;
import model.card.Card;
import model.card.Monster;
import model.enums.State;
import model.enums.ZoneType;

public class Parser {
    public static User UserParser(String username) throws ParserException {
        User user = User.getUserByUsername(username);
        if(user == null){
            throw new ParserException("there is no user with this username!");
        }
        return user;
    }
    public static Integer IntegerParser(String number) throws ParserException {
        try{
            return Integer.parseInt(number);
        } catch (Exception e) {
            throw new ParserException("invalid number");
        }
    }
    public static int RoundParser(String number) throws ParserException {
        if(number.equals("1"))
            return 1;
        if(number.equals("2"))
            return 1;
        if(number.equals("3"))
            return 3;
        throw new ParserException("round is not valid!");
    }
    public static CardAddress cardAddressParser(String zoneName, String idString, boolean opponent) throws ParserException {
        ZoneType zone = ZoneType.getZoneByName(zoneName);
        if (zone == null)
            throw new ParserException("invalid zone name");
        int id = IntegerParser(idString);
        return new CardAddress(zone, id, opponent);
    }
    public static State cardStateParser(String state) throws ParserException {
        State ret = State.getOccupiedStateByName(state);
        if (ret == null)
            throw new ParserException("invalid card state!");
        return ret;
    }
    public static int monsterPositionIdParser(String number) throws ParserException {
        if(number.equals("1"))
            return 1;
        if(number.equals("2"))
            return 1;
        if(number.equals("3"))
            return 3;
        if(number.equals("4"))
            return 4;
        if(number.equals("5"))
            return 5;
        throw new ParserException("monster id is not valid!");
    }
    public static Card cardParser(String cardName) throws ParserException {
        // todo add some card to game!
        if(cardName.equals("shit shit"))
            throw new ParserException(String.format("card with name %s does not exist", cardName));
        return new Monster();
        // just for test
    }
    public static Class<?> menuParser(String menuName) throws ParserException {
        if(menuName.equalsIgnoreCase("Login"))
            return view.LoginMenu.class;
        if(menuName.equalsIgnoreCase("Main"))
            return view.MainMenu.class;
        if(menuName.equalsIgnoreCase("Duel"))
            return view.DuelMenu.class;
        if(menuName.equalsIgnoreCase("Deck"))
            return view.DeckMenu.class;
        if(menuName.equalsIgnoreCase("Scoreboard"))
            return view.ScoreboardMenu.class;
        if(menuName.equalsIgnoreCase("Profile"))
            return view.ProfileMenu.class;
        if(menuName.equalsIgnoreCase("Shop"))
            return view.ShopMenu.class;
        if(menuName.equalsIgnoreCase("Import/Export"))
            return view.ImportExportMenu.class;
        throw new ParserException("invalid menu name");
    }
}
