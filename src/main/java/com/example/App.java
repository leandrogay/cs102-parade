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

            int currPlayerIndex = 0;
            while (!game.checkLastRound()) {
                for (int i = 0 ; i < players.size() ; i++) {
                    // 1. Display the current table ()
                    // 2. Display player collection
                    // 3. Display player hand
                    // 4. Prompt for input
                    // 5. Put card - follow up with the logic
                    // 6. Provide feedback - "Cards collected: 1red, 7blue, 8yellow"
                    // 7. Current deck count

                    currPlayerIndex = i;
                }
            }

            // one more round
            // 1. Notify that it is the last round - ""
            // 2. repeat contents of for loop 
            
        }  
    }
}