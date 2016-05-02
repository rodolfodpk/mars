package com.rdpk.mars.web.representations;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.util.Set;

@Getter @Value @Builder @AllArgsConstructor
public class PlateauRepresentation {

    String plateauId;
    int xSize;
    int ySize;
    Set<RoverRepresentation> rovers;

    public int getxSize() {
        return xSize;
    }

    public int getySize() {
        return ySize;
    }

}
