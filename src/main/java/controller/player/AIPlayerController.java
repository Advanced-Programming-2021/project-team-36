package controller.player;

import controller.GameController;
import model.Player.AIPlayer;

public class AIPlayerController extends PlayerController {
    public AIPlayerController(AIPlayer player){
        super(player);
    }

    // todo complete this

    @Override
    public void controlDrawPhase() {
        GameController.getInstance().goNextPhase();
    }

    @Override
    public void controlStandbyPhase() {
        GameController.getInstance().goNextPhase();
    }

    @Override
    public void controlMainPhase1() {
        GameController.getInstance().goNextPhase();
    }

    @Override
    public void controlMainPhase2() {
        GameController.getInstance().goNextPhase();
    }

    @Override
    public void controlBattlePhase() {
        GameController.getInstance().goNextPhase();
    }

    @Override
    public void controlEndPhase() {
        GameController.getInstance().goNextPhase();
    }
}
