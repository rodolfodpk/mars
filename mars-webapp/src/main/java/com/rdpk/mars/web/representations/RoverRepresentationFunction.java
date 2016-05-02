package com.rdpk.mars.web.representations;

import com.rdpk.mars.Rover;

import java.util.function.Function;
import java.util.stream.Collectors;

public class RoverRepresentationFunction implements Function<Rover, RoverRepresentation> {

    @Override
    public RoverRepresentation apply(Rover rover) {

        LocationRepresentationFunction f = new LocationRepresentationFunction();

        return RoverRepresentation.builder()
                .roverId(rover.getId())
                .isLanded(rover.isLanded())
                .location(f.apply(rover.getLocation()))
                .direction(rover.getDirection())
                .build();
    }
}
