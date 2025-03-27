import java.util.*;

public class Game {
    private Deck deck;
    private Table table;
    private int playerNumber;
    private boolean is2Players;
    private ArrayList<Player> players = new ArrayList<>();

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
    //     // determine who has majority in each colour (with help of comparator class).
    //     // those with most cards in each color, the count will be +1. If got two or more
    //     // players that hold majority, their cards
    //     // will be flipped over(flip to face down).
    //     // face down cards will now be scored as +1, and faceup cards will be their
    //     // value.
    // }
    // public HashMap<Color, Integer> getMajorityOfEachCard() {
    //     HashMap<Color, ArrayList<Integer>> color_result_map = new HashMap<Color, ArrayList<Integer>>();
    //     for (Player player : players) {
    //         ArrayList<Card> collected_cards = player.getCollectedCards();

    //     }   
    // }
    public HashMap<Card.Color, Integer> getMajorityOfEachCard() { // returns a map mapping each card to its majority holder number
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
            Integer max_color = Collections.max(color_num_array);
            color_majority_map.put(color, max_color);
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

    public void collectCardsFromParade(Player player, Card playedCard){
        // Changed direction to make it easier for removal condition.
        table.changeDirection();

        ArrayList<Card> parade = table.getParade();
        ArrayList<Card> cardsToCollect = new ArrayList<>();

        int startIndex = Math.max(0, playedCard.getValue() - 1);
        for (int i = startIndex; i < parade.size(); i++){

        //for (int i = playedCard.getValue() - 1; i < parade.size(); i++){ // since we have to skip n (played cards value) number of cards
        
            Card c = parade.get(i);
            if (c.getColor() == playedCard.getColor() || c.getValue() <= playedCard.getValue()) {
                cardsToCollect.add(c);
            }
        }

        for (Card c : cardsToCollect){
            player.collectCard(c);
            table.removeCard(c);
        }

        //sets the direction back to LEFT_TO_RIGHT and adds the played card to the parade
        table.changeDirection();
        table.addCardToParade(playedCard);

        Card drawnCard = deck.draw();
        // player draws card after end of turn (should i use the addToHand method or collectCard method for this)
        player.addToHand(drawnCard);

        System.out.println(player.getName() + " collected: " + cardsToCollect);
    }

    public ArrayList<Player> getWinner(){
        int lowestScore = Integer.MAX_VALUE;

        ArrayList<Player> winners = new ArrayList<>();
        for (Player p : players){
            if (p.getScore() < lowestScore){
                lowestScore = p.getScore();
            }
        }

        for (Player p : players){
            System.out.println("Player score for " + p.getName()  + " is " + p.getScore());
            if (p.getScore() == lowestScore){
                winners.add(p);
            }
        }

        return winners;
    }

    /* public ArrayList<Player> getWinner() {
        List<Integer> scores = tabulateScore(); 
        int highestScore = Collections.max(scores); // Find the highest score
        ArrayList<Player> winners = new ArrayList<>();
    

        for (Player p : players) {
            if (p.getScore() == highestScore) {
                winners.add(p);
            }
        }
    
        return winners;
    } */



    public void conductRound() {
        for (int currPlayerIndex  = 0; currPlayerIndex < players.size(); currPlayerIndex ++) {
            
            boolean isLastRound = this.checkLastRound();

            if (isLastRound) {
                System.out.println("====LAST ROUND====");
            }

            //set currPlayer to i so that it can rotate between players
        
            Player currentPlayer = players.get(currPlayerIndex);

            System.out.println("Current Player: Player " + currentPlayer.getName());

        
            // 1. Display the current table () DONE
            // 2. Display player collection DONE
            // 3. Display player hand DONE

            this.displayTable();
            this.displayCollected(currentPlayer);
            this.displayHand(currentPlayer);

        // 4. Prompt for input EXCEPTION TBD // added option to play with bot 

        Card cardPlaced;
        Scanner inputScanner = new Scanner(System.in);
        
        if (currentPlayer instanceof BotPlayer) {  // instantiate bot player 
            cardPlaced = ((BotPlayer) currentPlayer).cardToPlay(table.getParade());
            currentPlayer.removeFromHand(cardPlaced);
            System.out.println("Bot " + currentPlayer.getName() + " played: " + cardPlaced);
        } else {
            System.out.println("Enter card to put (color, then value): ");
            String cardString = inputScanner.nextLine();
            cardPlaced = this.convertCard(cardString);
            currentPlayer.removeFromHand(cardPlaced);
        }

        System.out.println("Card placed is " + cardPlaced);
        table.updateParade(cardPlaced, currentPlayer);


        // 6. Provide feedback - "Cards collected: 1red, 7blue, 8yellow" DONE

        this.displayCollected(currentPlayer);

        this.displayTable();

        //Draw card for player if not last round
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

        //Wait for key
        System.out.println("Press Enter to continue to the next turn...");
        inputScanner.nextLine(); // Wait for Enter key


        } //end of each iteration
    }

    //Helper functions

    public void displayTable() {

        System.out.println("Current Table");

        ArrayList<Card> tableCards = table.getParade();
        int position = 1;
        for (Card card : tableCards) {
            System.out.printf("%d: ", position);
            position++;
            System.out.println(card);
        }

        System.out.println("==============");

    }

    public void displayHand(Player player) {

        System.out.println("Current Hand: " + player.getName());

        for (Card card : player.getPlayerHand()) {
            System.out.println(card);
        }

        System.out.println("==============");

    }

    public void displayCollected(Player player) {

        if (player.getCardCollection().isEmpty()) {
            System.out.println("Card Collection is empty for " + player.getName());
            System.out.println("==============");
            return;
        }

        System.out.println("Current Collection: " + player.getName());


        for (Card card : player.getCollectedCards()) {
            System.out.println(card);
        }
        System.out.println("==============");

    }

    public Card convertCard(String cardString) {
        try {
            String[] parts = cardString.trim().split("\\s+"); 
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid card format. Use 'COLOR VALUE' (e.g., 'RED 5').");
            }
    
            Card.Color color = Card.Color.valueOf(parts[0].toUpperCase());
    
            int value = Integer.parseInt(parts[1]);
    
            return new Card(color, value);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }


    public void conductScoringRound() {
        // first prompt the user to discard two cards
        for (int currPlayerIndex  = 0; currPlayerIndex < players.size(); currPlayerIndex ++) {
            
            //set currPlayer to i so that it can rotate between players
        
            Player currentPlayer = players.get(currPlayerIndex);

            System.out.println("=====DISCARD TWO CARDS=====");

            //prompt user to discard 2 cards, then display the remaining cards, and the updated collection of the player.
            Scanner sc = new Scanner(System.in);

            this.displayHand(currentPlayer);

            System.out.println("Enter first card to be discard for Player " + currentPlayer.getName());
            String firstCardString = sc.nextLine();

            System.out.println("Enter Second card to be discard for Player " + currentPlayer.getName());
            String secondCardString = sc.nextLine();
            
            //INPUT EXCEPTIONS HERE TBD

            Card firstCardDiscarded = this.convertCard(firstCardString);

            Card secondCardDiscarded = this.convertCard(secondCardString);

            currentPlayer.removeFromHand(firstCardDiscarded);
            currentPlayer.removeFromHand(secondCardDiscarded);

            //add rest to the collection
            
            for (Card remainingCards : currentPlayer.getPlayerHand()) {
                currentPlayer.collectCard(remainingCards);
            }

            this.displayCollected(currentPlayer);


            //calculate the sccores
            HashMap<Card.Color, Integer> playerCardMap = currentPlayer.getCardCollection();
            currentPlayer.calculateScore(playerCardMap, is2Players);



        }
    }
}
