package edu.sharif.nameless.in.seattle.yugioh.model.Player;

import edu.sharif.nameless.in.seattle.yugioh.model.ModelException;
import edu.sharif.nameless.in.seattle.yugioh.model.User;

public class HumanPlayer extends Player {
    public HumanPlayer(User user) throws ModelException {
        super(user);
    }
}
