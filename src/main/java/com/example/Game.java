import java.util.*;

public class Game {
    private Deck deck;
    private Table table;
    private int playerNumber;
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

    // public int calculateScore(Player player) {
    //     // determine who has majority in each colour (with help of comparator class).
    //     // those with most cards in each color, the count will be +1. If got two or more
    //     // players that hold majority, their cards
    //     // will be flipped over(flip to face down).
    //     // face down cards will now be scored as +1, and faceup cards will be their
    //     // value.
    // }


    public ArrayList<Player> getPlayers() {
        return players;
    }

    public List<Integer> tabulateScore() {
        List<Integer> scores = new ArrayList<>();

        for (Player player : players) {
            scores.add(calculateScore(player));
        }

        return scores;
    }

    public boolean drawPileExhausted() {
        if (deck.getCardCount() == 0 ) { // deck is exhausted
            return true;
        }
        return false;
    }

    
}
