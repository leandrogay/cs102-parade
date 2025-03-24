import java.util.*;

public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of players: ");
        int playerCount = sc.nextInt();
        if (playerCount >= 2 && playerCount <= 6) { // exception needed temp for now
            Game game = new Game(playerCount);            

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


            //this is the round where the player chooses to discard 2, and place 2
            game.conductScoringRound();
            

            //tabulate scores and stuff and print winner

            //print winner
            ArrayList<Player> players = game.getPlayers();

            //print out each collection and hand

            for (Player p : players) {
                System.out.println("score for " + p.getName() + " is " + p.getScore());
                game.displayCollected(p);

            }


            ArrayList<Player> winners = game.getWinner();

            System.out.println("WINNERS: ");

            for (Player winner : winners) {
                System.out.println(winner.getName());
            }
            
            
            
        }  
    }
}