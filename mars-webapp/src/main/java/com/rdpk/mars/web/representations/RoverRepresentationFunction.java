package com.rdpk.mars.web.representations;

import com.rdpk.mars.Rover;

import java.util.function.Function;

/**
 */
public class RoverRepresentationFunction implements Function<Rover, RoverRepresentation> {

    @Override
    public RoverRepresentation apply(Rover rover) {
        return RoverRepresentation.builder()
                .roverId(rover.getId())
                .isLanded(rover.isLanded())
                .location(rover.getLocation())
                .direction(rover.getDirection())
                .pastLocations(rover.getPastLocations())
                .build();
    }
}
