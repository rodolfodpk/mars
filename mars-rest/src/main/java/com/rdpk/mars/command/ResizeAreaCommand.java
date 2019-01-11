package com.rdpk.mars.command;

import com.rdpk.mars.domain.Coordinates;
import lombok.Value;

@Value
public class ResizeAreaCommand {

  String plateauName;
  Coordinates topRightCoordinates;

}
