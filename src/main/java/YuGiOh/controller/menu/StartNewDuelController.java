package YuGiOh.controller.menu;

import YuGiOh.model.exception.DuelHasNotStarted;
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

import java.util.Optional;

public class StartNewDuelController extends BaseMenuController {
    private static void startDuel(Duel duel) throws LogicException {
        new DuelMenuController(duel).newGameRequest();
        CustomPrinter.println(String.format("start new duel between %s and %s", duel.getFirstPlayer().getUser().getNickname(), duel.getSecondPlayer().getUser().getNickname()), Color.Default);
    }

    private static Duel getDuel(Request request) {
        Optional<Duel> opt = Duel.getUserLastDuel(request.getUser());
        if(opt.isPresent())
            return opt.get();
        throw new DuelHasNotStarted();
    }

    public static void startNewDuel(Request request, String secondUsername, int round) throws LogicException, ModelException {
        User secondUser = User.getUserByUsername(secondUsername);
        if (secondUser == null)
            throw new ModelException("The specified user does not exist!");
        startDuel(new Duel(
                new HumanPlayer(request.getUser()),
                new HumanPlayer(secondUser),
                round
        ));
    }

    public static void startDuelWithAI(Request request, int round, AIMode aiMode) throws LogicException, ModelException {
        startDuel(new Duel(
                new HumanPlayer(request.getUser()),
                new AIPlayer(aiMode),
                round
        ));
    }

    public static boolean doesUserStart(Request request) {
        return getDuel(request).getFirstPlayer().getUser().getUserId() == request.getUser().getUserId();
    }
}
