package puzzles.common;

/**
 * The coordinates class will be used to track positions of objects in the
 * puzzles that involve a grid
 * @author Jake Edelstein
 */
public class Coordinates {
    private int row;
    private int column;

    /**
     * Constructor for a pair of coordinates
     * @param r the row
     * @param c the column
     */
    public Coordinates(int r, int c){
        this.row = r;
        this.column = c;
    }

    /**
     * @return the row
     */
    public int getRow() {
        return this.row;
    }

    /**
     * @return the column
     */
    public int getColumn() {
        return this.column;
    }

    /**
     * @return the string representation of a coordinate pair
     */
    @Override
    public String toString(){
        return "(" + this.row + ", " + this.column + ")";
    }
}
