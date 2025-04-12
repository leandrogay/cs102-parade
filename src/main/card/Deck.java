package card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        for (Card.Color color : Card.Color.values()) {
            for (int value = 0; value <= 10; value++) {
                cards.add(new Card(color, value));
            }
        }
    }

    public void shuffleDeck() {
        Collections.shuffle(cards);
    }

    public Card draw() {
        if (cardCount > 0 && isDrawable) {
            Card cardDrawn = cards.remove(0);
            cardCount--;
            return cardDrawn;
        } else {
            return null;
        }
    }

    public List<Card> getDeck() {
        return cards;
    }

    public int getCardCount() {
        return cardCount;
    }

    public boolean isDrawable() {
        return isDrawable;
    }

    public void setDrawable(boolean isDrawable) {
        this.isDrawable = isDrawable;
    }
}
