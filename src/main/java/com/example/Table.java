import java.util.*;


public class Table{
    enum Direction {LEFT_TO_RIGHT, RIGHT_TO_LEFT};

    private ArrayList<Card> parade;
    private Direction direction;

    // table will be empty at the start of the game
    // default direction is from left to right
    public Table(){
        this.parade = new ArrayList<>(); 
        this.direction = Direction.LEFT_TO_RIGHT; 
    }

    public void addCardToParade(Card card) {
        parade.add(card);
    }

    public ArrayList<Card> getParade(){
        if (direction == Direction.LEFT_TO_RIGHT){
            return parade;


        //reverses the order of cards if direction is from right to left
        } else{                                                     
            ArrayList<Card> reversed = new ArrayList<>(parade);
            Collections.reverse(reversed);
            return reversed;
        }

    }


}
