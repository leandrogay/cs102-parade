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
            players.add(new Player(name));
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
            // scores.add(calculateScore(player));
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
        for (int i = playedCard.getValue() - 1; i < parade.size(); i++){ // since we have to skip n (played cards value) number of cards
            Card c = parade.get(i);
            if (c.getColor() == playedCard.getColor() || c.getValue() <= playedCard.getValue()) {
                cardsToCollect.add(c);
            }
        }


        for (Card c : cardsToCollect){
            player.collectCard(c);
            table.removeCard(c);
        }



        System.out.println(player.getName() + " collected: " + cardsToCollect);
    }

}
