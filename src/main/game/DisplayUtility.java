package game;

import card.*;
import java.util.*;

public class DisplayUtility {
    private static final String DIVIDER = "===================================================================================";
    private static final String DEFAULT_COLOR = "\u001B[0m";
    private static final String RED_COLOR = "\u001B[31m";
    private static final String BLUE_COLOR = "\u001B[34m";
    private static final String GREEN_COLOR = "\u001B[32m";
    private static final String YELLOW_COLOR = "\u001B[33m";
    private static final String PURPLE_COLOR = "\u001B[35m";
    private static final String BLACK_COLOR = "\u001B[30m";

    public static void printDivider(String text) {
        if (text != null && !text.isEmpty()) {
            int dividerLength = DIVIDER.length();
            String paddedText = " " + text + " ";
            int textLength = paddedText.length();
            int padding = (dividerLength - textLength) / 2;

            if (textLength > dividerLength) {
                System.out.println(paddedText.substring(0, dividerLength));
            } else {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < padding; i++) {
                    sb.append("=");
                }
                sb.append(paddedText);
                for (int i = 0; i < dividerLength - padding - textLength; i++) {
                    sb.append("=");
                }
                System.out.println(sb.toString());
            }
        } else {
            System.out.println(DIVIDER);
        }
    }

    public static void printDeckCount(int deckCount) {
        printDivider(deckCount + " cards left");
    }

    public static void printLine(int numOfLines) {
        for (int i = 0; i < numOfLines; i++) {
            System.out.println();
        }
    }

    public static void displayCardsAsArt(String title, List<Card> cards, boolean printIndex) {
        printDivider(title);

        if (cards.isEmpty()) {
            System.out.println("*No cards to display*");
            printDivider(null);
            return;
        }

        StringBuilder topLine = new StringBuilder();
        StringBuilder colorLine = new StringBuilder();
        StringBuilder valueLine = new StringBuilder();
        StringBuilder bottomLine = new StringBuilder();
        StringBuilder indexLine = new StringBuilder();

        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            String colorCode = getColorCode(card.getColor());
            String colorName = formatColor(card.getColor());
            String value = formatValue(card.getValue());

            topLine.append("+-------+  ");
            colorLine.append(String.format("| %s%-6s%s|  ", colorCode, colorName, DEFAULT_COLOR));
            if (card.getIsFlipped()) {
                valueLine.append(String.format("| %s%-6s%s|  ", colorCode, " ", DEFAULT_COLOR));
            } else{
                valueLine.append(String.format("| %s%-6s%s|  ", colorCode, value, DEFAULT_COLOR));
            }
            bottomLine.append("+-------+  ");

            int cardWidth = 9;
            int indexWidth = Integer.toString(i + 1).length();
            int padding = (cardWidth - indexWidth) / 2;
            indexLine.append(" ".repeat(padding)).append(i + 1).append(" ".repeat(cardWidth - indexWidth - padding))
                    .append("  ");
        }

        System.out.println(topLine);
        System.out.println(colorLine);
        System.out.println(valueLine);
        System.out.println(bottomLine);

        if (printIndex) {
            System.out.println(indexLine);
        }

        printDivider(null);
    }

    private static String getColorCode(Card.Color color) {
        switch (color) {
            case RED:
                return RED_COLOR;
            case BLUE:
                return BLUE_COLOR;
            case GREEN:
                return GREEN_COLOR;
            case YELLOW:
                return YELLOW_COLOR;
            case PURPLE:
                return PURPLE_COLOR;
            case BLACK:
                return BLACK_COLOR;
            default:
                return DEFAULT_COLOR;
        }
    }

    private static String formatColor(Card.Color color) {
        return color.name();
    }

    private static String formatValue(int value) {
        return Integer.toString(value);
    }
}
