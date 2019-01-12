package com.rdpk.mars.read;

import com.rdpk.mars.domain.Plateau;
import com.rdpk.mars.domain.Rover;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class PlateauReadRepository {

  private final ConcurrentHashMap<String, Plateau> storage;

  public PlateauReadRepository(ConcurrentHashMap<String, Plateau> storage) {
    this.storage = storage;
  }

  public Optional<PlateauReadModel> find(String id) {
    System.out.println("id =" + id);
    return getPlateaus().stream().filter(plateau -> plateau.id.equals(id)).findFirst();
  }

  public List<PlateauReadModel> getPlateaus() {
    return storage.values().stream().map(plateau -> {
      System.out.println("plateau \n" + plateau);
      List<RoverReadModel> plateaus = plateau.rovers.stream()
              .map(this::convert).collect(Collectors.toList());
      System.out.println("plateau roovers\n" + plateau);
      return new PlateauReadModel(plateau.name, plateau.topRight.x, plateau.topRight.y,
              plateaus, convert(plateau.activeRover));
    }).collect(Collectors.toList());
  }

  private RoverReadModel convert(Rover rover) {
    System.out.println("converting " + rover);
    return rover == null ? null : new RoverReadModel(rover.id, rover.location.x, rover.location.y,
            rover.direction.name().substring(0, 1).toUpperCase());
  }

}
