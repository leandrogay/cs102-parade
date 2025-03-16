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
        Collections.sort(playerHand, new CardComparator());
        return playerHand;
    }

    public ArrayList<Card> getCollectedCards() {
        return collectedCards;
    }

    public int getScore() {
        return score;
    }

    // condition for if there are > 2 players
    public void calculateScore(HashMap<Card.Color, Integer> majority_card_map) {
        score = 0; // reset score
        ArrayList<Card.Color> flippedCards = getFlippedCardsColor(majority_card_map);
        for (Card card : collectedCards) {
            if (flippedCards.contains(card.getColor())) { // the card is flipped down -> score +1
                score += 1;
            } else { // card is flipped up -> score +num on card
                score += card.getValue();
            }
        } 
    }


    public int getNumColors() { // return the number of colors in collected hand
        ArrayList<Card.Color> check_colors;
        check_colors = new ArrayList<>();
        for (Card card : collectedCards) {
            if (!check_colors.contains(card.getColor())) {
                check_colors.add(card.getColor());
            }
        }
        return check_colors.size();
    }

    public HashMap<Card.Color, Integer> getCardCollection() { // return a mapping of color player obtained with the number of cards of that color
        HashMap<Card.Color, Integer> card_map = new HashMap<>();
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

    // FOR WHEN THERE ARE > 2 PLAYERS
    public ArrayList<Card.Color> getFlippedCardsColorManyPlayers(HashMap<Card.Color, Integer> majority_card_map) { // retrieve majority_card_map from getMajorityOfEachCard() in Game class
        HashMap<Card.Color, Integer> personal_collection_map = getCardCollection();
        ArrayList<Card.Color> flipped_cards = new ArrayList<>();
        personal_collection_map.forEach((color, num_cards) -> {
            if (majority_card_map.containsKey(color)) {
                Integer max_num_color = majority_card_map.get(color);
                if (Objects.equals(max_num_color, num_cards)) { // is the max 
                    flipped_cards.add(color);
                }
            }
        });
        return flipped_cards;
    }

    // FOR WHEN THERE ARE 2 PLAYERS
    public ArrayList<Card.Color> getFlippedCardsColor2Players(HashMap<Card.Color, Integer> opp_card_collection) { // retrieve opponent's card collection in Game class if possible
        HashMap<Card.Color, Integer> personal_collection_map = getCardCollection();
        ArrayList<Card.Color> flipped_cards = new ArrayList<>();
        personal_collection_map.forEach((color, num_cards) -> {
            if (opp_card_collection.containsKey(color)) { // opponent's hand also possess the card that player has
                Integer opp_num_cards = opp_card_collection.get(color);
                if (num_cards > opp_num_cards) { // player's card hand is more than opponent's
                    if (num_cards - opp_num_cards >= 2) { // player has majority in this case
                        flipped_cards.add(color);
                    }
                }
            } else { // opponent do not possess that hand, player automatically has majority
                flipped_cards.add(color);
            }
        });
        return flipped_cards;
    }
}
