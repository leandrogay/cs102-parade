package card;

import java.util.Comparator;

public class CardComparator implements Comparator<Card> {

    @Override
    public int compare(Card card1, Card card2) {
        // compares based on their ordinal values declared in enum 
        int cmp = card1.getColor().compareTo(card2.getColor());

        // returns if the colours of the card objects are not the same 
        if (cmp != 0) {
            return cmp;
        }
        // returns by ascending order
        return card1.getValue() - card2.getValue();
    }
}