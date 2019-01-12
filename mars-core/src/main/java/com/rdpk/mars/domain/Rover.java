package com.rdpk.mars.domain;

import java.util.Objects;

public class Rover implements Comparable<Rover> {

  public Rover(int id, Coordinates location, Direction direction) {
    this.id = id;
    this.location = location;
    this.direction = direction;
  }

  public Integer id;
  public Coordinates location;
  public Direction direction;

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
      case NORTH: newLocation = new Coordinates(location.x, location.y + 1); break;
      case SOUTH: newLocation = new Coordinates(location.x, location.y - 1); break;
      case EAST: newLocation = new Coordinates(location.x + 1, location.y); break;
      case WEST: newLocation = new Coordinates(location.x - 1, location.y); break;
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


  @Override
  public int compareTo(Rover rover) {
    return rover.id.compareTo(this.id);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Rover rover = (Rover) o;
    return id.equals(rover.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "Rover{" +
            "id=" + id +
            ", location=" + location +
            ", direction=" + direction +
            '}';
  }

}
