package com.rdpk.mars.command;

import com.rdpk.mars.domain.MoveRoverAction;
import lombok.Value;

import java.util.List;

@Value
public class MoveRoverCommand {

  String plateauName;
  List<MoveRoverAction> moves;

}
