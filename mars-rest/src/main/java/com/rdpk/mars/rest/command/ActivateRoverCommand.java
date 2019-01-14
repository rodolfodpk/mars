package com.rdpk.mars.rest.command;

import com.rdpk.mars.domain.Coordinates;
import com.rdpk.mars.domain.Direction;

public class ActivateRoverCommand {

  public final String plateauId;
  public final Coordinates location;
  public final Direction direction;

  public ActivateRoverCommand(String plateauId, Coordinates location, Direction direction) {
    this.plateauId = plateauId;
    this.location = location;
    this.direction = direction;
  }

}
