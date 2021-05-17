package controller.player;

import controller.GameController;
import controller.LogicException;
import controller.cardSelector.ResistToChooseCard;
import controller.cardSelector.SelectCondition;
import model.Game;
import model.Player.AIPlayer;
import model.Player.Player;
import model.card.Card;
import model.card.Magic;
import model.card.Monster;

import java.util.ArrayList;
import java.util.List;
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

    public void mainPhase(){
        Random rnd = new Random();
        try {
            while (true) {
                List<Card> cards = player.getBoard().getCardsOnHand();
                Card card = cards.get(rnd.nextInt(cards.size()));
                if(card instanceof Monster) {
                    int r = rnd.nextInt(3);
                    if(r == 0)
                        summonCard((Monster) card);
                    if(r == 1)
                        flipSummon((Monster) card);
                    if(r == 2)
                        setMonster((Monster) card);
                }
                else if(card instanceof Magic) {
                    setMagic(card);
                }
            }
        } catch (ResistToChooseCard | LogicException ignored) {
        }
        GameController.getInstance().goNextPhase();
    }

    @Override
    public void controlMainPhase1() {
        mainPhase();
    }

    @Override
    public void controlMainPhase2() {
        mainPhase();
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
            int id = rnd.nextInt(cards.size());
            cards.remove(id);
        }
        return cards.toArray(Card[]::new);
    }
}
