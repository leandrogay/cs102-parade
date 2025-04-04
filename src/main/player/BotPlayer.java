package player;

import java.util.*;
import card.*;
import game.*;

public class BotPlayer extends Player {
    public BotPlayer(String name){
        super(name);
    }

    public Card cardToPlay(ArrayList<Card> parade){
        ArrayList<Card> botHand = getPlayerHand(); // get hand of cards for bot 
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
        } else {
            System.out.println(getName() + " plays: " + playCard);
            getPlayerHand();
            game.collectCardsFromParade(this, playCard);
        }
    }
}

