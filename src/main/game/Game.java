package game;

import card.*;
import exception.*;
import java.util.*;
import player.*;

public class Game {
    private Deck deck;
    private Table table;
    private int playerNumber;
    private int botCount;
    private boolean is2Players;
    private ArrayList<Player> players = new ArrayList<>();
    private String divider = "===================================================================================";
    private int delayDuration = 500;
    private int roundDelayDuration = 2000;

    public Game(int playerNumber) {
        this.deck = new Deck();
        this.table = new Table();
        this.playerNumber = playerNumber;
        playerSettings();
        dealStartingHands();
        setTable();
    }

    public void playerSettings(){
        Scanner sc = new Scanner(System.in);
        
        boolean validPlayerSetup = false;
        
        while (!validPlayerSetup) {
            players.clear(); // clear player list
            botCount = 0; // reset number of bots 
            
            boolean inputError = false; // to check for input error (not y or n)
            
            for (int i = 0; i < this.playerNumber; i++) {
                System.out.print("Do you want Player " + (i + 1) + " to be a bot? (y/n): ");
                String isBotResponse = sc.nextLine().trim().toLowerCase();
                
                switch (isBotResponse) {
                    case "y":
                        players.add(new BotPlayer("BotPlayer " + (i + 1)));
                        botCount++; // keep track of number of bots 
                        break;
                    case "n":
                        System.out.print("Enter name for Player " + (i + 1) + ": ");
                        String name = sc.nextLine().trim();
                        players.add(new Player(name));
                        break;
                    default:
                        System.out.println("Invalid input. Please enter either y or n");
                        inputError = true;
                        break;
                }
                
                if (inputError) {
                    break; // exit loop if input error 
                }
            }
            
            // if there is an input error, continue the while loop 
            if (inputError) {
                continue;
            }
            
            // to check if all players all bots (all players should not be bots)
            if (botCount == playerNumber) {
                System.out.println("At least one human player is required. Restarting player setup."); // while loop will continue and player setup will restart 
            } else {
                validPlayerSetup = true;
            }
        }
    }
    
    public void dealStartingHands() { // Distributes 5 cards to each player
        int cardsPerPlayer = 5;

        for (int i = 0; i < cardsPerPlayer; i++) { // Repeat 5 times
            for (Player player : players) { // Iterate through players in order
                Card drawnCard = deck.draw();
                if (drawnCard != null) {
                    player.addToHand(drawnCard);
                } else {
                    System.out.println("Deck ran out of cards!");
                    return; // Stop dealing if the deck is empty
                }
            }
        }
    }

    public void setTable() { // Draws and places 6 cards to the table/parade
        int cardsPerTable = 6;

        for (int i = 0; i < cardsPerTable; i++) {
            Card drawnCard = deck.draw();
            if (drawnCard != null) {
                table.addCardToParade(drawnCard);
            } else {
                System.out.println("Deck ran out of cards!");
                return; // Stop dealing if the deck is empty
            }
        }
    }

    public ArrayList<Card> getDeck() {
        return deck.getDeck();
    }

    public ArrayList<Card> getParade() {
        return table.getParade();
    }

    public boolean getIs2Players() {
        return is2Players;
    }

