package com.rdpk.mars;

import javaslang.Tuple;
import javaslang.Tuple2;
import lombok.Getter;
import lombok.val;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A parser for protocol commands
 */
public class ProtocolCommandParser {

    static private Pattern plateauCreation = Pattern.compile("(\\d*)\\s(\\d*)");
    static private Pattern roverCoordinates = Pattern.compile("(\\d*)\\s(\\d*)\\s([N|S|E|W])");
    static private Pattern roverMoving = Pattern.compile("[M|L|R]*");

    @Getter
    private final String protocolCommand ;
    private final Matcher matcherPlateau ;
    private final Matcher matcherRoverCoordinates;
    private final Matcher matcherRoverMoving ;

    public ProtocolCommandParser(String protocolCommand) {
        this.protocolCommand = protocolCommand;
        this.matcherPlateau = plateauCreation.matcher(protocolCommand);
        this.matcherRoverCoordinates = roverCoordinates.matcher(protocolCommand);
        this.matcherRoverMoving = roverMoving.matcher(protocolCommand);
    }

    public boolean isPlateauCreation() { return matcherPlateau.matches(); }

    public boolean isRoverCoordinates() { return matcherRoverCoordinates.matches(); }

    public boolean isRoverMoving () { return matcherRoverMoving.matches(); }

    public Tuple2<Integer, Integer> plateauAttributes() {
        return Tuple.of(new Integer(matcherPlateau.group(1)), new Integer(matcherPlateau.group(2)));
    }

    public Tuple2<Location, Direction> roverAttributes() {
        val location = new Location(new Integer(matcherRoverCoordinates.group(1)),
                                    new Integer(matcherRoverCoordinates.group(2)));
        val direction = getDirectionOf(matcherRoverCoordinates.group(3));
        return Tuple.of(location, direction);
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
