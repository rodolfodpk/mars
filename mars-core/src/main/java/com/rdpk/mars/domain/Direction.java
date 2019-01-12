package com.rdpk.mars.domain;


/**
 * Rover direction
 */
public enum Direction {
  NORTH,
  SOUTH,
  EAST,
  WEST;

  public static Direction find(String character) {
    switch (character) {
      case "N": return NORTH;
      case "S": return SOUTH;
      case "E": return EAST;
      case "W": return WEST;
      default: return null;
    }
  }

}
