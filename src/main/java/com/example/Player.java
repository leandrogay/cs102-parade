import java.util.*;

public class Player {
    private String playerName;
    private ArrayList<Card> playerHand = new ArrayList<>();
    private ArrayList<Card> collectedCards = new ArrayList<>();
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

    public String getPlayerName() {
        return playerName;
    }

    public ArrayList<Card> getPlayerHand() {
        return playerHand;
    }

    public ArrayList<Card> getCollectedCards() {
        return collectedCards;
    }

    public int getScore() {
        return score;
    }

    public int calculateScore() {
        return 0;
    }

    public int getNumColors() { // return the number of colors in collected hand
        ArrayList<Color> check_colors = new ArrayList<Color>();
        for (Card card : collectedCards) {
            if (!check_colors.contains(card.getColor())) {
                check_colors.add(card.getColor());
            }
        }
        return check_colors.size();
    }
}
