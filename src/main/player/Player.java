package player;

import card.Card;
import card.CardComparator;
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

    public void removeFromHand(Card card) {

        for (Card c : playerHand) {
            if (c.equals(card)) {
                playerHand.remove(c);
                // System.out.println(c + " removed from hand");
                return;
            }
        }

        System.out.println("Card not removed");
    
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

    public List<Card> getPlayerHand() {
        Collections.sort(playerHand, new CardComparator());
        return playerHand;
    }

    public List<Card> getCollectedCards() {
        Collections.sort(collectedCards, new CardComparator());
        return collectedCards;
    }

    public int getScore() {
        return score;
    }

    // condition for if there are > 2 players
    public void calculateScore(HashMap<Card.Color, Integer> majorityCardMap, boolean is2Players) {
        score = 0; // reset score
        List<Card.Color> flippedCards;
        if (is2Players) {
            flippedCards = getFlippedCardsColor2Players(majorityCardMap);
        } else {
            flippedCards = getFlippedCardsColorManyPlayers(majorityCardMap);
        }
        for (Card card : collectedCards) {
            if (flippedCards.contains(card.getColor())) { // the card is flipped down -> score +1
                card.setIsFlipped(true);
                score += 1;
            } else { // card is flipped up -> score +num on card
                score += card.getValue();
            }
        } 
        System.out.println("Player: " + playerName);
        System.out.println("Flipped Cards: " + flippedCards);
        System.out.println("Collected Cards: " + collectedCards);
    }


    public int getNumColors() { // return the number of colors in collected hand
        List<Card.Color> checkColors;
        checkColors = new ArrayList<>();
        for (Card card : collectedCards) {
            if (!checkColors.contains(card.getColor())) {
                checkColors.add(card.getColor());
            }
        }
        return checkColors.size();
    }

    public Map<Card.Color, Integer> getCardCollection() { // return a mapping of color player obtained with the number of cards of that color
        Map<Card.Color, Integer> cardMap = new HashMap<>();
        for (Card card : collectedCards) {
            Card.Color card_color= card.getColor();
            if (!cardMap.containsKey(card_color)) {
                cardMap.put(card_color, 1);
            } else {
                cardMap.put(card_color, cardMap.getOrDefault(card_color, 0) + 1);
            }
        }
        return cardMap;
    }

    // FOR WHEN THERE ARE > 2 PLAYERS
    public List<Card.Color> getFlippedCardsColorManyPlayers(Map<Card.Color, Integer> majorityCardMap) { // retrieve majority_card_map from getMajorityOfEachCard() in Game class
        Map<Card.Color, Integer> personalCollectionMap = getCardCollection();
        List<Card.Color> flippedCards = new ArrayList<>();
        personalCollectionMap.forEach((color, numCards) -> {
            if (majorityCardMap.containsKey(color)) {
                Integer maxNumColor = majorityCardMap.get(color);
                if (maxNumColor != -1) {
                    if (Objects.equals(maxNumColor, numCards)) { // is the max 
                        flippedCards.add(color);
                    }
                }
            }
        });
        return flippedCards;
    }

    // FOR WHEN THERE ARE 2 PLAYERS
    public List<Card.Color> getFlippedCardsColor2Players(Map<Card.Color, Integer> oppCardCollection) { // retrieve opponent's card collection in Game class if possible
        Map<Card.Color, Integer> personalCollectionMap = getCardCollection();
        List<Card.Color> flippedCards = new ArrayList<>();
        personalCollectionMap.forEach((color, numCards) -> {
            if (oppCardCollection.containsKey(color)) { // opponent's hand also possess the card that player has
                Integer oppNumCards = oppCardCollection.get(color);
                if (numCards >= oppNumCards + 2) { // player's card hand is more than oppenent's by at least 2 
                    flippedCards.add(color);
                }
            } else { // opponent do not possess that hand, check if player has 2 or more cards 
                if (numCards >= 2) {
                    flippedCards.add(color);
                }
            }
        });
        return flippedCards;
    }
}
