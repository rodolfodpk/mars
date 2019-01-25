package com.rdpk.mars.domain;


import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Mar's Plateau to be explored
 */
public class Plateau {

  private AtomicInteger generator = new AtomicInteger();

  public final String name;
  public Coordinates topRight;
  public SortedSet<Rover> rovers = new TreeSet<>(Comparator.comparing(Rover::getId));
  public Rover activeRover;

  public Plateau(String name) {
    this.name = name;
  }

  public void resize(Coordinates topRight) {
    this.topRight = topRight;
  }

  public void activate(Coordinates location, Direction direction) {
    if (location.isBiggerThan(topRight)) {
      throw new IllegalArgumentException(String.format("Location %s is not within %s", location, topRight));
    }
    Optional<Rover> target = roverAtLocation(location);
    if (target.isPresent()) {
      activeRover = target.get();
    } else {
      activeRover = new Rover(generator.incrementAndGet(), location, direction);
      rovers.add(activeRover);
    }
  }

  public void move(List<MoveRoverAction> moves) {
    if (activeRover == null) {
      throw new IllegalStateException("Before moving a rover you must to activate it");
    }
    moves.forEach(move -> {
      switch (move) {
        case WALK:
          activeRover.moveForward(this);
          break;
        case TURN_LEFT:
          activeRover.turnToLeft();
          break;
        case TURN_RIGHT:
          activeRover.turnToRight();
          break;
        default:
      }
    });
  }

  Optional<Rover> roverAtLocation(Coordinates newLocation) {
    return rovers.stream()
            .filter(rover -> rover.location.equals(newLocation)).findFirst();
  }

  boolean isUnknownLocation(Coordinates newLocation) {
    return newLocation.isBiggerThan(topRight);
  }

  @Override
  public String toString() {
    return "Plateau{" +
            "name='" + name + '\'' +
            ", topRight=" + topRight +
            ", rovers=" + rovers +
            ", activeRover=" + activeRover +
            '}';
  }
}
