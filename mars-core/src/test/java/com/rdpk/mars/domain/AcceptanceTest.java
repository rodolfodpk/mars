package com.rdpk.mars.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.rdpk.mars.domain.Direction.EAST;
import static com.rdpk.mars.domain.Direction.NORTH;
import static com.rdpk.mars.domain.MoveRoverAction.*;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class AcceptanceTest {

  Plateau plateau;

  @BeforeEach
  void setUp() {
    plateau = new Plateau("test-1");
  }

  @Test
  @DisplayName("it can check if a coordinate is within this plateau")
  void a5() {
    plateau.resize(new Coordinates(5, 5));
    plateau.activate(new Coordinates(1, 2), NORTH);
    plateau.move(asList(TURN_LEFT, WALK, TURN_LEFT, WALK, TURN_LEFT, WALK, TURN_LEFT, WALK, WALK));
    assertThat(plateau.activeRover.location).isEqualTo(new Coordinates(3, 3));
    assertThat(plateau.activeRover.direction).isEqualTo(EAST);
  }



}
