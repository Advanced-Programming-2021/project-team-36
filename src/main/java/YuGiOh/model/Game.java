package YuGiOh.model;

import YuGiOh.model.Player.Player;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.action.Action;
import YuGiOh.model.enums.Phase;
import YuGiOh.model.enums.ZoneType;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class Game {
    @Getter
    private final Player firstPlayer;
    @Getter
    private final Player secondPlayer;

    @Getter
    private int turn;

    @Getter @Setter
    private Phase phase;

    @Getter
    @Setter
    private Stack<Action> chain;

    public Game(Player firstPlayer, Player secondPlayer) throws ModelException {
        if (firstPlayer.getUser().getUsername().equals(secondPlayer.getUser().getUsername()))
            throw new ModelException("you can't play with yourself");
    /*    Random random = new Random(); movagghat: comment kardam ke too graphic ma paeeni bashim
        if (random.nextInt(2) == 0) {
            this.firstPlayer = firstPlayer;
            this.secondPlayer = secondPlayer;
        } else {
            this.firstPlayer = secondPlayer;
            this.secondPlayer = firstPlayer;
        }
     */
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        firstPlayer.getMainDeck().shuffleCards();
        secondPlayer.getMainDeck().shuffleCards();

        for (int i = 0; i < 5; i++) {
            firstPlayer.getBoard().drawCardFromDeck();
            secondPlayer.getBoard().drawCardFromDeck();
        }

        this.turn = 0;
        this.phase = Phase.MAIN_PHASE2;
    }

    public Card getCardByCardAddress(CardAddress cardAddress) {
        return cardAddress.getOwner().getBoard().getCardByCardAddress(cardAddress);
    }

    public CardAddress getCardAddress(Card card){
        CardAddress firstCardAddress = firstPlayer.getBoard().getCardAddress(card);
        CardAddress secondCardAddress = secondPlayer.getBoard().getCardAddress(card);
        if(firstCardAddress != null)
            return firstCardAddress;
        if(secondCardAddress != null)
            return secondCardAddress;
        throw new Error("There is no such card in game");
    }

    public ZoneType getCardZoneType(Card card){
        ZoneType firstPlayerZone = firstPlayer.getBoard().getCardZoneType(card);
        ZoneType secondPlayerZone = secondPlayer.getBoard().getCardZoneType(card);
        if(firstPlayerZone != null)
            return firstPlayerZone;
        if(secondPlayerZone != null)
            return secondPlayerZone;
        throw new Error("There is no such card in game");
    }

    public Player getCurrentPlayer() {
        if (turn % 2 == 0)
            return firstPlayer;
        else
            return secondPlayer;
    }

    public Player getOpponentPlayer() {
        if (turn % 2 == 0)
            return secondPlayer;
        else
            return firstPlayer;
    }

    public void changeTurn() {
        turn++;
    }

    public void moveCardToGraveYard(Card card) {
        // exactly one of them contain this card
        firstPlayer.getBoard().moveCardToGraveYard(card);
        secondPlayer.getBoard().moveCardToGraveYard(card);
    }

    public Player getOtherPlayer(Player player){
        if(player.equals(firstPlayer))
            return secondPlayer;
        if(player.equals(secondPlayer))
            return firstPlayer;
        throw new Error("no such player in the game");
    }

    public List<Card> getAllCardsOnBoard(){
        List<Card> cards = new ArrayList<>();
        cards.addAll(firstPlayer.getBoard().getAllCardsOnBoard());
        cards.addAll(secondPlayer.getBoard().getAllCardsOnBoard());
        return cards;
    }

    public List<Card> getAllCards(){
        List<Card> cards = new ArrayList<>();
        cards.addAll(firstPlayer.getBoard().getAllCards());
        cards.addAll(secondPlayer.getBoard().getAllCards());
        return cards;
    }
}
