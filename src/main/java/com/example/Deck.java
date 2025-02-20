import java.util.Collections;
import java.util.Stack;

public class Deck {
    private final Stack<Card> cards = new Stack<>();

    public Deck() {
        for (Card.Color color : Card.Color.values()) {
            for (int value = 0; value <= 10; value++) {
                cards.add(new Card(color, value));
            }
        }
        Collections.shuffle(cards);
    }

    public Card drawCard() {
        return cards.isEmpty() ? null : cards.pop();
    }
}
