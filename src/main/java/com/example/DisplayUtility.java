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
     * Prints a table of cards with proper formatting.
     *
     * @param title The title of the table.
     * @param cards The list of cards to display.
     */
    public static void printCardTable(String title, List<Card> cards) {
        printDivider(title);
        System.out.printf("%-10s | %-10s%n", "COLOR", "VALUE");
        System.out.println("----------------------------");
        for (Card card : cards) {
            System.out.printf("%-10s | %-10d%n", card.getColor(), card.getValue());
        }
        printDivider(null);
    }

    /**
     * Prints a player's hand or collection as a formatted table.
     *
     * @param title The title of the display.
     * @param cards The list of cards to display.
     */
    public static void printPlayerCards(String title, List<Card> cards) {
        if (cards.isEmpty()) {
            printDivider(title);
            System.out.println("*No cards available*");
            printDivider(null);
            return;
        }
        printCardTable(title, cards);
    }

    /**
     * Prints a player's collection summary with counts for each color.
     *
     * @param title The title of the display.
     * @param cardCollection A map containing card colors and their counts.
     */
    public static void printCollectionSummary(String title, Map<Card.Color, Integer> cardCollection) {
        printDivider(title);
        if (cardCollection.isEmpty()) {
            System.out.println("*No collected cards*");
        } else {
            System.out.printf("%-10s | %-10s%n", "COLOR", "COUNT");
            System.out.println("----------------------------");
            for (Map.Entry<Card.Color, Integer> entry : cardCollection.entrySet()) {
                System.out.printf("%-10s | %-10d%n", entry.getKey(), entry.getValue());
            }
        }
        printDivider(null);
    }
}
