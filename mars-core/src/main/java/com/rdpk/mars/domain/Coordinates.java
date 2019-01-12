package com.rdpk.mars.domain;

import java.util.Objects;

/**
 * A location point given by x and y coordinates.
 */
public class Coordinates {

  public final int x;
  public final int y;

  public Coordinates(int x, int y) {
    if (x < 0 || y < 0) {
      String error =  String.format("Invalid coordinate. Both x [%d] and y [%d] must zero or positive ", x, y);
      throw new IllegalArgumentException(error);
    }
    this.x = x;
    this.y = y;
  }

  boolean isBiggerThan(Coordinates coordinates) {
    return x > coordinates.x && y > coordinates.y;
  }

  @Override
  public String toString() {
    return x + " " + y;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Coordinates that = (Coordinates) o;
    return x == that.x &&
            y == that.y;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }
}
