package com.rdpk.mars.servet;

import com.rdpk.mars.Direction;
import com.rdpk.mars.Location;
import com.rdpk.mars.Plateau;
import com.rdpk.mars.Rover;
import com.rdpk.mars.exceptions.IllegalOperation;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An object to receive text commands from NASA, validate it, interpret it and finally proceed
 */
public class MarsMissionController {

    private Pattern plateauCreation = Pattern.compile("(\\d*)\\s(\\d*)");
    private Pattern roverLanding = Pattern.compile("(\\d*)\\s(\\d*)\\s([N|S|E|W])");
    private Pattern roverMoving = Pattern.compile("[M|L|R]*");

    @Getter private Plateau plateau;
    private Rover lastRover ;
    private List<String> commandsFromNASA = new ArrayList<String>();

    private final AtomicInteger roverIdCounter = new AtomicInteger();

    /**
     * Execute a command received from NASA.
     * @param command
     */
    public void executeCommand(String command) {

        Matcher matcherPlateau = plateauCreation.matcher(command);
        boolean isPlateauCreation = matcherPlateau.matches();

        Matcher matcherRoverLanding = roverLanding.matcher(command);
        boolean isRoverLanding = matcherRoverLanding.matches();

        Matcher matcherRoverMoving = roverMoving.matcher(command);
        boolean isRoverMoving = matcherRoverMoving.matches();

        if (!isPlateauCreation && plateau == null) {
            throw new IllegalOperation(
                    "The first command must be a plateau creation");
        }

        if (isPlateauCreation) {

            // System.out.println("Plateau created");
            this.plateau = new Plateau("plateau-1", new Integer(matcherPlateau.group(1)),
                    new Integer(matcherPlateau.group(2)));

        } else if (isRoverLanding) {

            // System.out.println("Rover landed");
            lastRover = new Rover(String.format("rover-%d", roverIdCounter.incrementAndGet())) ;
            lastRover.land(plateau, new Location(new Integer(matcherRoverLanding.group(1)),
                            new Integer(matcherRoverLanding.group(2))),
                    getDirection(matcherRoverLanding.group(3))) ;

        } else if (isRoverMoving) {

            // System.out.println("Reover moving");
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
     * A helper method
     * @param c
     * @return direction
     */
    private Direction getDirection(String c) {
        if (c.equals("N")) return Direction.NORTH ;
        if (c.equals("S")) return Direction.SOUTH ;
        if (c.equals("E")) return Direction.EAST ;
        if (c.equals("W")) return Direction.WEST ;
        return null;
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