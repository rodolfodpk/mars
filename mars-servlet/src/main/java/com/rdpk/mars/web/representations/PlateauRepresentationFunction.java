package com.rdpk.mars.web.representations;

import com.rdpk.mars.Plateau;
import com.rdpk.mars.web.representations.PlateauRepresentation;

import java.util.function.Function;

/**
 */
public class PlateauRepresentationFunction implements Function<Plateau, PlateauRepresentation> {
    @Override
    public PlateauRepresentation apply(Plateau plateau) {
        PlateauRepresentation result =  PlateauRepresentation.builder()
                    .plateauId(plateau.getId())
                    .xSize(plateau.getXSize())
                    .ySize(plateau.getYSize())
                    .rovers(plateau.getRovers().stream().map(rover -> rover.getId()))

        return null;
    }
}
