import java.util.*;

public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println();
        System.out.print("Enter number of players: ");
        int playerCount = sc.nextInt();
        System.out.println();
        if (playerCount >= 2 && playerCount <= 6) { // exception needed temp for now
            Game game = new Game(playerCount);
            int cheat = 2;
            
            while (cheat > 0) {
            // while (!game.checkLastRound()) {
                game.conductRound(); // conduct round while not last round 
                cheat--;
            }

            // game.conductScoringRound(); // for scoring

            // one more round
            // 1. Notify that it is the last round - ""
            // 2. repeat contents of for loop 
            System.out.println("=====LAST ROUND=====");
            game.conductRound();
            //this is the round where the player chooses to discard 2, and place 2
            game.conductScoringRound();

            //tabulate scores and stuff and print winner

            //print winner
            ArrayList<Player> players = game.getPlayers();

            //print out each collection and hand

            System.out.println("=====SCORES=====");


            for (Player p : players) {
                System.out.println("score for " + p.getName() + " is " + p.getScore());
                game.displayCollected(p);
            }

            ArrayList<Player> winners = game.getWinner();
            System.out.println("=====WINNERS=====");
            System.out.println("WINNERS: ");

            for (Player winner : winners) {
                System.out.println(winner.getName());
            }            
        }  
    }
}