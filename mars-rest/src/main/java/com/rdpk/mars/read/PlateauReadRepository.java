package com.rdpk.mars.read;

import com.rdpk.mars.domain.Plateau;
import com.rdpk.mars.domain.Rover;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PlateauReadRepository {

  private final Map<String, Plateau> storage;

  public PlateauReadRepository(Map<String, Plateau> storage) {
    this.storage = storage;
  }

  public List<PlateauReadModel> getPlateaus() {
    return storage.values().stream().map(plateau -> {
      Set<RoverReadModel> plateaus = plateau.rovers.stream()
              .map(this::convert).collect(Collectors.toSet());
      return new PlateauReadModel(plateau.name, plateau.topRight.x, plateau.topRight.y,
              plateaus, convert(plateau.activeRover));
    }).collect(Collectors.toList());
  }

  private RoverReadModel convert(Rover rover) {
    return new RoverReadModel(rover.id, rover.location.x, rover.location.y,
            rover.direction.name().substring(0, 1).toUpperCase());
  }

}
