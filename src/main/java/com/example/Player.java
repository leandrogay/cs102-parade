import java.util.*;

public class Player {
    private String playerName;
    private List<Card> playerHand = new ArrayList<>();
    private List<Card> collectedCards = new ArrayList<>();
    private int score;

    public Player(String playerName) {
        this.playerName = playerName;
    }

    public void addToHand(Card c) {
        playerHand.add(c);
    }

    public void collectCard(Card c) {
        collectedCards.add(c);
    }

    public int getScore() {
        return score;
    }

    public int calculateScore() {
        
    }

}
