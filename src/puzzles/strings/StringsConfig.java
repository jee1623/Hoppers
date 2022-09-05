package puzzles.strings;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.Collection;
import java.util.LinkedList;

/**
 * This class represents an individual configuration for the strings puzzle
 * @author Jake Edelstein
 */
public class StringsConfig implements Configuration {

    private char[] charArray;
    private StringsConfig end;

    // initial config constructor
    public StringsConfig(String start, String end){
        this.end = new StringsConfig(end);
        this.charArray = start.toCharArray();
        Solver.totalConfigs++;
    }

    // copy constructor
    public StringsConfig(String s) {
        this.charArray = s.toCharArray();
        Solver.totalConfigs++;
    }

    /**
     * @return the ending configuration
     */
    public StringsConfig getEnd(){
        return this.end;
    }

    /**
     * Checks whether the current configuration is the solution
     * @return true if solution, else false
     */
    @Override
    public boolean isSolution() {
        return this.toString().equals(this.end.toString());
    }

    /**
     * Increments a character to the next letter in the alphabet. If the
     * current character is Z, loop back to A
     * @param c the current character
     * @return the next character
     */
    public char charUp(char c){
        char newChar;
        // loop back around
        if (c == 'Z'){
            newChar = 'A';
        } else {
            newChar = (char) ((int) c + 1);
        }
        return newChar;
    }

    /**
     * Decrements a character to the previous letter in the alphabet. If the
     * current character is A, loop back to Z
     * @param c the current character
     * @return the previous character
     */
    public char charDown(char c){
        char newChar;
        // loop back around
        if (c == 'A'){
            newChar = 'Z';
        } else {
            newChar = (char) ((int) c - 1);
        }
        return newChar;
    }

    /**
     * Generates neighbor configurations by going through each character in the current
     * config and changing it to the next letter as well as the previous letter
     * @return a list of neighbors
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        LinkedList<Configuration> neighbors = new LinkedList<>();
        for (int i = 0; i < this.charArray.length; i++){
            // increment one character
            StringsConfig neighborUp = new StringsConfig(this.toString());
            neighborUp.charArray[i] = charUp(this.charArray[i]);
            neighbors.add(neighborUp);
            // decrement one character
            StringsConfig neighborDown = new StringsConfig(this.toString());
            neighborDown.charArray[i] = charDown(this.charArray[i]);
            neighbors.add(neighborDown);
        }
        return neighbors;
    }

    /**
     * Checks to see if two StringsConfig objects are equal
     * @param other the other object
     * @return true if equal, else false
     */
    @Override
    public boolean equals(Object other) {
        // initially assume objects are unequal
        boolean result = false;
        if (other instanceof StringsConfig){
            // assume equality if other is a StringsConfig
            result = true;
            StringsConfig o = (StringsConfig) other;
            for (int i = 0; i < this.charArray.length; i++){
                // loop through all letters, terminate loop if any don't match and return false
                if (this.charArray[i] != o.charArray[i]) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * @return the hashcode of the word in the current config
     */
    public int hashCode() {
        return this.toString().hashCode();
    }

    /**
     * Converts a StringsConfig object to a string
     * @return the word as a string
     */
    @Override
    public String toString() {
        String word = "";
        for (char letter : this.charArray){
            word += letter;
        }
        return word;
    }
}