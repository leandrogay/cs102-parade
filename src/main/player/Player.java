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


    public void calculateScore(HashMap<Card.Color, Integer> majorityCardMap, boolean is2Players, Map<Card.Color, Integer> oppCardCollection) {
        score = 0; 
        List<Card.Color> flippedCards;
        if (is2Players) {
            flippedCards = getFlippedCardsColor2Players(oppCardCollection);
        } else {
            flippedCards = getFlippedCardsColorManyPlayers(majorityCardMap);
        }
        for (Card card : collectedCards) {
            if (flippedCards.contains(card.getColor())) { 
                card.setIsFlipped(true);
                score += 1;
            } else { 
                score += card.getValue();
            }
        } 
        System.out.println("Player: " + playerName);
        System.out.println("Flipped Cards: " + flippedCards);
        System.out.println("Collected Cards: " + collectedCards);
    }


    public int getNumColors() { 
        List<Card.Color> checkColors;
        checkColors = new ArrayList<>();
        for (Card card : collectedCards) {
            if (!checkColors.contains(card.getColor())) {
                checkColors.add(card.getColor());
            }
        }
        return checkColors.size();
    }

    public Map<Card.Color, Integer> getCardCollection() { 
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

    public List<Card.Color> getFlippedCardsColorManyPlayers(Map<Card.Color, Integer> majorityCardMap) {
        Map<Card.Color, Integer> personalCollectionMap = getCardCollection();
        List<Card.Color> flippedCards = new ArrayList<>();
        personalCollectionMap.forEach((color, numCards) -> {
            if (majorityCardMap.containsKey(color)) {
                Integer maxNumColor = majorityCardMap.get(color);
                if (maxNumColor != -1) {
                    if (Objects.equals(maxNumColor, numCards)) {
                        flippedCards.add(color);
                    }
                }
            }
        });
        return flippedCards;
    }

    public List<Card.Color> getFlippedCardsColor2Players(Map<Card.Color, Integer> oppCardCollection) { 
        Map<Card.Color, Integer> personalCollectionMap = getCardCollection();
        List<Card.Color> flippedCards = new ArrayList<>();
        personalCollectionMap.forEach((color, numCards) -> {
            if (oppCardCollection.containsKey(color)) { 
                Integer oppNumCards = oppCardCollection.get(color);
                if (numCards - oppNumCards >= 2) {
                    flippedCards.add(color);
                }
            } else { 
                if (numCards >= 2) {
                    flippedCards.add(color);
                }
            }
        });
        return flippedCards;
    }
}
