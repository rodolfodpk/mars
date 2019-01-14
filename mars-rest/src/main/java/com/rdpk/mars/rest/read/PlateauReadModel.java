package com.rdpk.mars.rest.read;

import java.util.List;

public class PlateauReadModel {

  public final String id;
  public final int x;
  public final int y;
  public final List<RoverReadModel> rovers;
  public final RoverReadModel activeRover;

  public PlateauReadModel(String id, int x, int y, List<RoverReadModel> rovers, RoverReadModel activeRover) {
    this.id = id;
    this.x = x;
    this.y = y;
    this.rovers = rovers;
    this.activeRover = activeRover;
  }

}
