package com.rdpk.mars.rest;

import com.rdpk.mars.domain.Direction;
import com.rdpk.mars.domain.MoveRoverAction;
import com.rdpk.mars.rest.read.PlateauReadModel;

import java.util.stream.Collectors;

import static com.rdpk.mars.domain.Direction.*;
import static com.rdpk.mars.domain.Direction.WEST;
import static com.rdpk.mars.domain.MoveRoverAction.*;

class ModelTranslator {

  String convert(PlateauReadModel plateauReadModel) {
    return plateauReadModel.rovers.stream()
            .map(r -> String.format("%s %s %s", r.x, r.y, r.direction))
            .collect(Collectors.joining("\n"));
  }

  Direction convert(String character) {
    switch (character.toUpperCase()) {
      case "N":
        return NORTH;
      case "S":
        return SOUTH;
      case "E":
        return EAST;
      case "W":
        return WEST;
      default:
        return null;
    }
  }

  MoveRoverAction convert(Character character) {
    switch (character) {
      case 'L':
        return TURN_LEFT;
      case 'R':
        return TURN_RIGHT;
      case 'M':
        return WALK;
      default:
        return null;
    }
  }

}
