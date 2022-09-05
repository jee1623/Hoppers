package puzzles.common.solver;
import java.util.*;

/**
 * This class implements a breadth-first search algorithm to solve several types of puzzles
 * @author Jake Edelstein
 */

public class Solver implements Configuration {

    public static int totalConfigs = 0;
    public static int uniqueConfigs = 0;
    public static Configuration endConfig;

    /**
     * This version of the findSolution method is used when an explicit end configuration is given
     * @param start the starting configuration
     * @param end the end configuration
     * @return the predecessors list
     */
   public HashMap<Configuration, Configuration> findSolution (Configuration start, Configuration end){
       // BFS find path
       LinkedList<Configuration> queue = new LinkedList<>();
       HashMap<Configuration, Configuration> predecessors = new HashMap<>();
       queue.add(start);
       predecessors.put(start, start);
       while (!queue.isEmpty()){
           Configuration current = queue.remove(0);
           if (current.equals(end)){
               return predecessors;
           }
           for (Configuration c : current.getNeighbors()){
               if (!predecessors.containsKey(c)){
                   predecessors.put(c, current);
                   queue.add(c);
                   uniqueConfigs++;
               }
           }
       }
       return null;
   }

    /**
     * This version of the findSolution method is used when there are many possible end configurations
     * @param start the starting configuration
     * @return the predecessors list
     */
   public HashMap<Configuration, Configuration> findSolution (Configuration start){
       // BFS find path
       LinkedList<Configuration> queue = new LinkedList<>();
       HashMap<Configuration, Configuration> predecessors = new HashMap<>();
       queue.add(start);
       predecessors.put(start, start);
       while (!queue.isEmpty()){
           Configuration current = queue.remove(0);
           if (current.isSolution()){
               endConfig = current;
               return predecessors;
           }
           for (Configuration c : current.getNeighbors()){
               if (!predecessors.containsKey(c)){
                   predecessors.put(c, current);
                   queue.add(c);
                   uniqueConfigs++;
               }
           }
       }
        return null;
   }

    /**
     * Use the predecessors list from findSolution() to build the shortest past from start to finish
     * @param predecessors the predecessors list
     * @param start the starting configuration
     * @param end the ending configuration
     * @return the path
     */
   public List<String> buildPath(Map<Configuration, Configuration> predecessors,
                                  Configuration start, Configuration end){
       List<String> path = null;
       if (predecessors != null){
           path = new LinkedList<>();
           Configuration currentConfig = end;
           while (currentConfig != start){
               path.add(0, currentConfig.toString());
               currentConfig = predecessors.get(currentConfig);
           }
           path.add(0, start.toString());
       }
       return path;
   }

    /**
     * Checks whether a configuration is a solution or not
     * @return true or false
     */
    @Override
    public boolean isSolution() {
       // will be overridden for specific puzzle types
        return false;
    }

    /**
     * Generates all possible neighbors for a given configuration
     * @return a list of neighbors
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        // will be overridden for specific puzzle types
        return null;
    }

    /**
     * Determines if two configurations are equal
     * @param other another object
     * @return true or false
     */
    @Override
    public boolean equals(Object other) {
        // will be overridden for specific puzzle types
        return false;
    }

    /**
     * Generates a hash code for an object
     * @return a hash code
     */
    @Override
    public int hashCode() {
        // will be overridden for specific puzzle types
        return 0;
    }

    /**
     * Converts a configuration into a readable string
     * @return a string
     */
    @Override
    public String toString() {
        // will be overridden for specific puzzle types
        return null;
    }
}
