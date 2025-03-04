import java.lang.reflect.Array;
import java.util.*;

public class Game {
    private List<Player> players = new ArrayList<>();
    private int playerNumber;

    public Game(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public int calculateScore(Player p) {
        //determine who has majority in each colour (with help of comparator class).
        //those with most cards in each color, the count will be +1. If got two or more players that hold majority, their cards
        //will be flipped over(flip to face down).
        //face down cards will now be scored as +1, and faceup cards will be their value.
    }

    public List<Integer> tabulateScore() {
        List<Integer> scores = new ArrayList<>();
        
        for (Player p : players) {
            scores.add(calculateScore(p));
        }

        return scores;
    }



}
