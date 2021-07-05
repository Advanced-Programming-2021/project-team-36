package YuGiOh.model.enums;

import YuGiOh.controller.player.AIPlayerController;
import YuGiOh.controller.player.AggressiveAIPlayerController;
import YuGiOh.model.Player.AIPlayer;

public enum AIMode {
    NORMAL,
    AGGRESSIVE;

    public AIPlayerController getNewInstance(AIPlayer aiPlayer) {
        if(this.equals(NORMAL))
            return new AIPlayerController(aiPlayer);
        if(this.equals(AGGRESSIVE))
            return new AggressiveAIPlayerController(aiPlayer);
        return null;
    }
}
