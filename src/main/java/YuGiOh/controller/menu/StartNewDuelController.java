package YuGiOh.controller.menu;

import YuGiOh.model.exception.LogicException;
import YuGiOh.model.Duel;
import YuGiOh.model.exception.ModelException;
import YuGiOh.model.Player.AIPlayer;
import YuGiOh.model.Player.HumanPlayer;
import YuGiOh.model.User;
import YuGiOh.model.enums.AIMode;
import YuGiOh.model.enums.Color;
import YuGiOh.network.packet.Request;
import YuGiOh.utils.CustomPrinter;

public class StartNewDuelController extends BaseMenuController {
    private static void startDuel(Duel duel) throws LogicException {
        new DuelMenuController(duel).newGameRequest();
        CustomPrinter.println(String.format("start new duel between %s and %s", duel.getFirstPlayer().getUser().getNickname(), duel.getSecondPlayer().getUser().getNickname()), Color.Default);
    }

    public static void startNewDuel(Request request, boolean userGoesFirst, String secondUsername, int round) throws LogicException, ModelException {
        User secondUser = User.getUserByUsername(secondUsername);
        if (secondUser == null)
            throw new ModelException("The specified user does not exist!");
        if (userGoesFirst) {
            startDuel(new Duel(
                    new HumanPlayer(request.getUser()),
                    new HumanPlayer(secondUser),
                    round
            ));
        } else {
            startDuel(new Duel(
                    new HumanPlayer(secondUser),
                    new HumanPlayer(request.getUser()),
                    round
            ));
        }
    }

    public static void startDuelWithAI(Request request, boolean userGoesFirst, int round, AIMode aiMode) throws LogicException, ModelException {
        if (!userGoesFirst) {
            startDuel(new Duel(
                    new HumanPlayer(request.getUser()),
                    new AIPlayer(aiMode),
                    round
            ));
        }
        else {
            startDuel(new Duel(
                    new AIPlayer(aiMode),
                    new HumanPlayer(request.getUser()),
                    round
            ));
        }
    }
}
