package game;

import card.*;
import java.util.*;
import player.Player;

public class Table{
    enum Direction {LEFT_TO_RIGHT, RIGHT_TO_LEFT}

    private List<Card> parade;
    private Direction direction;

    // Table will be empty at the start of the game
    // Default direction is from left to right
    public Table(){
        this.parade = new ArrayList<>(); 
        this.direction = Direction.LEFT_TO_RIGHT; 
    }

    public void addCardToParade(Card card) {
        parade.add(card);
    }

    public List<Card> getParade(){
        if (direction == Direction.LEFT_TO_RIGHT){
            return parade;


        // Reverses the order of cards if direction is from right to left
        } else{                                                     
            List<Card> reversed = new ArrayList<>(parade);
            Collections.reverse(reversed);
            return reversed;
        }

    }

    public void changeDirection() {
        if (direction == Direction.LEFT_TO_RIGHT){
            direction = Direction.RIGHT_TO_LEFT;
        } else {
            direction = Direction.LEFT_TO_RIGHT;
        }
    }

    public void removeCard(Card c){
        if (parade.contains(c)){
            parade.remove(c);
        }
    }

    public void updateParade(Card cardPlaced, Player currentPlayer) {

        if (cardPlaced == null) {
            System.out.println("Error: No card placed.");
            return;
        }

        int oldParadeSize = parade.size();
        parade.add(cardPlaced);

        if (cardPlaced.getValue() == 0) {
            removeCards(0, cardPlaced, currentPlayer);
            return;
        }

        if (cardPlaced.getValue() >= oldParadeSize) {
            return;
        }

        this.removeCards(cardPlaced.getValue(), cardPlaced, currentPlayer);
         parade = new ArrayList<>(parade); // Rebuilds list to maintain order

    }

    public void removeCards(int cardValue, Card cardPlaced, Player currentPlayer) {
        List<Card> removalPile = new ArrayList<>();

        // Starts at parade.size() - 2 because last index is parade.size() - 1, and minus another one because you exclude the just added card.
        for (int i = 0; i < parade.size() - 1 - cardValue; i++) {
            Card currentCard = parade.get(i);
            // Removes card with same color, and cards with value less than or equal to value of played card.card
            if (this.checkRemovalCondition(currentCard, cardPlaced)) {
                removalPile.add(currentCard);
            } 
        }

        // Removes all cards in the removal pile.
        for (Card toRemoveCard : removalPile) {
            currentPlayer.collectCard(toRemoveCard);
            this.removeCard(toRemoveCard);
        }

        if (removalPile.isEmpty()) {
            System.out.println("No cards removed");
        } else {
            System.out.println("Cards removed are: " + removalPile);
        }
    }

    public boolean checkRemovalCondition(Card currentCard, Card cardPlaced) {
        return currentCard.getColor() == cardPlaced.getColor() || currentCard.getValue() <= cardPlaced.getValue();
    }

}
