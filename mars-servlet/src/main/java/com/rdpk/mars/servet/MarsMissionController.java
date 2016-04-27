package com.rdpk.mars.servet;

import com.rdpk.mars.Plateau;
import com.rdpk.mars.Rover;
import com.rdpk.mars.exceptions.IllegalOperation;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * An object to receive text commands from NASA, validate it, interpret it and finally proceed
 */
public class MarsMissionController {

    @Getter private Plateau plateau;
    private Rover lastRover ;
    private List<String> commandsFromNASA = new ArrayList<>();

    private final AtomicInteger roverIdCounter = new AtomicInteger();

    /**
     * Execute a command received from NASA.
     * @param command
     */
    public void executeCommand(String command) {

        ProtocolCommandParser parser = new ProtocolCommandParser(command);

        if (!parser.isPlateauCreation() && plateau == null) {
            throw new IllegalOperation("The first command must be a plateau creation");
        }

        if (parser.isPlateauCreation()) {

            // System.out.println("Plateau created");
            this.plateau = parser.plateau("plateau-1");

        } else if (parser.isRoverLanding()) {

            // System.out.println("Rover landed");
            lastRover = new Rover(String.format("rover-%d", roverIdCounter.incrementAndGet())) ;
            lastRover.land(plateau, parser.location(), parser.direction());

        } else if (parser.isRoverMoving()) {

            // System.out.println("Rover moving");
            // iterate our actions
            char[] actionsList = command.toCharArray();
            for (int i = 0; i < actionsList.length; i++) {
                // System.out.println(i + " action for the rover = " + actionsList[i]) ;
                if (actionsList[i] == 'L') { // turn to left action
                    lastRover.turnToLeft();
                } else if (actionsList[i] == 'R') { // turn to right action
                    lastRover.turnToRight();
                } else if (actionsList[i] == 'M') { // move action
                    lastRover.moveForward();
                }
            }

        } else {
            throw new IllegalOperation("Unknown command");
        }

        // just to track the commands history
        commandsFromNASA.add(command);

    }

    /**
     * Return the current status of the Plateau in text format
     * @return status
     */
    public String getStatus() {
        StringBuffer status = new StringBuffer();
        for (Rover rover : plateau.getRovers()) {
            status.append(rover.getLocation() + " " + rover.getDirection().name().charAt(0)
                    + "\n");
        }
        return status.toString();
    }

}