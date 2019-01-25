package com.rdpk.mars.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PlateauTest {

  Plateau plateau;

  @BeforeEach
  void setUp() {
    plateau = new Plateau("test-1");
    plateau.resize(new Coordinates(5, 5));
  }

  @Test
  @DisplayName("it can check if a coordinate is within this plateau")
  void a5() {
    assertThat(plateau.isUnknownLocation(new Coordinates(11, 11))).isTrue();
  }

  @Test
  @DisplayName("when it's created, it is empty")
  void a0() {
    assertThat(plateau.rovers.size()).isEqualTo(0);
    assertThat(plateau.activeRover).isNull();
  }

  @Test
  @DisplayName("when the rover's location is not within this plateau, activate must fail")
  void a1() {
    assertThrows(IllegalArgumentException.class, () -> plateau.activate(new Coordinates(6, 6), Direction.NORTH));
  }

  @Test
  @DisplayName("when a rover location is valid, activate succeeds")
  void a2() {
    Coordinates target = new Coordinates(1, 1);
    plateau.activate(target, Direction.NORTH);
    Rover rover = new Rover(1, target, Direction.NORTH);
    assertThat(plateau.activeRover.location).isEqualTo(target);
    assertThat(plateau.rovers.size()).isEqualTo(1);
    assertThat(plateau.rovers.contains(rover)).isTrue();
    assertThat(plateau.roverAtLocation(target).get()).isEqualTo(rover);
  }

  @Test
  @DisplayName("when trying to move before activating a rover, it must fail")
  void a3() {
    assertThrows(IllegalStateException.class, () -> plateau.move(new ArrayList<>()));
  }

  @Test
  @DisplayName("when moving with an activated rover, it works")
  void a4() {
    Coordinates target = new Coordinates(1, 1);
    plateau.activate(target, Direction.NORTH);
    plateau.move(singletonList(MoveRoverAction.WALK));
    Coordinates afterStep = new Coordinates(1, 2);
    assertThat(plateau.activeRover.location).isEqualTo(afterStep);
    assertThat(plateau.roverAtLocation(afterStep).get()).isEqualTo(plateau.activeRover);
  }

  @Test
  @DisplayName("when activating a previously landed rover, it will be activated")
  void a6() {
    plateau.activate(new Coordinates(1, 1), Direction.NORTH);
    Rover rover1 = plateau.activeRover;
    plateau.activate(new Coordinates(2, 2), Direction.NORTH);
    plateau.activate(new Coordinates(1, 1), Direction.NORTH);
    assertThat(plateau.activeRover).isEqualTo(rover1);
  }


}