package model.Player;

import model.ModelException;
import model.User;

public class HumanPlayer extends Player {
    public HumanPlayer(User user) throws ModelException {
        super(user);
    }
}
