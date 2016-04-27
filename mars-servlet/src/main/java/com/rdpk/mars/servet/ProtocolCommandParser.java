package com.rdpk.mars.servet;

import com.rdpk.mars.Direction;
import com.rdpk.mars.Location;
import com.rdpk.mars.Plateau;
import lombok.Getter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class ProtocolCommandParser {

    private Pattern plateauCreation = Pattern.compile("(\\d*)\\s(\\d*)");
    private Pattern roverLanding = Pattern.compile("(\\d*)\\s(\\d*)\\s([N|S|E|W])");
    private Pattern roverMoving = Pattern.compile("[M|L|R]*");

    private final String protocolCommand ;
    private final Matcher matcherPlateau ;
    private final Matcher matcherRoverLanding ;
    private final Matcher matcherRoverMoving ;

    public ProtocolCommandParser(String protocolCommand) {
        this.protocolCommand = protocolCommand;
        this.matcherPlateau = plateauCreation.matcher(protocolCommand);
        this.matcherRoverLanding = roverLanding.matcher(protocolCommand);
        this.matcherRoverMoving = roverMoving.matcher(protocolCommand);
    }

    boolean isPlateauCreation() { return matcherPlateau.matches(); }

    boolean isRoverLanding() { return matcherRoverLanding.matches(); }

    boolean isRoverMoving () { return matcherRoverMoving.matches(); }

    Plateau plateau(String id) {
        return new Plateau(id, new Integer(matcherPlateau.group(1)),
                new Integer(matcherPlateau.group(2)));
    }

    Location location() {
        return new Location(new Integer(matcherRoverLanding.group(1)),
                            new Integer(matcherRoverLanding.group(2)));
    }

    Direction direction() {
        return getDirectionOf(matcherRoverLanding.group(3));
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
