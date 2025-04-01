import java.util.*;

public class DisplayUtility {
    private static final String DIVIDER = "===================================================================================";

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
     *
     * @param title The title of the display.
     * @param cards The list of cards to display.
     */
    public static void displayCardsAsArt(String title, ArrayList<Card> cards) {
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
    
        for (Card card : cards) {
            String color = formatColor(card.getColor());
            String value = formatValue(card.getValue());
    
            // Add ASCII art for the card
            topLine.append("+-------+  ");
            colorLine.append(String.format("| %-6s|  ", color)); // Card color
            valueLine.append(String.format("| %-6s|  ", value)); // Card value
            bottomLine.append("+-------+  ");
        }
    
        // Print the lines: Color above Value
        System.out.println(topLine);
        System.out.println(colorLine);
        System.out.println(valueLine);
        System.out.println(bottomLine);
    
        printDivider(null);
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

