package com.rdpk.mars.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.*;

@Getter
@ToString
public class Plateau {

  final String name;
  Coordinates topRight;
  Set<Rover> rovers =  new HashSet<>();
  Rover activeRover;

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
    Optional<Rover> target = rovers.stream().filter(rover -> rover.location == location).findFirst();
    if (target.isPresent()) {
      activeRover = target.get();
    } else {
      activeRover = new Rover(location, direction);
      rovers.add(activeRover);
    }
    System.out.println("after activate \n" + this);
  }

  public void move(List<MoveRoverAction> moves) {
    if (activeRover == null) {
      throw new IllegalStateException("Before moving a rover you must to activate it");
    }
    System.out.println("before move \n" + this);
    moves.forEach(move -> {
      switch (move) {
        case WALK: activeRover.moveForward(this); break;
        case TURN_LEFT: activeRover.turnToLeft(); break;
        case TURN_RIGHT: activeRover.turnToRight(); break;
        default: System.out.println("oops");
      }
      System.out.println(activeRover);
    });
    System.out.println("after activate \n" + this);
  }

  boolean isLocationBusy(Coordinates newLocation) {
    return rovers.stream().filter(rover -> rover.location == newLocation).findFirst().isPresent();
  }

  boolean isUnknownLocation(Coordinates newLocation) {
    return newLocation.isBiggerThan(topRight);
  }

}
