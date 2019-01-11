package com.rdpk.mars.domain;

import lombok.Value;

/**
 * A location point given by x and y coordinates.
 */
@Value
public class Coordinates {

  private final int x;
  private final int y;

  public Coordinates(int x, int y) {
    if (x < 0 || y < 0) {
      String error =  String.format("Invalid coordinate. Both x [%d] and y [%d] must zero or positive ", x, y);
      throw new IllegalArgumentException(error);
    }
    this.x = x;
    this.y = y;
  }

  @Override
  public String toString() {
    return x + " " + y;
  }

  boolean isBiggerThan(Coordinates coordinates) {
    return x > coordinates.x && y > coordinates.y;
  }
}
