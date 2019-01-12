package com.rdpk.mars.domain;


import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Plateau {

  public AtomicInteger generator = new AtomicInteger();

  public final String name;
  public Coordinates topRight;
  public SortedSet<Rover> rovers = new TreeSet<>(Comparator.comparing(Rover::toString));
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
      activeRover = new Rover(generator.incrementAndGet(), location, direction);
      rovers.add(activeRover);
    }
    System.out.println("after activate \n" + this);
  }

  public void move(List<MoveRoverAction> moves) {
    if (activeRover == null) {
      throw new IllegalStateException("Before moving a rover you must to activate it");
    }
    System.out.println("before move \n" + this);
    rovers.remove(activeRover);
    moves.forEach(move -> {
      switch (move) {
        case WALK: activeRover.moveForward(this); break;
        case TURN_LEFT: activeRover.turnToLeft(); break;
        case TURN_RIGHT: activeRover.turnToRight(); break;
        default: System.out.println("oops");
      }
      System.out.println(activeRover);
    });
    rovers.add(activeRover);
    System.out.println("after activate \n" + this);
  }

  boolean isLocationBusy(Coordinates newLocation) {
    return rovers.stream().anyMatch(rover -> rover.location.equals(newLocation));
  }

  boolean isUnknownLocation(Coordinates newLocation) {
    return newLocation.isBiggerThan(topRight);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Plateau plateau = (Plateau) o;
    return name.equals(plateau.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
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
