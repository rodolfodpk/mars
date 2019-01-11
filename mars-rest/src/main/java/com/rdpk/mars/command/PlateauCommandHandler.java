package com.rdpk.mars.command;

public class PlateauCommandHandler {

  private final PlateauRepository repository;

  public PlateauCommandHandler(PlateauRepository repository) {
    this.repository = repository;
  }

  public void create(CreateCommand cmd) {
    repository.create(cmd.getName());
  }

  public void resize(ResizeAreaCommand cmd) {
    repository.findByName(cmd.getPlateauName()).resize(cmd.getTopRightCoordinates());
  }

  public void activate(ActivateRoverCommand cmd) {
    repository.findByName(cmd.getPlateauName()).activate(cmd.getLocation(), cmd.getDirection());
  }

  public void move(MoveRoverCommand cmd) {
    repository.findByName(cmd.getPlateauName()).move(cmd.getMoves());
  }

}


