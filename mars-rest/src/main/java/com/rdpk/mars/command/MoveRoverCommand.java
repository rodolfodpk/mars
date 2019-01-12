package com.rdpk.mars.command;

import com.rdpk.mars.domain.MoveRoverAction;

import java.util.List;

public class MoveRoverCommand {

  public final String plateauId;
  public final List<MoveRoverAction> moves;

  public MoveRoverCommand(String plateauId, List<MoveRoverAction> moves) {
    this.plateauId = plateauId;
    this.moves = moves;
  }
}
