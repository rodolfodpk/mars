package com.rdpk.mars.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@ToString
public class Plateau {

  final String name;
  Coordinates topRight;
  Map<Coordinates, Rover> rovers =  new HashMap<>();
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
    rovers.computeIfAbsent(location, coordinates -> new Rover(coordinates, direction));
    activeRover = rovers.get(location);
  }

  public void move(List<MoveRoverAction> moves) {
    if (activeRover == null) {
      throw new IllegalStateException("Before moving a rover you must to activate it");
    }
    System.out.println(this);
    moves.forEach(move -> {
      switch (move) {
        case WALK: activeRover.moveForward(this); break;
        case TURN_LEFT: activeRover.turnToLeft(); break;
        case TURN_RIGHT: activeRover.turnToRight(); break;
        default: System.out.println("oops");
      }
      System.out.println(activeRover);
    });
    System.out.println(this);
    rovers.put(activeRover.location, activeRover);
  }

  boolean isLocationBusy(Coordinates newLocation) {
    return rovers.containsKey(newLocation);
  }

  boolean isUnknownLocation(Coordinates newLocation) {
    return newLocation.isBiggerThan(topRight);
  }

}
