package com.rdpk.mars.console;

class ActivateRoover {
  final Integer x;
  final Integer y;
  final Character direction;

  ActivateRoover(Integer x, Integer y, Character direction) {
    this.x = x;
    this.y = y;
    this.direction = direction;
  }

  @Override
  public String toString() {
    return "ActivateRoover{" +
            "x=" + x +
            ", y=" + y +
            ", direction=" + direction +
            '}';
  }
}
