package com.rdpk.mars.rest.command;

public class PlateauCommandHandler {

  private final PlateauRepository repository;

  public PlateauCommandHandler(PlateauRepository repository) {
    this.repository = repository;
  }

  public void create(CreateCommand cmd) {
    repository.create(cmd.id);
  }

  public void resize(ResizeAreaCommand cmd) {
    repository.findByName(cmd.plateauId).resize(cmd.topRightCoordinates);
  }

  public void activate(ActivateRoverCommand cmd) {
    repository.findByName(cmd.plateauId).activate(cmd.location, cmd.direction);
  }

  public void move(MoveRoverCommand cmd) {
    repository.findByName(cmd.plateauId).move(cmd.moves);
  }

}


