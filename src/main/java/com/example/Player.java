import java.util.*;

public class Player {
    private String playerName;
    private List<Card> playerHand = new ArrayList<>();
    private List<Card> collectedCards = new ArrayList<>();
    private int score;

    public Player(String playerName) {
        this.playerName = playerName;
    }

    public void addToHand(Card card) {
        playerHand.add(card);
    }

    public void collectCard(Card card) {
        collectedCards.add(card);
    }

    public String getName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }

    public int calculateScore() {
        
    }

}
