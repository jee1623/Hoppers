package puzzles.hoppers.ptui;

import puzzles.common.Coordinates;
import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersModel;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class is used to run simulations of the hoppers puzzle in a plain text user interface
 * @author Jake Edelstein
 */
public class HoppersPTUI implements Observer<HoppersModel, String> {

    private static HoppersModel model;

    /**
     * Update the model with the latest changes
     * @param model the model
     * @param msg the message to output
     */
    @Override
    public void update(HoppersModel model, String msg) {
        System.out.println(msg);
    }

    /**
     * Initialize the simulation
     * @param filename the file to load
     */
    public void init(String filename){
        model = new HoppersModel();
        model.addObserver(this);
        model.load(filename);
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        }
        // initial load output
        HoppersPTUI ptui = new HoppersPTUI();
        ptui.init(args[0]);
        ptui.run();
    }

    /**
     * The command interpreter will run until the user quits
     * @throws IOException if there is an error reading the file
     */
    public void run() throws IOException {
        System.out.println(model.ptuiToString());
        System.out.println(HoppersModel.commands);
        Scanner in = new Scanner(System.in);
        // user inputs
        while (in.hasNextLine()){
            String[] nextCommand = in.nextLine().split(" ");
            // quit command
            if (nextCommand[0].equals("q") || nextCommand[0].equals("quit")){
                model.quit();
            }
            // hint command
            else if (nextCommand[0].equals("h") || nextCommand[0].equals("hint")){
                model.hint();
                System.out.println(model.ptuiToString());
            }
            // reset command
            else if (nextCommand[0].equals("r") || nextCommand[0].equals("reset")){
                model.reset();
                System.out.println(model.ptuiToString());
            }
            // load command
            else if (nextCommand.length == 2 && (nextCommand[0].equals("l") || nextCommand[0].equals("load"))){
                model.load(nextCommand[1]);
                System.out.println(model.ptuiToString());
            }
            // select command
            else if (nextCommand.length == 3 && (nextCommand[0].equals("s") || nextCommand[0].equals("select"))){
                model.select(new Coordinates(Integer.parseInt(nextCommand[1]), Integer.parseInt(nextCommand[2])));
                System.out.println(model.ptuiToString());
            }
            // response to invalid input
            else {
                update(model, "> Invalid command");
                System.out.println(HoppersModel.commands);
                System.out.println(model.ptuiToString());
            }
        }
    }
}
