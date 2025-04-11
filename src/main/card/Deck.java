package card;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private List<Card> cards;
    private int cardCount;
    private boolean isDrawable = true;

    public Deck() { 
        this.cards = new ArrayList<>();
        prepDeck();
        shuffleDeck();
        cardCount = this.cards.size();
    }

    public void prepDeck() {
        for (Card.Color color: Card.Color.values()) { // Loop through all colors
            for (int value = 0; value <= 10; value++) { // Loop through values 0 to 10
                cards.add(new Card(color, value));
            }
        }
    }

    public void shuffleDeck () { // Shuffle the deck
        Collections.shuffle(cards);
    }

    public Card draw() { // Draw card from deck
        if (cardCount > 0 && isDrawable) {
            Card cardDrawn = cards.remove(0); // remove from top of the deck
            cardCount--;
            return cardDrawn;
        } else {
            return null; // no card drawn if deck is empty (maybe can throw custom error here idk)
        }
    }

    public List<Card> getDeck() {
        return cards;
    }

    public int getCardCount() { // get number of cards in deck 
        return cardCount;
    }

    public boolean isDrawable() {
        return isDrawable;
    }

    public void setDrawable(boolean isDrawable) {
        this.isDrawable = isDrawable;
    }

    // @Override
    // public String toString() {
    //     return "Deck{" + "cards=" + cards + ", cardCount=" + cardCount + '}';
    // }
}
