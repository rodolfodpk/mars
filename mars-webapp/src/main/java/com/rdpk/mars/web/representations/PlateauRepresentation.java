package com.rdpk.mars.web.representations;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.Set;

/**
 */
@Value @Builder @AllArgsConstructor
public class PlateauRepresentation {

    String plateauId;
    int xSize;
    int ySize;
    Set<RoverRepresentation> rovers;

}
