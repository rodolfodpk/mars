package com.rdpk.mars.domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class Rover {

  public Coordinates location;
  public Direction direction;

  Rover(Coordinates location, Direction direction) {
    this.location = location;
    this.direction = direction;
  }

  /**
   * Turn to left
   */
  void turnToLeft() {
    switch (direction) {
      case NORTH: direction = Direction.WEST; break;
      case SOUTH: direction = Direction.EAST; break;
      case EAST: direction = Direction.NORTH; break;
      case WEST: direction = Direction.SOUTH; break;
      default: System.out.println("oops");
    }
  }

  /**
   * Turn to right
   */
  void turnToRight() {
    switch (direction) {
      case NORTH: direction = Direction.EAST; break;
      case SOUTH: direction = Direction.WEST; break;
      case EAST: direction = Direction.SOUTH; break;
      case WEST: direction = Direction.NORTH; break;
      default: System.out.println("oops");
    }
  }

  /**
   * Move forward
   */
  void moveForward(Plateau plateau) {

    Coordinates newLocation = null;

    switch (direction) {
      case NORTH: newLocation = new Coordinates(location.getX(), location.getY() + 1); break;
      case SOUTH: newLocation = new Coordinates(location.getX(), location.getY() - 1); break;
      case EAST: newLocation = new Coordinates(location.getX() + 1, location.getY()); break;
      case WEST: newLocation = new Coordinates(location.getX() - 1, location.getY()); break;
      default: System.out.println("oops");
    }

    if (plateau.isLocationBusy(newLocation)) {
      throw new IllegalStateException(String.format("new location [%s] is already occupied", newLocation));
    }

    if (plateau.isUnknownLocation(newLocation)) {
      throw new IllegalStateException(String.format("new location [%s] is invalid for this plateau", newLocation));
    }

    location = newLocation;

  }

}
