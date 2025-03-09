import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<Card> cards;
    private int cardCount;

    public Deck(ArrayList<Card> initialCards) { // initialCards -> before any changes are made
        this.cards = new ArrayList<>(initialCards);
        this.cardCount = cards.size();
    }

    public void shuffle() { // shuffle
        Collections.shuffle(cards);
    }

    public Card draw() { // draw card
        if (cardCount > 0) {
            Card cardDrawn = cards.remove(0); // remove from top of the deck
            cardCount--;
            return cardDrawn;
        } else {
            return null; // no card drawn if deck is empty (maybe can throw custom error here idk)
        }
    }

    public int getCardCount() { // get number of cards in deck 
        return cardCount;
    }

    // @Override
    // public String toString() {
    //     return "Deck{" + "cards=" + cards + ", cardCount=" + cardCount + '}';
    // }
}
