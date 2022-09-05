package puzzles.hoppers.model;

import puzzles.common.Coordinates;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

/**
 * This class represents individual configurations for the hoppers puzzle
 * @author Jake Edelstein
 */
public class HoppersConfig implements Configuration{
    private char[][] grid;
    private int numRows;
    private int numCols;
    // names for the different characters found in hoppers files
    public static final char GREEN_FROG = 'G';
    public static final char RED_FROG = 'R';
    public static final char NO_FROG = '.';
    public static final char WATER = '*';

    /**
     * Create the initial configuration for a hoppers game
     * @param filename the file to read from
     */
    public HoppersConfig(String filename) throws  IOException{
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))){
            // create the grid given the dimensions on the first line
            String[] dims = bufferedReader.readLine().split(" ");
            this.numRows = Integer.parseInt(dims[0]);
            this.numCols = Integer.parseInt(dims[1]);
            this.grid = new char[this.numRows][this.numCols];
            // fill in the grid with the file's contents
            for (int r = 0; r < this.numRows; r++){
                String[] nextRow = bufferedReader.readLine().split(" ");
                for (int c = 0; c < this.numCols; c++){
                    this.grid[r][c] = nextRow[c].charAt(0);
                }
            }
            Solver.totalConfigs++;
            Solver.uniqueConfigs++;
        }
    }

    /**
     * Create a new configuration from an already existing one
     * @param current the current configuration
     * @param remove the coordinates of the frog to be removed due to another frog jumping over it
     * @param jumpStart the starting coordinates of a frog that is jumping
     * @param jumpEnd the ending coordinates of a frog that is jumping
     */
    public HoppersConfig(HoppersConfig current, Coordinates remove, Coordinates jumpStart, Coordinates jumpEnd){
        // copy the current config
        this.numRows = current.numRows;
        this.numCols = current.numCols;
        this.grid = new char[this.numRows][this.numCols];
        for (int r = 0; r < this.numRows; r++){
            for (int c = 0; c < this.numCols; c++){
                this.grid[r][c] = current.grid[r][c];
            }
        }
        // remove the frog that gets jumped over
        this.grid[remove.getRow()][remove.getColumn()] = NO_FROG;
        // store the color of the frog before it jumps
        char frog = this.grid[jumpStart.getRow()][jumpStart.getColumn()];
        // remove the frog that jumps from its current position
        this.grid[jumpStart.getRow()][jumpStart.getColumn()] = NO_FROG;
        // add the frog that jumps to its new position
        this.grid[jumpEnd.getRow()][jumpEnd.getColumn()] = frog;
        Solver.totalConfigs++;
    }

    /** getters for private values*/
    public int getNumRows(){
        return this.numRows;
    }

    public int getNumCols() {
        return this.numCols;
    }

    public char[][] getGrid(){
        return this.grid;
    }

    /**
     * Determine if the current config is a solution
     * @return true if it is a solution, else false
     */
    @Override
    public boolean isSolution() {
        // flags to check if green and red frogs are present
        boolean greenFrog = false;
        boolean redFrog = false;
        for (int r = 0; r < this.numRows; r++){
            for (int c = 0; c < this.numCols; c++){
                // if any green frogs are present, it is not a solution
                if (this.grid[r][c] == GREEN_FROG){
                    greenFrog = true;
                    break;
                }
                else if (this.grid[r][c] == RED_FROG){
                    redFrog = true;
                }
            }
        }
        // only returns true when the red frog is present and green frogs aren't
        return redFrog && !greenFrog;
    }

    /**
     * Generate configs for all possible moves from the current config
     * @return a list of new configs
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        LinkedList<Configuration> neighbors = new LinkedList<>();
        for (int r = 0; r < this.numRows; r++) {
            for (int c = 0; c < this.numCols; c++) {
                /*
                  Go through every space on the board and check:
                  1. if the space has a frog on it
                  2. if the frog can make a valid jump in each of the 8 directions
                  3. if the frog is jumping over a green frog
                  4. if the frog is landing on an empty space
                 */
                if (this.grid[r][c] == GREEN_FROG || this.grid[r][c] == RED_FROG) {
                    // jump north
                    if (r - 4 >= 0 && this.grid[r - 2][c] == GREEN_FROG && this.grid[r - 4][c] == NO_FROG) {
                        neighbors.add(new HoppersConfig(this, new Coordinates(r - 2, c),
                                new Coordinates(r, c), new Coordinates(r - 4, c)));
                    }
                    // jump south
                    if (r + 4 < this.numRows && this.grid[r + 2][c] == GREEN_FROG && this.grid[r + 4][c] == NO_FROG) {
                        neighbors.add(new HoppersConfig(this, new Coordinates(r + 2, c),
                                new Coordinates(r, c), new Coordinates(r + 4, c)));
                    }
                    // jump east
                    if (c + 4 < this.numCols && this.grid[r][c + 2] == GREEN_FROG && this.grid[r][c + 4] == NO_FROG) {
                        neighbors.add(new HoppersConfig(this, new Coordinates(r, c + 2),
                                new Coordinates(r, c), new Coordinates(r, c + 4)));
                    }
                    // jump west
                    if (c - 4 >= 0 && this.grid[r][c - 2] == GREEN_FROG && this.grid[r][c - 4] == NO_FROG) {
                        neighbors.add(new HoppersConfig(this, new Coordinates(r, c - 2),
                                new Coordinates(r, c), new Coordinates(r, c - 4)));
                    }
                    // jump northeast
                    if (r - 2 >= 0 && c + 2 < this.numCols && this.grid[r - 1][c + 1] == GREEN_FROG
                            && this.grid[r - 2][c + 2] == NO_FROG){
                        neighbors.add(new HoppersConfig(this, new Coordinates(r - 1, c + 1),
                                new Coordinates(r, c), new Coordinates(r - 2, c + 2)));
                    }
                    // jump southeast
                    if (r + 2 < this.numRows && c + 2 < this.numCols && this.grid[r + 1][c + 1] == GREEN_FROG
                            && this.grid[r + 2][c + 2] == NO_FROG){
                        neighbors.add(new HoppersConfig(this, new Coordinates(r + 1, c + 1),
                                new Coordinates(r, c), new Coordinates(r + 2, c + 2)));
                    }
                    // jump northwest
                    if (r - 2 >= 0 && c - 2 >= 0 && this.grid[r - 1][c - 1] == GREEN_FROG
                            && this.grid[r - 2][c - 2] == NO_FROG){
                        neighbors.add(new HoppersConfig(this, new Coordinates(r - 1, c - 1),
                                new Coordinates(r, c), new Coordinates(r - 2, c - 2)));
                    }
                    // jump southwest
                    if (r + 2 < this.numRows && c - 2 >= 0 && this.grid[r + 1][c - 1] == GREEN_FROG
                            && this.grid[r + 2][c - 2] == NO_FROG){
                        neighbors.add(new HoppersConfig(this, new Coordinates(r + 1, c - 1),
                                new Coordinates(r, c), new Coordinates(r + 2, c - 2)));
                    }
                }
            }
        }
        return neighbors;
    }
    /**
     * Determines if two configurations are equal
     * @param other another configuration
     * @return true if equal, else false
     */
    @Override
    public boolean equals(Object other){
        // initially assume false
        if (other instanceof HoppersConfig){
            // assume true if other object is a HoppersConfig
            HoppersConfig o = (HoppersConfig) other;
            // go through all cells in the grid, return false if any don't match
            for (int r = 0; r < this.numRows; r++){
                for (int c = 0; c < this.numCols; c++){
                    if (this.grid[r][c] != o.grid[r][c]){
                        return false;
                    }
                }
            }
            // if you get here, everything matched
            return true;
        }
        return false;
    }

    /**
     * Generate a hashcode for this configuration
     * @return a hashcode
     */
    @Override
    public int hashCode(){
        return this.toString().hashCode();
    }

    /**
     * Converts a HoppersConfig object to a string
     * @return the config as a string
     */
    @Override
    public String toString(){
        String gridString = "";
        for (int r = 0; r < this.numRows; r++){
            for (int c = 0; c < this.numCols; c++){
                if (c != 0){
                    gridString += " ";
                }
                gridString += this.grid[r][c];
            }
            if (r != this.numRows - 1){
                gridString += "\n";
            }
        }
        return gridString;
    }


}
