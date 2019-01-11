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
  @DisplayName("it compares coordinates dimensions")
  void a2() {
    Coordinates topRight = new Coordinates(5, 5);
    assertThat(topRight.isBiggerThan(new Coordinates(6, 6))).isFalse();
  }

}
