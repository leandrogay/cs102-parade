class Card {
    enum Color { RED, BLUE, GREEN, YELLOW, PURPLE, BLACK }
    
    private final Color color;
    private final int value;

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

    @Override
    public String toString() {
        return color + ": " + value;
    }
}