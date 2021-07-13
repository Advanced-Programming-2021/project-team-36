package YuGiOh.model.Player;

import YuGiOh.model.enums.AIMode;
import YuGiOh.utils.Cheat;
import YuGiOh.model.exception.ModelException;
import YuGiOh.model.User;
import lombok.Getter;

public class AIPlayer extends Player {
    @Getter
    private AIMode aiMode;

    public AIPlayer(AIMode aiMode) throws ModelException {
        this(getAIUser(), aiMode);
    }

    public AIPlayer(User user, AIMode aiMode) throws ModelException {
        super(user);
        this.aiMode = aiMode;
    }

    private static User getAIUser() {
        int aiId = 1;
        while (User.getUserByUsername("artificial_intelligence" + aiId) != null || User.getUserByNickname("Mr.AI" + aiId) != null)
            aiId++;
        User user = new User("artificial_intelligence" + aiId, "Mr.AI" + aiId, "thisIsAStrongPassword");
        Cheat.buildSuperUser(user);
        return user;
    }
}
