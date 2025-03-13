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
        ArrayList<Card.Color> check_colors = new ArrayList<Card.Color>();
        for (Card card : collectedCards) {
            if (!check_colors.contains(card.getColor())) {
                check_colors.add(card.getColor());
            }
        }
        return check_colors.size();
    }

    public HashMap<Card.Color, Integer> getCardCollection() { // return a mapping of color player obtained with the number of cards of that color
        HashMap<Card.Color, Integer> card_map = new HashMap<Card.Color, Integer>();
        for (Card card : collectedCards) {
            Card.Color card_color= card.getColor();
            if (!card_map.containsKey(card_color)) {
                card_map.put(card_color, 0);
            } else {
                card_map.put(card_color, card_map.getOrDefault(card_color, 0) + 1);
            }
        }
        return card_map;
    }
}
