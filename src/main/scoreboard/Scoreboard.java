package scoreboard;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

import player.Player;

public class Scoreboard {
    public void readScore(String pathname) {
        try (Scanner sc = new Scanner(new File(pathname))) {
            sc.useDelimiter(",|\r\n|\n"); // ",|\r\n|\n" <- regular expression

            while (sc.hasNext()) {
                String name = sc.next(); // Read name as String
                double score = sc.nextDouble(); // Read score as double
                LocalDate date = LocalDate.parse(sc.next()); // Read and parse date

                System.out.println("Name: " + name);
                System.out.println("Score: " + score);
                System.out.println("Date: " + date);
                System.out.println("======================");
                
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    public void writeScore(String pathname, Player player) {
        try (PrintStream out = new PrintStream(new FileOutputStream(new File(pathname), true))) {
            out.println(player.getName() + "," + player.getScore() + "," + LocalDate.now()); // e.g. format -> Leandro,2000.69,2025-03-09

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }
}
