package com.rdpk.mars.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CoordinatesTest {

  @Test
  @DisplayName("it checks for invalid coordinates")
  void a1() {
    assertThrows(IllegalArgumentException.class, () -> new Coordinates(0, -1));
  }

  @Test
  @DisplayName("6,6 is bigger than 5,5")
  void a2() {
    Coordinates topRight = new Coordinates(5, 5);
    assertThat(topRight.isBiggerThan(new Coordinates(6, 6))).isFalse();
  }

  @Test
  @DisplayName("4,5 is bigger than 4,4")
  void a3() {
    Coordinates topRight = new Coordinates(4, 5);
    assertThat(topRight.isBiggerThan(new Coordinates(4, 4))).isTrue();
  }

  @Test
  @DisplayName("3,5 is bigger than 4,4")
  void a4() {
    Coordinates topRight = new Coordinates(3, 5);
    assertThat(topRight.isBiggerThan(new Coordinates(4, 4))).isTrue();
  }

  @Test
  @DisplayName("3,4 is not bigger than 4,4")
  void a5() {
    Coordinates topRight = new Coordinates(3, 4);
    assertThat(topRight.isBiggerThan(new Coordinates(4, 4))).isFalse();
  }
}
