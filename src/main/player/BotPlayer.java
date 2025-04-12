package player;

import java.util.*;
import card.*;
import game.*;

public class BotPlayer extends Player {
    public BotPlayer(String name){
        super(name);
    }

    public Card cardToPlay(List<Card> parade){
        List<Card> botHand = getPlayerHand(); // get hand of cards for bot 
        for (Card c : botHand) {
            if (c.getValue() > 0) {
                return c; // choose any non-zero value card
            }
        }
        Collections.shuffle(botHand); // shuffle the hand so that the next line takes in // do I need this ?????
        return botHand.get(0); // if card is 0 value, choose any
    }

    public void botTurn(Game game){
        Card playCard = cardToPlay(game.getParade());
        if (playCard == null) {
            return;
        } 
        System.out.println(getName() + " plays: " + playCard);
        getPlayerHand();
        game.collectCardsFromParade(this, playCard);
    }

    public void botDiscard() {
        List<Card> hand = getPlayerHand();
        // to prevent any error if the bot has less than 2 cards 
        if (hand.size() < 2) {
            return;  
        }
        // sort bot's hand by value in descending order using CardComparator
        Collections.sort(hand, Collections.reverseOrder(new CardComparator()));
        
        // remove first card to be discarded 
        removeFromHand(hand.get(0));
        System.out.println(getName() + " discarded: " + hand.get(0));
        // returns second card to be discarded 
        removeFromHand(hand.get(1));
        System.out.println(getName() + " discarded: " + hand.get(1));
    }
}

