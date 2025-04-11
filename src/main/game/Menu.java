package game;

import java.util.*;

public class Menu {  

    public static void select(){
        Scanner sc = new Scanner(System.in);
        int choice;
        do { // do code below while choice is not 2 (choice = 2 means quit game)
            displayHome(); // display initial options (start or quit)

            String input = "";
            System.out.print("Please enter your choice: ");
            input = sc.nextLine();
            choice = 0;
            if (!input.isBlank()) {
                try {
                    choice = Integer.parseInt(input);
                    switch (choice) {
                        case 1:
                            Game.startGame(getPlayerCount());
                            break;

                        case 2:
                            System.out.println();
                            System.out.println("You have quit the game.");
                            System.out.println();
                            break;

                        default:
                            System.out.println();
                            System.out.println("ERROR! Please enter either 1 or 2");
                            System.out.println();
                            break;
                    }
                } catch (NumberFormatException e){
                    System.out.println();
                    System.out.println("ERROR! Please enter a number.");
                    System.out.println();
                }
            } 
        } while (choice != 2); // quit game 
    }


    public static int getPlayerCount(){
        Scanner sc = new Scanner(System.in);
        System.out.println();
        int playerCount = 0;

        boolean isValidPlayerCount = false;

        while (!isValidPlayerCount) {
            try {
                System.out.print("Enter number of players: ");
                playerCount = sc.nextInt();

                if (playerCount >= 2 && playerCount <= 6) {
                    isValidPlayerCount = true;
                } else {
                    System.out.println();
                    System.out.println("ERROR! Player number be an integer value between 2 and 6");
                    System.out.println();
                }

            } catch (InputMismatchException e) {
                System.out.println();
                System.out.println("ERROR! Invalid Input");
                System.out.println();
                sc.nextLine();
            }
        }

        System.out.println();

        return playerCount;
    }

    private static void displayHome(){
        System.out.println("Press [1] to start a new game!");
        System.out.println("Press [2] to quit.");
    }

}
