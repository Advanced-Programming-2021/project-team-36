package YuGiOh.model.Player;

import YuGiOh.utils.Cheat;
import YuGiOh.model.ModelException;
import YuGiOh.model.User;

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
