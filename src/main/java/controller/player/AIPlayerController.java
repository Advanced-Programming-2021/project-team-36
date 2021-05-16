package controller.player;

import controller.GameController;
import controller.cardSelector.ResistToChooseCard;
import controller.cardSelector.SelectCondition;
import model.Game;
import model.Player.AIPlayer;
import model.Player.Player;
import model.card.Card;

import java.util.ArrayList;
import java.util.Random;

public class AIPlayerController extends PlayerController {
    public AIPlayerController(AIPlayer player){
        super(player);
    }

    // todo complete this

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
    public boolean askRespondToChain() {
        return false;
    }

    @Override
    public void doRespondToChain() {
        // todo
    }

    @Override
    public Card[] chooseKCards(String message, int numberOfCards, SelectCondition condition) throws ResistToChooseCard {
        ArrayList<Card> cards = new ArrayList<>();
        for(Card card: player.getBoard().getAllCards()){
            if(condition.canSelect(card)){
                cards.add(card);
            }
        }
        Game game = GameController.getInstance().getGame();
        Player otherPlayer = player.equals(game.getFirstPlayer()) ? game.getSecondPlayer() : game.getFirstPlayer();
        for(Card card: otherPlayer.getBoard().getAllCards()){
            if(condition.canSelect(card)){
                cards.add(card);
            }
        }

        if(cards.size() < numberOfCards)
            throw new ResistToChooseCard();
        Random rnd = new Random();
        while(cards.size() > numberOfCards) {
            int id = rnd.nextInt() % cards.size();
            cards.remove(id);
        }
        return (Card[]) cards.toArray();
    }
}