    public HashMap<Card.Color, Integer> getMajorityOfEachCard() { // returns a map mapping each card to its majority
                                                                  // holder number
        HashMap<Card.Color, ArrayList<Integer>> color_result_map = new HashMap<>();
        for (Card.Color color : Card.Color.values()) {
            color_result_map.put(color, new ArrayList<>());
            for (Player player : players) {
                HashMap<Card.Color, Integer> player_collection = player.getCardCollection();
                if (player_collection.containsKey(color)) { // player collection possesses a card of that color
                    color_result_map.get(color).add(player_collection.get(color));
                }
            }
        }
        HashMap<Card.Color, Integer> color_majority_map = new HashMap<>();
        color_result_map.forEach((color, color_num_array) -> {
            if (!color_num_array.isEmpty()) {
                Integer max_color = Collections.max(color_num_array);
                color_majority_map.put(color, max_color);
            } else {
                color_majority_map.put(color, 0);
            }
        });
        return color_majority_map;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public List<Integer> tabulateScore() {
        List<Integer> scores = new ArrayList<>();

        for (Player player : players) {
            scores.add(player.getScore());
        }

        return scores;
    }

    public boolean drawPileExhausted() {
        return deck.getCardCount() == 0;
    }

    public boolean checkAllColors() {
        for (Player player : players) {
            if (player.getNumColors() == 6) { // player got all suites of the cards
                return true;
            }
        }
        return false;
    }

    public boolean checkLastRound() {
        // if either of the conditions are fulfilled, the game ends
        return drawPileExhausted() || checkAllColors();
    }

    public void collectCardsFromParade(Player player, Card playedCard) {
        // Changed direction to make it easier for removal condition.
        table.changeDirection();

        ArrayList<Card> parade = table.getParade();
        ArrayList<Card> cardsToCollect = new ArrayList<>();

        int startIndex = Math.max(0, playedCard.getValue() - 1);
        for (int i = startIndex; i < parade.size(); i++) {

            // for (int i = playedCard.getValue() - 1; i < parade.size(); i++){ // since we
            // have to skip n (played cards value) number of cards

            Card c = parade.get(i);
            if (c.getColor() == playedCard.getColor() || c.getValue() <= playedCard.getValue()) {
                cardsToCollect.add(c);
            }
        }

        for (Card c : cardsToCollect) {
            player.collectCard(c);
            table.removeCard(c);
        }

        // sets the direction back to LEFT_TO_RIGHT and adds the played card to the
        // parade
        table.changeDirection();
        table.addCardToParade(playedCard);

        Card drawnCard = deck.draw();
        // player draws card after end of turn (should i use the addToHand method or
        // collectCard method for this)
        player.addToHand(drawnCard);

        System.out.println(player.getName() + " collected: " + cardsToCollect);
    }

    public ArrayList<Player> getWinner() {
        List<Integer> scores = tabulateScore();
        int lowestScore = Collections.min(scores); // Find the lowest score
        ArrayList<Player> lowestScorePlayers = new ArrayList<>();

        for (Player p : players) {
            if (p.getScore() == lowestScore) {
                lowestScorePlayers.add(p);
            }
        }
        if (lowestScorePlayers.size() == 1){
            return lowestScorePlayers;
        }

        // TIE BREAKER
        // Still returns an array list in case of another tie
        ArrayList<Integer> cardCounts = new ArrayList<>();
        for (Player p : lowestScorePlayers){
            cardCounts.add(p.getCollectedCards().size());
        }
        int lowestCardCount = Collections.min(cardCounts);

        ArrayList<Player> winners = new ArrayList<>();
        for (Player p : lowestScorePlayers){
            if (p.getCollectedCards().size() == lowestCardCount){
                winners.add(p);
            }
        }

        return winners;
    }

    public void conductRound() {
        System.out.println();
        for (int currPlayerIndex = 0; currPlayerIndex < players.size(); currPlayerIndex++) {

            boolean isLastRound = this.checkLastRound();

            if (isLastRound) {
                DisplayUtility.printDivider("LAST ROUND");
            }
            // set currPlayer to i so that it can rotate between players

            Player currentPlayer = players.get(currPlayerIndex);
            System.out.println("Current Player: " + currentPlayer.getName());

            this.displayTable();
            this.displayCollected(currentPlayer);
            this.displayHand(currentPlayer);

            // 4. Prompt for input EXCEPTION TBD // added option to play with bot
            Card cardPlaced = null;
            Scanner inputScanner = new Scanner(System.in);

            if (currentPlayer instanceof BotPlayer) { // instantiate bot player
                cardPlaced = ((BotPlayer) currentPlayer).cardToPlay(table.getParade());
                currentPlayer.removeFromHand(cardPlaced);
                System.out.println("Bot " + currentPlayer.getName() + " played: " + cardPlaced);
            } else {
                boolean validCard = false;

                while (!validCard) { // Prompts user until they input a valid card
                    try {

                        System.out.println("Enter card to put (index of card): ");
                        int cardNumber = inputScanner.nextInt();
                        

                        if (this.checkValidCardPlacement(cardNumber, currentPlayer)) {
                            cardPlaced = currentPlayer.getPlayerHand().get(cardNumber - 1);
                            currentPlayer.removeFromHand(cardPlaced);
                            validCard = true;
                        }

                    } catch (InputMismatchException e) {
                        System.out.println("Error: Invalid card value! Must be between 1 and " + (currentPlayer.getPlayerHand().size()));
                        inputScanner.nextLine();
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
            }

            System.out.println(currentPlayer.getName() + " placed " + cardPlaced);
            table.updateParade(cardPlaced, currentPlayer);

            this.displayTable();
            this.displayCollected(currentPlayer);
            DisplayUtility.printLine(1);

            // Draw card for player if not last round
            if (!isLastRound) {
                Card drawnCard = deck.draw();

                if (drawnCard != null) {
                    currentPlayer.addToHand(drawnCard);
                } else {
                    System.out.println("Deck ran out of cards!");
                    return; // Stop dealing if the deck is empty
                }

                // 7. Current deck count
                DisplayUtility.printLine(2);
                DisplayUtility.printDeckCount(deck.getCardCount());
                DisplayUtility.printLine(2);
            }

            try {
                Thread.sleep(roundDelayDuration);
            } catch (Exception e) {
                // Swallow exception
            }

        }
    }

    // Helper functions
    public void displayTable() {
        ArrayList<Card> tableCards = table.getParade();
        DisplayUtility.displayCardsAsArt("TABLE", tableCards, false);

        try {
            Thread.sleep(delayDuration);
        } catch (Exception e) {
            // Swallow exception
        }
    }

    public void displayHand(Player player) {
        DisplayUtility.displayCardsAsArt(player.getName() + "'s HAND", player.getPlayerHand(), true);

        try {
            Thread.sleep(delayDuration);
        } catch (Exception e) {
            // Swallow exception
        }
    }

    public void displayCollected(Player player) {
        DisplayUtility.displayCardsAsArt(player.getName() + "'s COLLECTION", player.getCollectedCards(), false);

        try {
            Thread.sleep(delayDuration);
        } catch (Exception e) {
            // Swallow exception
        }
    }

/*     public Card convertCard(String cardString) {
        try {
            String[] parts = cardString.trim().split("\\s+");
            if (parts.length != 2) {
                return null;
            }

            Card.Color color = Card.Color.valueOf(parts[0].toUpperCase());

            int value = Integer.parseInt(parts[1]);

            return new Card(color, value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    } */

    public void conductScoringRound() {
        // first prompt the user to discard two cards
        for (int currPlayerIndex = 0; currPlayerIndex < players.size(); currPlayerIndex++) {
            // set currPlayer to i so that it can rotate between players

            Player currentPlayer = players.get(currPlayerIndex);
            System.out.println("Current Player: " + currentPlayer.getName());


            this.displayTable();
            this.displayCollected(currentPlayer);
            DisplayUtility.printDivider("DISCARD FIRST CARD");
            this.displayHand(currentPlayer);

            // prompt user to discard 2 cards, then display the remaining cards, and the
            // updated collection of the player.
            int cardDiscardedIndex1 = 0;
            int cardDiscardedIndex2 = 0;
            Scanner inputScanner = new Scanner(System.in);

            // HANDLE BOT LOGIC IF REQUIRED


            boolean validCard1 = false;

                while (!validCard1) { // Prompts user until they input a valid card
                    try {

                        System.out.println("Enter first card to be discarded (index of card): ");
                        int cardNumber = inputScanner.nextInt();
                        

                        if (this.checkValidCardPlacement(cardNumber, currentPlayer)) {
                            cardDiscardedIndex1 = cardNumber;
                            validCard1 = true;
                        }

                    } catch (InputMismatchException e) {
                        System.out.println("Error: Invalid card value! Must be between 1 and " + (currentPlayer.getPlayerHand().size()));
                        inputScanner.nextLine();
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }

            Card firstCardDiscarded = currentPlayer.getPlayerHand().get(cardDiscardedIndex1 - 1);
            currentPlayer.removeFromHand(firstCardDiscarded);
            
            DisplayUtility.printDivider("DISCARD SECOND CARD");
            this.displayHand(currentPlayer);

            boolean validCard2 = false;

                while (!validCard2) { // Prompts user until they input a valid card
                    try {

                        System.out.println("Enter second card to be discarded (index of card): ");
                        int cardNumber = inputScanner.nextInt();
                        

                        if (this.checkValidCardPlacement(cardNumber, currentPlayer)) {
                            cardDiscardedIndex2 = cardNumber;
                            validCard2 = true;
                        }

                    } catch (InputMismatchException e) {
                        System.out.println("Error: Invalid card value! Must be between 1 and " + (currentPlayer.getPlayerHand().size()));
                        inputScanner.nextLine();
                    } 
                }

                Card secondCardDiscarded = currentPlayer.getPlayerHand().get(cardDiscardedIndex2 - 1);
                currentPlayer.removeFromHand(secondCardDiscarded);

            // try {
            //     System.out.println("Enter first card to be discard for Player " + currentPlayer.getName());
            //     int firstCardNumber = sc.nextInt();

            //     System.out.println("Enter Second card to be discard for Player " + currentPlayer.getName());
            //     int secondCardNumber = sc.nextInt();
            // } catch (InputMismatchException e) {
            //     System.out.println("Error: Invalid card value! Must be between 1 and " + (currentPlayer.getPlayerHand().size()));
            //     sc.nextLine();
            // } catch (Exception e) {
            //     System.out.println("Error: " + e.getMessage());
            // }

            // INPUT EXCEPTIONS HERE TBD

            // add rest to the collection
            for (Card remainingCards : currentPlayer.getPlayerHand()) {
                currentPlayer.collectCard(remainingCards);
            }

            this.displayCollected(currentPlayer);

            // calculate the sccores
            HashMap<Card.Color, Integer> majorityCardMap = getMajorityOfEachCard();
            currentPlayer.calculateScore(majorityCardMap, is2Players);
        }
    }

    public boolean checkValidCardPlacement(int cardNumber, Player currentPlayer) {

        if (cardNumber < 1 || cardNumber > currentPlayer.getPlayerHand().size()) {
            throw new CardException("Invalid card value! Must be between 1 and " + (currentPlayer.getPlayerHand().size()));
        }

        return true;
    }   

}
