package puzzles.hoppers.model;

import puzzles.common.Coordinates;
import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is used to store all the rules and logic for the hoppers puzzle
 * @author Jake Edelstein
 */
public class HoppersModel {
    /** the collection of observers of this model */
    private final List<Observer<HoppersModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private HoppersConfig currentConfig;

    /** the current filename */
    private String filename;

    /** coordinates for jumping */
    private Coordinates startCoords = null;
    private Coordinates endCoords = null;

    /** the list of commands */
    public static String commands = """
                h(int)              -- hint next move
                l(oad) filename     -- load new puzzle file
                s(elect) r c        -- select cell at r, c
                q(uit)              -- quit the game
                r(eset)             -- reset the current game
                """;


    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<HoppersModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(String msg) {
        for (var observer : observers) {
            observer.update(this, msg);
        }
    }

    /**
     * Load a puzzle from a file
     * precondition: the file is a valid puzzle file
     * @param filename the file to load
     */
    public void load(String filename){
        try {
            this.currentConfig = new HoppersConfig(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.filename = filename;
        alertObservers("Loaded: " + filename.substring(filename.lastIndexOf(File.separator) + 1));
    }

    /**
     * Select a pair of coordinates on the board. If the selected pair is the
     * first of two, store it. If it is the second, store it and then attempt
     * to jump from the first set of coordinates to the second set of coordinates
     * @param c the coordinates
     */
    public void select(Coordinates c){
        // do this if the selection is the first pair of coordinates
        if (this.startCoords == null){
            if (c.getRow() < currentConfig.getNumRows() && c.getColumn() < currentConfig.getNumCols()){
                if (currentConfig.getGrid()[c.getRow()][c.getColumn()] != HoppersConfig.NO_FROG &&
                        currentConfig.getGrid()[c.getRow()][c.getColumn()] != HoppersConfig.WATER){
                    this.startCoords = c;
                    alertObservers("> Selected " + c);
                } else if (currentConfig.getGrid()[c.getRow()][c.getColumn()] == HoppersConfig.NO_FROG) {
                    alertObservers("> No frog at " + c);
                } else {
                    alertObservers("> Invalid selection");
                }
            } else {
                alertObservers("> Selected coordinates out of bounds");
            }
        // do this if the selection is the second pair of coordinates
        } else {
            if (c.getRow() < currentConfig.getNumRows() && c.getColumn() < currentConfig.getNumCols()){
                this.endCoords = c;
                jump(this.startCoords, this.endCoords);
                this.startCoords = null;
                this.endCoords = null;
            } else {
                alertObservers("> Selected coordinates out of bounds");
            }
        }
    }

    /**
     * First check if you can legally jump the distance from start to end, and for each of those
     * checks, make sure the user jumped over a green frog. If both checks pass, adjust the
     * config as needed
     * @param start starting coordinates
     * @param end ending coordinates
     */
    public void jump(Coordinates start, Coordinates end){
        boolean validJump = false;
        boolean greenFrogUnderJump = false;

        //jump north
        if (start.getRow() - end.getRow() == 4 && start.getColumn() == end.getColumn()){
            validJump = true;
            if (currentConfig.getGrid()[start.getRow() - 2][start.getColumn()] == HoppersConfig.GREEN_FROG &&
                    currentConfig.getGrid()[start.getRow() - 4][start.getColumn()] == HoppersConfig.NO_FROG){
                greenFrogUnderJump = true;
                currentConfig.getGrid()[start.getRow() - 2][start.getColumn()] = HoppersConfig.NO_FROG;
            }
        }
        //jump south
        else if (start.getRow() - end.getRow() == -4 && start.getColumn() == end.getColumn()){
            validJump = true;
            if (currentConfig.getGrid()[start.getRow() + 2][start.getColumn()] == HoppersConfig.GREEN_FROG &&
                    currentConfig.getGrid()[start.getRow() + 4][start.getColumn()] == HoppersConfig.NO_FROG){
                greenFrogUnderJump = true;
                currentConfig.getGrid()[start.getRow() + 2][start.getColumn()] = HoppersConfig.NO_FROG;
            }
        }
        //jump east
        else if (start.getRow() == end.getRow() && start.getColumn() - end.getColumn() == -4){
            validJump = true;
            if (currentConfig.getGrid()[start.getRow()][start.getColumn() + 2] == HoppersConfig.GREEN_FROG &&
                    currentConfig.getGrid()[start.getRow()][start.getColumn() + 4] == HoppersConfig.NO_FROG){
                greenFrogUnderJump = true;
                currentConfig.getGrid()[start.getRow()][start.getColumn() + 2] = HoppersConfig.NO_FROG;
            }
        }
        //jump west
        else if (start.getRow() == end.getRow() && start.getColumn() - end.getColumn() == 4){
            validJump = true;
            if (currentConfig.getGrid()[start.getRow()][start.getColumn() - 2] == HoppersConfig.GREEN_FROG &&
                    currentConfig.getGrid()[start.getRow()][start.getColumn() - 4] == HoppersConfig.NO_FROG){
                greenFrogUnderJump = true;
                currentConfig.getGrid()[start.getRow()][start.getColumn() - 2] = HoppersConfig.NO_FROG;
            }
        }
        // jump northeast
        else if (start.getRow() - end.getRow() == 2 && start.getColumn() - end.getColumn() == -2){
            validJump = true;
            if (currentConfig.getGrid()[start.getRow() - 1][start.getColumn() + 1] == HoppersConfig.GREEN_FROG &&
                    currentConfig.getGrid()[start.getRow() - 2][start.getColumn() + 2] == HoppersConfig.NO_FROG){
                greenFrogUnderJump = true;
                currentConfig.getGrid()[start.getRow() - 1][start.getColumn() + 1] = HoppersConfig.NO_FROG;
            }
        }
        // jump southeast
        else if (start.getRow() - end.getRow() == -2 && start.getColumn() - end.getColumn() == -2){
            validJump = true;
            if (currentConfig.getGrid()[start.getRow() + 1][start.getColumn() + 1] == HoppersConfig.GREEN_FROG &&
                    currentConfig.getGrid()[start.getRow() + 2][start.getColumn() + 2] == HoppersConfig.NO_FROG){
                greenFrogUnderJump = true;
                currentConfig.getGrid()[start.getRow() + 1][start.getColumn() + 1] = HoppersConfig.NO_FROG;
            }
        }
        // jump southwest
        else if (start.getRow() - end.getRow() == -2 && start.getColumn() - end.getColumn() == 2){
            validJump = true;
            if (currentConfig.getGrid()[start.getRow() + 1][start.getColumn() - 1] == HoppersConfig.GREEN_FROG &&
                    currentConfig.getGrid()[start.getRow() + 2][start.getColumn() - 2] == HoppersConfig.NO_FROG){
                greenFrogUnderJump = true;
                currentConfig.getGrid()[start.getRow() + 1][start.getColumn() - 1] = HoppersConfig.NO_FROG;
            }
        }
        // jump northwest
        else if (start.getRow() - end.getRow() == 2 && start.getColumn() - end.getColumn() == 2){
            validJump = true;
            if (currentConfig.getGrid()[start.getRow() - 1][start.getColumn() - 1] == HoppersConfig.GREEN_FROG &&
                    currentConfig.getGrid()[start.getRow() - 2][start.getColumn() - 2] == HoppersConfig.NO_FROG){
                greenFrogUnderJump = true;
                currentConfig.getGrid()[start.getRow() - 1][start.getColumn() - 1] = HoppersConfig.NO_FROG;
            }
        }
        // run both checks
        if (validJump && greenFrogUnderJump){
            // make sure to ove the correct color frog from start to finish
            if (currentConfig.getGrid()[start.getRow()][start.getColumn()] == HoppersConfig.GREEN_FROG){
                currentConfig.getGrid()[end.getRow()][end.getColumn()] = HoppersConfig.GREEN_FROG;
            } else {
                currentConfig.getGrid()[end.getRow()][end.getColumn()] = HoppersConfig.RED_FROG;
            }
            currentConfig.getGrid()[start.getRow()][start.getColumn()] = HoppersConfig.NO_FROG;
            alertObservers("Jumped from " + start + " to " + end);

        } else {
            alertObservers("Can't jump from " + start + " to " + end);
        }
    }

    /**
     * Generate the next step to solving the puzzle, if possible.
     * Otherwise, tell the user there is no solution.
     */
    public void hint(){
        Solver hoppersSolver = new Solver();
        HashMap<Configuration, Configuration> predecessors = hoppersSolver.findSolution(currentConfig);
        List<String> path = hoppersSolver.buildPath(predecessors, currentConfig, Solver.endConfig);
        if (path == null){
            alertObservers("> No solution!");
        }
        else if (path.size() == 1){
            alertObservers("Already solved!");
        } else {
            /* convert the second string in the path (the step after the current config)
             back to a HoppersConfig
             */
            int r = 0;
            int c = 0;
            String[] nextStep = path.get(1).split(" ");
            for (String s : nextStep){
                if (s.contains("\n")){
                    currentConfig.getGrid()[r][c] = s.charAt(0);
                    c = 0;
                    r++;
                    currentConfig.getGrid()[r][c] = s.charAt(2);
                    c++;
                } else {
                    currentConfig.getGrid()[r][c] = s.charAt(0);
                    c++;
                }

            }

            alertObservers("> Next step!");
        }
    }

    /**
     * Quit the program
     */
    public void quit(){
        System.exit(0);
    }

    /**
     * Reset the current puzzle to its initial state
     */
    public void reset() {
        load(this.filename);
        alertObservers("Puzzle reset!");
    }

    /**
     * Prints the current configuration in the designated PTUI format
     * @return the configuration as a string
     */
    public String ptuiToString(){
        String s = "   ";
        // column numbers across the top
        for (int c = 0; c < currentConfig.getNumCols(); c++){
            s += c;
            if (c <= currentConfig.getNumCols()){
                s += " ";
            }
        }
        // hyphens under column numbers
        s += "\n  ";
        for (int c = 0; c < currentConfig.getNumCols(); c++){
            s += "--";
        }
        // row numbers and row contents
        s += "\n";
        for (int r = 0; r < currentConfig.getNumRows(); r++){
            s += r + "|";
            for (int c = 0; c < currentConfig.getNumCols(); c++){
                s += " " + currentConfig.getGrid()[r][c];
            }
            s += "\n";
        }

        return s;
    }

    /**
     * @return the current configuration
     */
    public HoppersConfig getCurrentConfig() {
        return currentConfig;
    }
}
