package player;

import card.*;
import game.*;
import java.util.*;

public class BotPlayer extends Player {
    public BotPlayer(String name){
        super(name);
    }

    public Card cardToPlay(List<Card> parade){
        List<Card> botHand = getPlayerHand();
        for (Card c : botHand) {
            if (c.getValue() > 0) {
                return c; // Chooses any non-zero value card
            }
        }
        Collections.shuffle(botHand); // Shuffles the hand to randomise the card selection
        return botHand.get(0); 
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

    public void botDiscard(){
        List<Card> botHand = getPlayerHand();
        Collections.shuffle(botHand); // Shuffles the hand to randomise the card selection

        removeFromHand(botHand.get(0));
        System.out.println(getName() + " discarded: " + botHand.get(0));
    }
}

