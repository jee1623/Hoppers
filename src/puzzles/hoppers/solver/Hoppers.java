package puzzles.hoppers.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.hoppers.model.HoppersConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Hoppers {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java Hoppers filename");
        }
        // create initial config
        HoppersConfig config = new HoppersConfig(args[0]);
        Solver hoppersSolver = new Solver();
        HashMap<Configuration, Configuration> predecessors =
                hoppersSolver.findSolution(config);
        List<String> path = hoppersSolver.buildPath(predecessors, config, Solver.endConfig);
        // print filename, starting config, and statistics
        System.out.println("File: " + args[0]);
        System.out.println(config);
        System.out.println("Total configurations: " + Solver.totalConfigs);
        System.out.println("Unique configurations: " + Solver.uniqueConfigs);
        // display path if one exists
        if (path == null){
            System.out.println("No solution");
        } else {
            for (int step = 0; step < path.size(); step++){
                System.out.println("\nStep " + step + ": \n" + path.get(step));
            }
        }
    }
}
