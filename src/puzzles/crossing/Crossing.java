package puzzles.crossing;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.HashMap;
import java.util.List;

/**
 * This class runs simulations for crossing puzzles
 * @author Jake Edelstein
 */
public class Crossing extends Solver{
    public static void main(String[] args) {
        // must have 2 arguments
        if (args.length != 2) {
            System.out.println(("Usage: java Crossing pups wolves"));
            System.exit(0);
        } else {
            // create initial config
            CrossingConfig config = new CrossingConfig(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
            Solver crossingSolver = new Solver();
            HashMap<Configuration, Configuration> predecessors =
                    crossingSolver.findSolution(config, config.getEnd());
            List<String> path = crossingSolver.buildPath(predecessors, config, config.getEnd());
            // display statistics and other info
            System.out.println("Pups: " + args[0] +", Wolves: " + args[1]);
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
