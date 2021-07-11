package YuGiOh.controller.menu;

import YuGiOh.controller.LogicException;
import YuGiOh.model.Duel;
import YuGiOh.model.ModelException;
import YuGiOh.model.Player.AIPlayer;
import YuGiOh.model.Player.HumanPlayer;
import YuGiOh.model.User;
import YuGiOh.model.enums.AIMode;
import YuGiOh.model.enums.Color;
import YuGiOh.utils.CustomPrinter;
import lombok.Getter;

public class StartNewDuelController extends BaseMenuController {
    @Getter
    private static StartNewDuelController instance;

    public StartNewDuelController() {
        instance = this;
    }

    private void startDuel(Duel duel) throws LogicException {
        new DuelMenuController(duel).getNewGameThread();
        CustomPrinter.println(String.format("start new duel between %s and %s", duel.getFirstPlayer().getUser().getNickname(), duel.getSecondPlayer().getUser().getNickname()), Color.Default);
    }

    public void startNewDuel(boolean userGoesFirst, String secondUsername, int round) throws LogicException, ModelException {
        User secondUser = User.getUserByUsername(secondUsername);
        if (secondUser == null)
            throw new ModelException("The specified user does not exist!");
        if (userGoesFirst) {
            startDuel(new Duel(
                    new HumanPlayer(MainMenuController.getInstance().getUser()),
                    new HumanPlayer(secondUser),
                    round
            ));
        } else {
            startDuel(new Duel(
                    new HumanPlayer(secondUser),
                    new HumanPlayer(MainMenuController.getInstance().getUser()),
                    round
            ));
        }
    }

    public void startDuelWithAI(boolean userGoesFirst, int round, AIMode aiMode) throws LogicException, ModelException {
        if (!userGoesFirst) {
            startDuel(new Duel(
                    new HumanPlayer(MainMenuController.getInstance().getUser()),
                    new AIPlayer(aiMode),
                    round
            ));
        }
        else {
            startDuel(new Duel(
                    new AIPlayer(aiMode),
                    new HumanPlayer(MainMenuController.getInstance().getUser()),
                    round
            ));
        }
    }
}
