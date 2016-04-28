package com.rdpk.mars.servet;

import com.rdpk.mars.Direction;
import com.rdpk.mars.Location;
import com.rdpk.mars.Plateau;
import com.rdpk.mars.Rover;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
class ProtocolCommandParser {

    private Pattern plateauCreation = Pattern.compile("(\\d*)\\s(\\d*)");
    private Pattern roverCoordinates = Pattern.compile("(\\d*)\\s(\\d*)\\s([N|S|E|W])");
    private Pattern roverMoving = Pattern.compile("[M|L|R]*");

    private final String protocolCommand ;
    private final Matcher matcherPlateau ;
    private final Matcher matcherRoverCoordinates;
    private final Matcher matcherRoverMoving ;

    ProtocolCommandParser(String protocolCommand) {
        this.protocolCommand = protocolCommand;
        this.matcherPlateau = plateauCreation.matcher(protocolCommand);
        this.matcherRoverCoordinates = roverCoordinates.matcher(protocolCommand);
        this.matcherRoverMoving = roverMoving.matcher(protocolCommand);
    }

    boolean isPlateauCreation() { return matcherPlateau.matches(); }

    boolean isRoverCoordinates() { return matcherRoverCoordinates.matches(); }

    boolean isRoverMoving () { return matcherRoverMoving.matches(); }

    Plateau plateau(String id) {
        return new Plateau(id, new Integer(matcherPlateau.group(1)),
                new Integer(matcherPlateau.group(2)));
    }

    Location location() {
        return new Location(new Integer(matcherRoverCoordinates.group(1)),
                            new Integer(matcherRoverCoordinates.group(2)));
    }

    Direction direction() {
        return getDirectionOf(matcherRoverCoordinates.group(3));
    }

    List<Runnable> roverSteps(Rover targetRover) {
        List<Runnable> stepsList = new ArrayList<>();
        // iterate our actions
        char[] actionsList = protocolCommand.toCharArray();
        for (int i = 0; i < actionsList.length; i++) {
            // System.out.println(i + " action for the rover = " + actionsList[i]) ;
            if (actionsList[i] == 'L') { // turn to left action
                stepsList.add(targetRover::turnToLeft) ;
            } else if (actionsList[i] == 'R') { // turn to right action
                stepsList.add(targetRover::turnToRight) ;
            } else if (actionsList[i] == 'M') { // move action
                stepsList.add(targetRover::moveForward) ;
            }
        }
        return stepsList;
    }

    /**
     * A helper method
     * @param c
     * @return direction
     */
    private Direction getDirectionOf(String c) {
        if (c.equals("N")) return Direction.NORTH ;
        if (c.equals("S")) return Direction.SOUTH ;
        if (c.equals("E")) return Direction.EAST ;
        if (c.equals("W")) return Direction.WEST ;
        return null;
    }

}
