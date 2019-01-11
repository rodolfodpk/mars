package com.rdpk.mars.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.rdpk.mars.domain.Direction.EAST;
import static com.rdpk.mars.domain.Direction.NORTH;
import static com.rdpk.mars.domain.MoveRoverAction.*;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class AcceptanceTest {

  Plateau plateau;

  @BeforeEach
  void setUp() {
    plateau = new Plateau("test-1");
  }

  @Test
  @DisplayName("the solution must work")
  void a5() {

    plateau.resize(new Coordinates(5, 5));

    // rover 1
    plateau.activate(new Coordinates(1, 2), NORTH);
    // LMLMLMLMM
    plateau.move(asList(TURN_LEFT, WALK, TURN_LEFT, WALK, TURN_LEFT, WALK, TURN_LEFT, WALK, WALK));
    assertThat(plateau.activeRover.location).isEqualTo(new Coordinates(1, 3));
    assertThat(plateau.activeRover.direction).isEqualTo(NORTH);

    // rover 2
    plateau.activate(new Coordinates(3, 3), EAST);
    // MMRMMRMRRM
    plateau.move(asList(WALK, WALK, TURN_RIGHT, WALK, WALK, TURN_RIGHT, WALK, TURN_RIGHT, TURN_RIGHT, WALK));
    assertThat(plateau.activeRover.location).isEqualTo(new Coordinates(5, 1));
    assertThat(plateau.activeRover.direction).isEqualTo(EAST);

  }

}
