package edu.sharif.nameless.in.seattle.yugioh.model.Player;

import edu.sharif.nameless.in.seattle.yugioh.model.ModelException;
import edu.sharif.nameless.in.seattle.yugioh.model.User;
import edu.sharif.nameless.in.seattle.yugioh.utils.Cheat;

public class AIPlayer extends Player {
    public AIPlayer() throws ModelException {
        super(getAIUser());
    }
    private static User getAIUser() {
        int aiId = 1;
        while(User.getUserByUsername("artificial_intelligence" + aiId) != null || User.getUserByNickname("Mr.AI" + aiId) != null)
            aiId++;
        User user = new User("artificial_intelligence" + aiId, "Mr.AI" + aiId, "thisIsAStrongPassword");
        Cheat.buildSuperUser(user);
        return user;
    }
}
