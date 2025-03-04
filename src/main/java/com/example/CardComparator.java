import java.util.Comparator;

public class CardComparator implements Comparator<Card> {

    public int compare(Card card1, Card card2) {
        int cmp = card1.getColor().compareTo(card2.getColor());

        if (cmp != 0) {
            return cmp;
        }

        return card1.getValue() - card2.getValue();
    }
}