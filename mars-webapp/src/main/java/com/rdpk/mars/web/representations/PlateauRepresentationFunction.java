package com.rdpk.mars.web.representations;

import com.rdpk.mars.Plateau;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 */
public class PlateauRepresentationFunction implements Function<Plateau, PlateauRepresentation> {
    @Override
    public PlateauRepresentation apply(Plateau plateau) {
        return PlateauRepresentation.builder()
                    .plateauId(plateau.getId())
                    .xSize(plateau.getXSize())
                    .ySize(plateau.getYSize())
                    .rovers(plateau.getRovers()
                            .stream()
                            .map(rover -> new RoverRepresentationFunction().apply(rover))
                            .collect(Collectors.toSet()))
                    .build();
    }
}
