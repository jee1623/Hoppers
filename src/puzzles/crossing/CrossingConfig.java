package puzzles.crossing;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.Collection;
import java.util.LinkedList;

/**
 * This class represents an individual configuration for the crossing puzzle
 * @author Jake Edelstein
 */
public class CrossingConfig implements Configuration {

    private int[] config;
    private String boatPosition;
    private CrossingConfig end;

    // initial config constructor
    public CrossingConfig(int pups, int wolves){
        // array structure: {leftPups, leftWolves, rightPups, rightWolves}
        this.config = new int[]{pups, wolves, 0, 0};
        this.boatPosition = "left";
        this.end = new CrossingConfig(0, 0, pups, wolves, "right");
        Solver.totalConfigs++;
    }

    // copy constructor
    public CrossingConfig(int pupsLeft, int wolvesLeft, int pupsRight, int wolvesRight, String boatPosition){
        this.config = new int[]{pupsLeft, wolvesLeft, pupsRight, wolvesRight};
        this.boatPosition = boatPosition;
        Solver.totalConfigs++;
    }

    /**
     * @return the ending configuration
     */
    public CrossingConfig getEnd(){
        return this.end;
    }

    /**
     * Checks whether the current configuration is the solution
     * @return true if solution, else false
     */
    @Override
    public boolean isSolution() {
        return this.config == end.config && this.boatPosition.equals("right");
    }

    /**
     * Generates neighbor configurations by trying all possible combinations of moving
     * wolves and pups to opposite sides of the river
     * @return a list of neighbors
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        LinkedList<Configuration> neighbors = new LinkedList<>();
        if (this.boatPosition.equals("left")){
            if (this.config[0] >= 2){
                neighbors.add(new CrossingConfig(this.config[0] - 2, this.config[1],
                        this.config[2] + 2, this.config[3], "right"));
                neighbors.add(new CrossingConfig(this.config[0] - 1, this.config[1],
                        this.config[2] + 1, this.config[3], "right"));
            }
            if (this.config[0] == 1){
                neighbors.add(new CrossingConfig(0, this.config[1],
                        this.config[2] + 1, this.config[3], "right"));
            }
            if (this.config[1] >= 1){
                neighbors.add(new CrossingConfig(this.config[0], this.config[1] - 1,
                        this.config[2], this.config[3] + 1, "right"));
            }
        } else {
            if (this.config[2] >= 2){
                neighbors.add(new CrossingConfig(this.config[0] + 2, this.config[1],
                        this.config[2] - 2, this.config[3], "left"));
                neighbors.add(new CrossingConfig(this.config[0] + 1, this.config[1],
                        this.config[2] - 1, this.config[3], "left"));
            }
            if (this.config[2] == 1){
                neighbors.add(new CrossingConfig(this.config[0] + 1, this.config[1],
                        0, this.config[3], "left"));
            }
            if (this.config[3] >= 1){
                neighbors.add(new CrossingConfig(this.config[0], this.config[1] + 1,
                        this.config[2], this.config[3] - 1, "left"));
            }
        }
        return neighbors;
    }

    /**
     * Checks to see if two CrossingConfig objects are equal
     * @param other the other object
     * @return true if equal, else false
     */
    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof CrossingConfig){
            result = true;
            CrossingConfig o = (CrossingConfig) other;
            for (int i = 0; i < this.config.length; i++){
                if (this.config[i] != o.config[i]){
                    return false;
                }
            }
            if (!(this.boatPosition.equals(o.boatPosition))){
                return false;
            }
        }
        return result;
    }

    /**
     * @return the hashcode of the current config converted to a string
     */
    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    /**
     * Converts a CrossingConfig object to a string
     * @return the config represented as a string
     */
    @Override
    public String toString() {
        String s;
        if (this.boatPosition.equals("left")){
            s = "(BOAT) left=[" + this.config[0] + ", " + this.config[1] +"], right=["
            + this.config[2] + ", " + this.config[3] + "]       ";
        } else {
            s = "       left=[" + this.config[0] + ", " + this.config[1] +"], right=["
                    + this.config[2] + ", " + this.config[3] + "]  (BOAT)";
        }
        return s;
    }
}
