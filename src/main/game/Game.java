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
    private List<Player> players = new ArrayList<>();
    private int cardsPerPlayer = 5;
    private int cardsPerTable = 6;
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

    public void playerSettings() {
        Scanner sc = new Scanner(System.in);

        boolean validPlayerSetup = false;

        while (!validPlayerSetup) {
            players.clear(); // clear player list
            botCount = 0; // reset number of bots

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
                        if (name.equals("")) {
                            System.out.println("ERROR! Invalid input. Please enter valid name!");
                            i--;
                        } else {
                            players.add(new Player(name));
                        }
                        break;
                    case "":  // handling for if user presses enter with no input 
                        System.out.println();
                        System.out.println("ERROR! Invalid input. Please enter either y or n");
                        System.out.println();
                        i--; // to reset player setup
                        break;
                    default:
                        System.out.println();
                        System.out.println("ERROR! Invalid input. Please enter either y or n");
                        System.out.println();
                        i--; // to reset player setup 
                        break;
                }
            }

            // Checks if all players are bots (all players should not be bots)
            if (botCount == playerNumber) {
                System.out.println();
                System.out.println("ERROR! At least one human player is required. Restarting player setup."); 
                System.out.println();
            } else {
                validPlayerSetup = true;
            }
        }
    }

    public void dealStartingHands() { // Distributes 5 cards to each player
        for (int i = 0; i < cardsPerPlayer; i++) { // Iterates 5 times
            for (Player player : players) { // Iterates through players in order
                Card drawnCard = deck.draw();
                if (drawnCard != null) {
                    player.addToHand(drawnCard);
                } else {
                    System.out.println("Deck ran out of cards!");
                    return; // Stops dealing if the deck is empty
                }
            }
        }
    }

    public void setTable() { // Draws and places 6 cards to the table
        for (int i = 0; i < cardsPerTable; i++) {
            Card drawnCard = deck.draw();
            if (drawnCard != null) {
                table.addCardToParade(drawnCard);
            } else {
                System.out.println("Deck ran out of cards!");
                return; // Stops dealing if the deck is empty
            }
        }
    }

    public static void startGame(int playerCount) {
        if (playerCount >= 2 && playerCount <= 6) {
            Game game = new Game(playerCount);

            while (!game.checkLastRound()) {
            game.conductRound(); // Conducts round while not last round
            }

            DisplayUtility.printDivider("LAST ROUND");
            game.conductRound();
            game.conductScoringRound();

            List<Player> players = game.getPlayers();
            DisplayUtility.printDivider("SCORES");

            for (Player player : players) {
                System.out.println("score for " + player.getName() + " is " + player.getScore());
                game.displayCollected(player);
            }

            List<Player> winners = game.getWinner();
            DisplayUtility.printDivider("WINNERS");
            System.out.println("WINNERS: ");

            for (Player winner : winners) {
                System.out.println(winner.getName());
            }
        }

        System.out.println();
        System.out.print("Do you want to play again? (y/n): ");
        Scanner sc = new Scanner(System.in);
        String playAgain = sc.nextLine().trim().toLowerCase();
        switch (playAgain) {
            case "y":
                System.out.println("Starting new game.");
                Game.startGame(Menu.getPlayerCount());
                break;
            case "n":
                System.out.println();
                System.out.println("You have quit the game.");
                System.out.println();
                System.exit(0);
            default:
                System.out.println();
                System.out.println("ERROR! Invalid input. Please enter either y or n");
                System.out.println();
                break;
        }
    }

    public List<Card> getDeck() {
        return deck.getDeck();
    }

    public List<Card> getParade() {
        return table.getParade();
    }


    public HashMap<Card.Color, Integer> getMajorityOfEachCard() {
        HashMap<Card.Color, Integer> colorMajorityMap = new HashMap<>();
    
        for (Card.Color color : Card.Color.values()) {
            int maxCount = 0;
            int countOfMaxPlayers = 0;
    
            for (Player player : players) {
                int count = player.getCardCollection().getOrDefault(color, 0);
    
                if (count > maxCount) {
                    maxCount = count;
                    countOfMaxPlayers = 1;
                } else if (count == maxCount && maxCount > 0) {
                    countOfMaxPlayers++;
                }
            }
    
            if (countOfMaxPlayers > 1) {
                colorMajorityMap.put(color, -1); // -1 indicates a tie
            } else {
                colorMajorityMap.put(color, maxCount);
            }
        }
        System.out.println(colorMajorityMap);
        return colorMajorityMap;
    }

    public List<Player> getPlayers() {
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
            if (player.getNumColors() == 6) { // Player has all colors of the cards
                return true;
            }
        }
        return false;
    }

    public boolean checkLastRound() {
        // If either of the conditions are fulfilled, the last round begins
        return drawPileExhausted() || checkAllColors();
    }

    public void collectCardsFromParade(Player player, Card playedCard) {
        // Changed direction to make it easier for removal condition.
        table.changeDirection();

        List<Card> parade = table.getParade();
        List<Card> cardsToCollect = new ArrayList<>();

        int startIndex = Math.max(0, playedCard.getValue() - 1);
        for (int i = startIndex; i < parade.size(); i++) {
            Card c = parade.get(i);
            if (c.getColor() == playedCard.getColor() || c.getValue() <= playedCard.getValue()) {
                cardsToCollect.add(c);
            }
        }

        for (Card c : cardsToCollect) {
            player.collectCard(c);
            table.removeCard(c);
        }

        // Sets the direction back to LEFT_TO_RIGHT and adds the played card to the parade
        table.changeDirection();
        table.addCardToParade(playedCard);

        Card drawnCard = deck.draw();
        // Player draws card after end of turn
        player.addToHand(drawnCard);

        System.out.println(player.getName() + " collected: " + cardsToCollect);
    }

    public List<Player> getWinner() {
        List<Integer> scores = tabulateScore();
        int lowestScore = Collections.min(scores); // Finds the lowest score
        ArrayList<Player> lowestScorePlayers = new ArrayList<>();

        for (Player p : players) {
            if (p.getScore() == lowestScore) {
                lowestScorePlayers.add(p);
            }
        }
        if (lowestScorePlayers.size() == 1) {
            return lowestScorePlayers;
        }

        // TIE BREAKER - returns an ArrayList in case of another tie
        List<Integer> cardCounts = new ArrayList<>();
        for (Player p : lowestScorePlayers) {
            cardCounts.add(p.getCollectedCards().size());
        }
        int lowestCardCount = Collections.min(cardCounts);

        List<Player> winners = new ArrayList<>();
        for (Player p : lowestScorePlayers) {
            if (p.getCollectedCards().size() == lowestCardCount) {
                winners.add(p);
            }
        }
        return winners;
    }

    public void conductRound() {
        System.out.println();
        for (Player currentPlayer : players) {
            boolean isLastRound = this.checkLastRound();

            if (isLastRound) {
                DisplayUtility.printDivider("LAST ROUND");
            }

            System.out.println("Current Player: " + currentPlayer.getName());

            this.displayTable();
            this.displayCollected(currentPlayer);
            this.displayHand(currentPlayer);

            Card cardPlaced = null;
            Scanner inputScanner = new Scanner(System.in);

            if (currentPlayer instanceof BotPlayer bot) {
                cardPlaced = bot.cardToPlay(table.getParade());
                currentPlayer.removeFromHand(cardPlaced);
                System.out.println(bot.getName() + " played: " + cardPlaced);
            } else {
                boolean validCard = false;
                while (!validCard) {
                    try {
                        System.out.println("Enter card to put (index of card) or 'q' to quit: ");
                        String input = inputScanner.nextLine().trim().toLowerCase();
                        
                        if (input.equals("q")) {
                            System.out.println();
                            System.out.println("You have quit the game.");
                            System.out.println();
                            System.exit(0);
                        }
                        
                        int cardNumber = Integer.parseInt(input);
                        if (this.checkValidCardPlacement(cardNumber, currentPlayer)) {
                            cardPlaced = currentPlayer.getPlayerHand().get(cardNumber - 1);
                            currentPlayer.removeFromHand(cardPlaced);
                            validCard = true;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println();
                        System.out.println("ERROR! Please enter a valid number or 'q' to quit");
                        System.out.println();
                    } catch (InputMismatchException e) {
                        System.out.println();
                        System.out.println("ERROR! Invalid card value! Must be between 1 and " + currentPlayer.getPlayerHand().size());
                        System.out.println();
                        inputScanner.nextLine();
                    } catch (Exception e) {
                        System.out.println();
                        System.out.println("ERROR! " + e.getMessage());
                        System.out.println();
                    }
                }
            }

            System.out.println(currentPlayer.getName() + " placed " + cardPlaced);
            table.updateParade(cardPlaced, currentPlayer);

            this.displayTable();
            this.displayCollected(currentPlayer);
            DisplayUtility.printLine(1);

            if (!isLastRound) {
                Card drawnCard = deck.draw();
                if (drawnCard != null) {
                    currentPlayer.addToHand(drawnCard);
                } else {
                    System.out.println("Deck ran out of cards!");
                    return;
                }

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
        List<Card> tableCards = table.getParade();
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

    public void conductScoringRound() {
        for (Player currentPlayer : players) {
            int cardDiscardedIndex1 = 0;
            int cardDiscardedIndex2 = 0;
            boolean validCard1 = false;
            boolean validCard2 = false;
    
            this.displayTable();
            this.displayCollected(currentPlayer);
            DisplayUtility.printDivider("DISCARD FIRST CARD");
            this.displayHand(currentPlayer);
    
            if (currentPlayer instanceof BotPlayer bot) {
                bot.botDiscard();
            } else {
                Scanner inputScanner = new Scanner(System.in);
    
                while (!validCard1) {
                    try {
                        System.out.println("Enter first card to be discarded (index of card) or 'q' to quit: ");
                        String input = inputScanner.nextLine().trim().toLowerCase();
                        
                        if (input.equals("q")) {
                            System.out.println();
                            System.out.println("You have quit the game.");
                            System.out.println();
                            System.exit(0);
                        }
                        
                        int cardNumber = Integer.parseInt(input);
                        if (this.checkValidCardPlacement(cardNumber, currentPlayer)) {
                            cardDiscardedIndex1 = cardNumber;
                            validCard1 = true;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println();
                        System.out.println("ERROR! Please enter a valid number or 'q' to quit");
                        System.out.println();
                    } catch (InputMismatchException e) {
                        System.out.println();
                        System.out.println("ERROR! Invalid card value! Must be between 1 and " + currentPlayer.getPlayerHand().size());
                        System.out.println();
                        inputScanner.nextLine();
                    } catch (Exception e) {
                        System.out.println();
                        System.out.println("ERROR! " + e.getMessage());
                        System.out.println();
                    }
                }
    
                Card firstCardDiscarded = currentPlayer.getPlayerHand().get(cardDiscardedIndex1 - 1);
                currentPlayer.removeFromHand(firstCardDiscarded);
                
                DisplayUtility.printDivider("DISCARD SECOND CARD");
                this.displayHand(currentPlayer);
    
                while (!validCard2) { // Prompts user until they input a valid card
                    try {
                        System.out.println("Enter second card to be discarded (index of card) or 'q' to quit: ");
                        String input = inputScanner.nextLine().trim().toLowerCase();
                        
                        if (input.equals("q")) {
                            System.out.println();
                            System.out.println("You have quit the game.");
                            System.out.println();
                            System.exit(0);
                        }
                        
                        int cardNumber = Integer.parseInt(input);
                        if (this.checkValidCardPlacement(cardNumber, currentPlayer)) {
                            cardDiscardedIndex2 = cardNumber;
                            validCard2 = true;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println();
                        System.out.println("ERROR! Please enter a valid number or 'q' to quit");
                        System.out.println();
                    } catch (InputMismatchException e) {
                        System.out.println();
                        System.out.println("ERROR! Invalid card value! Must be between 1 and " + currentPlayer.getPlayerHand().size());
                        System.out.println();
                        inputScanner.nextLine();
                    } catch (Exception e) {
                        System.out.println();
                        System.out.println("ERROR! " + e.getMessage());
                        System.out.println();
                    }
                }
    
                Card secondCardDiscarded = currentPlayer.getPlayerHand().get(cardDiscardedIndex2 - 1);
                currentPlayer.removeFromHand(secondCardDiscarded);
            }
    
            // add rest to the collection
            for (Card remainingCards : currentPlayer.getPlayerHand()) {
                currentPlayer.collectCard(remainingCards);
            }
    
            this.displayCollected(currentPlayer);
        }

        // Calculate the scores
        HashMap<Card.Color, Integer> majorityCardMap = getMajorityOfEachCard();
        if (players.size() == 2) {
            Player player1 = players.get(0);
            Player player2 = players.get(1);
            player1.calculateScore(null, true, player2.getCardCollection());
            player2.calculateScore(null, true, player1.getCardCollection());
        }
        else {
            for (Player currentPlayer : players) {
                currentPlayer.calculateScore(majorityCardMap, false, null);
            }
        }
    }

    public boolean checkValidCardPlacement(int cardNumber, Player currentPlayer) {
        try {
            if (cardNumber < 1 || cardNumber > currentPlayer.getPlayerHand().size()) {
                System.out.println();
                throw new CardException(
                        "ERROR! Invalid card value! Must be between 1 and " + currentPlayer.getPlayerHand().size());
            }
        } catch (CardException e) {
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }
}
