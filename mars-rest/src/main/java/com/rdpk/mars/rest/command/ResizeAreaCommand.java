package com.rdpk.mars.rest.command;

import com.rdpk.mars.domain.Coordinates;

public class ResizeAreaCommand {

  public final String plateauId;
  public final Coordinates topRightCoordinates;

  public ResizeAreaCommand(String plateauId, Coordinates topRightCoordinates) {
    this.plateauId = plateauId;
    this.topRightCoordinates = topRightCoordinates;
  }
}
