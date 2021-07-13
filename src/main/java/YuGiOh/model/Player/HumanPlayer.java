package YuGiOh.model.Player;

import YuGiOh.model.exception.ModelException;
import YuGiOh.model.User;

public class HumanPlayer extends Player {
    public HumanPlayer(User user) throws ModelException {
        super(user);
    }
}
