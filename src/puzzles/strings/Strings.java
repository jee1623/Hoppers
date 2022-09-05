package puzzles.strings;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.HashMap;
import java.util.List;

/**
 * This class runs simulations for the strings puzzle
 * @author Jake Edelstein
 */
public class Strings extends Solver{
    public static void main(String[] args) {
        // must have 2 arguments
        if (args.length != 2) {
            System.out.println(("Usage: java Strings start finish"));
            System.exit(0);
        // arguments must be same length
        } else if (args[0].length() != args[1].length()){
            System.out.println("Strings must be the same length to solve");
            System.exit(0);
        } else {
            String start = args[0];
            String end = args[1];
            // create initial config
            StringsConfig config = new StringsConfig(start, end);
            Solver stringSolver = new Solver();
            HashMap<Configuration, Configuration> predecessors =
                    stringSolver.findSolution(config, config.getEnd());
            List<String> path = stringSolver.buildPath(predecessors, config, config.getEnd());
            // display statistics and other info
            System.out.println("Start: " + config + ", End: " + config.getEnd());
            System.out.println("Total configurations: " + Solver.totalConfigs);
            System.out.println("Unique configurations: " + Solver.uniqueConfigs);
            // display path if one exists
            if (path == null){
                System.out.println("No solution");
            } else {
                for (int step = 0; step < path.size(); step++){
                    System.out.println("Step " + step + ": " + path.get(step));
                }
            }
        }
    }
}
