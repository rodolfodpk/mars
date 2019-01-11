package com.rdpk.mars.read;

import com.rdpk.mars.domain.Plateau;

import java.util.*;
import java.util.stream.Collectors;

public class PlateauReadRepository {

  private final Map<String, Plateau> storage;

  public PlateauReadRepository(Map<String, Plateau> storage) {
    this.storage = storage;
  }

  public List<PlateauReadModel> getPlateaus() {
    return storage.values().stream().map(plateau -> {
      Set<RoverReadModel> plateaus = plateau.rovers.stream()
              .map(rover -> new RoverReadModel(rover.location.getX(), rover.location.getY(),
                      rover.direction.name().substring(0, 1).toUpperCase())).collect(Collectors.toSet());
      return new PlateauReadModel(plateau.name, plateau.topRight.getX(), plateau.topRight.getY(), plateaus);
    }).collect(Collectors.toList());
  }

}
