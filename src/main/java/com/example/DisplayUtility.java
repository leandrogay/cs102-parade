import java.util.*;

public class DisplayUtility {
    private static final String DIVIDER = "===================================================================================";

    // // ANSI color codes
    private static final String DEFAULT_COLOR = "\u001B[0m";
    private static final String RED_COLOR = "\u001B[31m";
    private static final String BLUE_COLOR = "\u001B[34m";
    private static final String GREEN_COLOR = "\u001B[32m";
    private static final String YELLOW_COLOR = "\u001B[33m";
    private static final String PURPLE_COLOR = "\u001B[35m";
    private static final String BLACK_COLOR = "\u001B[30m";

    /**
     * Prints a divider line with optional centered text.
     * If no text is provided, it prints a plain divider line.
     *
     * @param text The text to center within the divider. If null or empty, prints a plain divider.
     */
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

    /**
     * Displays a list of cards as ASCII art horizontally, showing only color and number.
     * Also displays the index of each card below the ASCII art, centered under each card.
     *
     * @param title The title of the display.
     * @param cards The list of cards to display.
     */
    public static void displayCardsAsArt(String title, ArrayList<Card> cards, boolean printIndex) {
        printDivider(title);

        if (cards.isEmpty()) {
            System.out.println("*No cards to display*");
            printDivider(null);
            return;
        }

        // Create ASCII art lines for each card
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

            // Add ASCII art for the card
            topLine.append("+-------+  ");
            colorLine.append(String.format("| %s%-6s%s|  ", colorCode, colorName, DEFAULT_COLOR)); // Card color with ANSI code
            valueLine.append(String.format("| %s%-6s%s|  ", colorCode, value, DEFAULT_COLOR)); // Card value with ANSI code
            bottomLine.append("+-------+  ");

            // Center index below each card
            int cardWidth = 9; // Width of "+-------+"
            int indexWidth = Integer.toString(i + 1).length(); // Length of the index number
            int padding = (cardWidth - indexWidth) / 2; // Calculate spaces needed on each side
            indexLine.append(" ".repeat(padding)).append(i + 1).append(" ".repeat(cardWidth - indexWidth - padding)).append("  ");
        }

        // Print the lines: Color above Value and Index below
        System.out.println(topLine);
        System.out.println(colorLine);
        System.out.println(valueLine);
        System.out.println(bottomLine);

        if (printIndex) {
            System.out.println(indexLine);
        }

        printDivider(null);
    }

    /**
     * Returns the ANSI color code for a given card color.
     *
     * @param color The card's color.
     * @return The corresponding ANSI escape code for the color.
     */
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
                return DEFAULT_COLOR; // Default to no color
        }
    }

    /**
     * Formats the color of a card as its full name.
     *
     * @param color The card's color.
     * @return The formatted color name in uppercase.
     */
    private static String formatColor(Card.Color color) {
        return color.name(); // Use full color name
    }

    /**
     * Formats the value of a card as a string.
     *
     * @param value The numeric value of the card.
     * @return The formatted value as a string.
     */
    private static String formatValue(int value) {
        return Integer.toString(value); // Simply convert to string
    }
}
