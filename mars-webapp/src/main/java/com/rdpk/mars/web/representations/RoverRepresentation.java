package com.rdpk.mars.web.representations;

import com.rdpk.mars.Direction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.util.List;

@Getter
@Value @Builder @AllArgsConstructor
public class RoverRepresentation {

    String roverId;
    boolean isLanded ;
    Direction direction ;
    String plateauId ;
    LocationRepresentation location ;

    public boolean getIsLanded() {
        return isLanded;
    }

}
