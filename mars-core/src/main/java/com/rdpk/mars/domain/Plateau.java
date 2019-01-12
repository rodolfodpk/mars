package com.rdpk.mars.domain;


import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Plateau {

  public final String name;
  public Coordinates topRight;
  public Set<Rover> rovers = new HashSet<>();
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
    Optional<Rover> target = rovers.stream().filter(rover -> rover.location.equals(location)).findFirst();
    if (target.isPresent()) {
      activeRover = target.get();
    } else {
      activeRover = new Rover(location, direction);
      rovers.add(activeRover);
    }
//    System.out.println("after activate \n" + this);
  }

  public void move(List<MoveRoverAction> moves) {
    if (activeRover == null) {
      throw new IllegalStateException("Before moving a rover you must to activate it");
    }
//    System.out.println("before move \n" + this);
    rovers.remove(activeRover);
    moves.forEach(move -> {
      switch (move) {
        case WALK: activeRover.moveForward(this); break;
        case TURN_LEFT: activeRover.turnToLeft(); break;
        case TURN_RIGHT: activeRover.turnToRight(); break;
        default: System.out.println("oops");
      }
//      System.out.println(activeRover);
    });
    rovers.add(activeRover);
//    System.out.println("after activate \n" + this);
  }

  boolean isLocationBusy(Coordinates newLocation) {
    return rovers.stream().anyMatch(rover -> rover.location.equals(newLocation));
  }

  boolean isUnknownLocation(Coordinates newLocation) {
    return newLocation.isBiggerThan(topRight);
  }

  @Override
  public String toString() {
    return "Plateau{" +
            "name='" + name + '\'' +
            ", topRight=" + topRight +
            '}';
  }
}
