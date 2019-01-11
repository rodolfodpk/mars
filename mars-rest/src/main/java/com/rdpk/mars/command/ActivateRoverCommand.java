package com.rdpk.mars.command;

import com.rdpk.mars.domain.Coordinates;
import com.rdpk.mars.domain.Direction;
import lombok.Value;

@Value
public class ActivateRoverCommand {

  String plateauName;
  Coordinates location;
  Direction direction;

}
