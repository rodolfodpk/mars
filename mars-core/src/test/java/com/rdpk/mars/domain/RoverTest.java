package com.rdpk.mars.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static com.rdpk.mars.domain.Direction.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

//@TestMethodOrder(OrderAnnotation.class)
class RoverTest {

  static Map<String, Direction> directionsByName;

  static Rover rover;
  static Plateau plateau;

  @BeforeAll
  static void setUp() {
    rover = new Rover(new Coordinates(1, 1), Direction.NORTH);
    directionsByName = new HashMap<>();
    directionsByName.put("NORTH", NORTH);
    directionsByName.put("SOUTH", SOUTH);
    directionsByName.put("EAST", EAST);
    directionsByName.put("WEST", WEST);
    plateau = new Plateau("test-2");
    plateau.resize(new Coordinates(5, 5));
  }

  @DisplayName("it can turn to left")
  @ParameterizedTest(name = "{index} => {0}")
  @ValueSource(strings = {"NORTH WEST", "WEST SOUTH", "SOUTH EAST", "EAST NORTH"})
  void a1(String rule) {
    String[] terms = rule.split(" ");
     assertThat(rover.direction).isEqualTo(directionsByName.get(terms[0]));
     rover.turnToLeft();
     assertThat(rover.direction).isEqualTo(directionsByName.get(terms[1]));
  }

  @DisplayName("it can turn to right")
  @ParameterizedTest(name = "{index} => {0}")
  @ValueSource(strings = {"NORTH EAST", "EAST SOUTH", "SOUTH WEST", "WEST NORTH"})
  void a2(String rule) {
    String[] terms = rule.split(" ");
    assertThat(rover.direction).isEqualTo(directionsByName.get(terms[0]));
    rover.turnToRight();
    assertThat(rover.direction).isEqualTo(directionsByName.get(terms[1]));
  }

  @DisplayName("when its direction is NORTH it can move forward")
  @Test
  void a3() {
    rover.location = new Coordinates(1, 1);
    rover.direction = NORTH;
    rover.moveForward(plateau);
    assertThat(rover.direction).isEqualTo(NORTH);
    assertThat(rover.location).isEqualTo(new Coordinates(1, 2));
  }

  @DisplayName("when its direction is SOUTH it can move forward")
  @Test
  void a4() {
    rover.location = new Coordinates(1, 2);
    rover.direction = SOUTH;
    rover.moveForward(plateau);
    assertThat(rover.direction).isEqualTo(SOUTH);
    assertThat(rover.location).isEqualTo(new Coordinates(1, 1));
  }

  @DisplayName("when its direction is EAST it can move forward")
  @Test
  void a5() {
    rover.location = new Coordinates(1, 1);
    rover.direction = EAST;
    rover.moveForward(plateau);
    assertThat(rover.direction).isEqualTo(EAST);
    assertThat(rover.location).isEqualTo(new Coordinates(2, 1));
  }

  @DisplayName("when its direction is WEST it can move forward")
  @Test
  void a6() {
    rover.location = new Coordinates(2, 1);
    rover.direction = WEST;
    rover.moveForward(plateau);
    assertThat(rover.direction).isEqualTo(WEST);
    assertThat(rover.location).isEqualTo(new Coordinates(1, 1));
  }

  @DisplayName("when it may collide with another rover, it cannot move")
  @Test
  void a9() {
    rover.location = new Coordinates(1, 1);
    rover.direction = NORTH;
    plateau.resize(new Coordinates(5, 5));
    plateau.activate(new Coordinates(1, 2), EAST);
    assertThrows(IllegalStateException.class, () -> rover.moveForward(plateau));
    assertThat(rover.direction).isEqualTo(NORTH);
    assertThat(rover.location).isEqualTo(new Coordinates(1, 1));
  }

  @DisplayName("when it may get outside of the plateau, it cannot move")
  @Test
  void a10() {
    rover.location = new Coordinates(1, 1);
    rover.direction = NORTH;
    plateau.resize(new Coordinates(1, 1));
    assertThrows(IllegalStateException.class, () -> rover.moveForward(plateau));
    assertThat(rover.direction).isEqualTo(NORTH);
    assertThat(rover.location).isEqualTo(new Coordinates(1, 1));
  }
}