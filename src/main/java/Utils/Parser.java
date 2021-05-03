package Utils;

import model.User;

public class Parser {
    public static User
    UserParser(String username) throws ParserException {
        User user = User.getUserByUsername(username);
        if(user == null){
            throw new ParserException("there is no user with this username!");
        }
        return user;
    }
}
