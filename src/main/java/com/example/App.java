import java.util.*;

public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of players: ");
        int playerCount = sc.nextInt();
        if (playerCount >= 2 && playerCount <= 6) { // exception needed temp for now
            Game game = new Game(playerCount);
            for (Card card : game.getParade()) {
                System.out.println(card);
            }
        }
    }
}