import java.util.*;

public class Game {
    private Deck deck;
    private Table table;
    private int playerNumber;
    private boolean is2Players;
    private ArrayList<Player> players = new ArrayList<>();
    private String divider = "===================================================================================";
    private int delayDuration = 1200;

    public Game(int playerNumber) {
        this.deck = new Deck();
        this.table = new Table();
        this.playerNumber = playerNumber;

        Scanner sc = new Scanner(System.in);

        for (int i = 0; i < this.playerNumber; i++) {
            System.out.print("Enter name for Player " + (i + 1) + ": ");
            String name = sc.nextLine();
            // nee testing
            if (name.equalsIgnoreCase("bot")) {
                players.add(new BotPlayer("BotPlayer" + (i + 1)));
            } else {
                players.add(new Player(name));
            }
            // nee testing end

            // og code
            // players.add(new Player(name));
            // og code end
        }

        dealStartingHands();
        setTable();
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

    // public int calculateScore(Player player) {
    // // determine who has majority in each colour (with help of comparator class).
    // // those with most cards in each color, the count will be +1. If got two or
    // more
    // // players that hold majority, their cards
    // // will be flipped over(flip to face down).
    // // face down cards will now be scored as +1, and faceup cards will be their
    // // value.
    // }
    // public HashMap<Color, Integer> getMajorityOfEachCard() {
    // HashMap<Color, ArrayList<Integer>> color_result_map = new HashMap<Color,
    // ArrayList<Integer>>();
    // for (Player player : players) {
    // ArrayList<Card> collected_cards = player.getCollectedCards();

    // }
    // }
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
        // deck is exhausted
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
                printDivider("LAST ROUND");
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

                while (!validCard) { // Keep asking for a valid card
                    try {

                        System.out.println("Enter card to put (color, then value): ");
                        String cardString = inputScanner.nextLine();
                        cardPlaced = this.convertCard(cardString);

                        if (this.checkValidCardPlacement(cardPlaced, currentPlayer)) {
                            currentPlayer.removeFromHand(cardPlaced);
                            validCard = true;
                        }

                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
            }

            System.out.println("Card placed is " + cardPlaced);
            table.updateParade(cardPlaced, currentPlayer);

            // 6. Provide feedback - "Cards collected: 1red, 7blue, 8yellow" DONE

            this.displayCollected(currentPlayer);
            System.out.println();
            this.displayTable();

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
                System.out.println("Current deck count is " + deck.getCardCount());
            }
        }
    }

    // Helper functions

    public void displayTable() {
        printDivider("TABLE");
        // System.out.println("Table: ");
        ArrayList<Card> tableCards = table.getParade();
        System.out.println(tableCards);
        printDivider(null);
        try {
            Thread.sleep(delayDuration);
        } catch (Exception e) {
        }
    }

    public void displayHand(Player player) {
        printDivider(player.getName() + "'s HAND");
        // System.out.println(player.getName() + " Hand: ");
        System.out.println(player.getPlayerHand());
        printDivider(null);
        System.out.println();
        try {
            Thread.sleep(delayDuration);
        } catch (Exception e) {
        }
    }

    public void displayCollected(Player player) {
        printDivider("COLLECTION");

        if (player.getCardCollection().isEmpty()) {
            System.out.println("*collection is empty for " + player.getName() + "*");
            printDivider(null);
            return;
        }

        System.out.println(player.getName() + " Collection: ");
        System.out.println(player.getCollectedCards());
        printDivider(null);

        try {
            Thread.sleep(delayDuration);
        } catch (Exception e) {
        }
    }

    public void printDivider(String text) {
        if (text != null && !text.isEmpty()) {
            int dividerLength = divider.length();
            String paddedText = " " + text + " ";
            int textLength = paddedText.length();
            int padding = (dividerLength - textLength) / 2;

            // Handle cases where the padded text length is greater than the divider length
            if (textLength > dividerLength) {
                System.out.println(paddedText.substring(0, dividerLength));
            } else {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < padding; i++) {
                    sb.append("=");
                }
                sb.append(paddedText);
                for (int i = 0; i < dividerLength - padding - textLength; i++) {
                    sb.append("=");
                }
                System.out.println(sb.toString());
            }
        } else {
            System.out.println();
        }
    }

    public Card convertCard(String cardString) {
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
    }

    public void conductScoringRound() {
        // first prompt the user to discard two cards
        for (int currPlayerIndex = 0; currPlayerIndex < players.size(); currPlayerIndex++) {

            // set currPlayer to i so that it can rotate between players

            Player currentPlayer = players.get(currPlayerIndex);

            printDivider("DISCARD TWO CARDS");

            // prompt user to discard 2 cards, then display the remaining cards, and the
            // updated collection of the player.
            Scanner sc = new Scanner(System.in);

            this.displayHand(currentPlayer);

            System.out.println("Enter first card to be discard for Player " + currentPlayer.getName());
            String firstCardString = sc.nextLine();

            System.out.println("Enter Second card to be discard for Player " + currentPlayer.getName());
            String secondCardString = sc.nextLine();

            // INPUT EXCEPTIONS HERE TBD

            Card firstCardDiscarded = this.convertCard(firstCardString);

            Card secondCardDiscarded = this.convertCard(secondCardString);

            currentPlayer.removeFromHand(firstCardDiscarded);
            currentPlayer.removeFromHand(secondCardDiscarded);

            // add rest to the collection

            for (Card remainingCards : currentPlayer.getPlayerHand()) {
                currentPlayer.collectCard(remainingCards);
            }

            this.displayCollected(currentPlayer);

            // calculate the sccores
            HashMap<Card.Color, Integer> playerCardMap = currentPlayer.getCardCollection();
            HashMap<Card.Color, Integer> majorityCardMap = getMajorityOfEachCard();
            currentPlayer.calculateScore(majorityCardMap, is2Players);
        }
    }

    public boolean checkValidCardPlacement(Card cardPlaced, Player currentPlayer) {

        if (cardPlaced == null) {
            throw new CardException("Invalid card!");
        }

        if (cardPlaced.getValue() < 0 || cardPlaced.getValue() > 10) {
            throw new CardException("Invalid card value! Must be between 0 and 10.");
        }

        if (!currentPlayer.getPlayerHand().contains(cardPlaced)) {
            throw new CardException("Card not found in player's hand!");
        }

        return true;
    }
}
