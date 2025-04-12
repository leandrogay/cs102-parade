package card;

public class Card {
    public enum Color {
        RED, BLUE, GREEN, YELLOW, PURPLE, BLACK
    }

    private final Color color;
    private final int value;
    private boolean isFlipped = false;

    public Card(Color color, int value) {
        this.color = color;
        this.value = value;
    }

    public Color getColor() {
        return color;
    }

    public int getValue() {
        return value;
    }

    public boolean getIsFlipped() {
        return isFlipped;
    }

    public void setIsFlipped(boolean isFlipped) {
        this.isFlipped = isFlipped;
    }

    @Override
    public String toString() {
        return color + " " + value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || this.getClass() != obj.getClass())
            return false;
        Card card = (Card) obj;
        return value == card.value && color == card.color;
    }
}