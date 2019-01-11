package com.rdpk.mars.read;

import lombok.Value;

import java.util.Set;

@Value
public class PlateauReadModel {
  String id;
  int x;
  int y;
  Set<RoverReadModel> rovers;
}
