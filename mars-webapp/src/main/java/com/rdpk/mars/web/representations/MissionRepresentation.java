package com.rdpk.mars.web.representations;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value @Builder @AllArgsConstructor public class MissionRepresentation {

    PlateauRepresentation plateauRepresentation;
    RoverRepresentation targetRover;

}
