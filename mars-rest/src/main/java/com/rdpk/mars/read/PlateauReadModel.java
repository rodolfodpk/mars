package com.rdpk.mars.read;

import java.util.Set;

public class PlateauReadModel {

  public final String id;
  public final int x;
  public final int y;
  public final Set<RoverReadModel> rovers;
  public final RoverReadModel activeRover;

  public PlateauReadModel(String id, int x, int y, Set<RoverReadModel> rovers, RoverReadModel activeRover) {
    this.id = id;
    this.x = x;
    this.y = y;
    this.rovers = rovers;
    this.activeRover = activeRover;
  }
}
