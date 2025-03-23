import java.util.*;

public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of players: ");
        int playerCount = sc.nextInt();
        if (playerCount >= 2 && playerCount <= 6) { // exception needed temp for now
            Game game = new Game(playerCount);
            ArrayList<Player> players = game.getPlayers();
            

            // System.out.println("Display Table");
            // System.out.println("=============");
            // for (Card card : game.getParade()) {
            //     System.out.println(card);
            // }
            // System.out.println("=============");

            // for (Player player : game.getPlayers()){
            //     System.out.println(player.getName());
            //     System.out.println("=============");
            //     for (Card card : player.getPlayerHand()) {
            //         System.out.println(card);
            //     }
            //     System.out.println("=============");
            // }
            //

            while (!game.checkLastRound()) {
                game.conductRound();
                break;
            }

            // one more round
            // 1. Notify that it is the last round - ""
            // 2. repeat contents of for loop 

            game.conductRound();
            

            //tabulate scores and stuff and print winner
            
        }  
    }
}