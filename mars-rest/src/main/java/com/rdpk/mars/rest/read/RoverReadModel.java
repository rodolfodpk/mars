package com.rdpk.mars.rest.read;

class RoverReadModel {

  public final int id;
  public final int x;
  public final int y;
  public final String direction;

  RoverReadModel(int id, int x, int y, String direction) {
    this.id = id;
    this.x = x;
    this.y = y;
    this.direction = direction;
  }
}
