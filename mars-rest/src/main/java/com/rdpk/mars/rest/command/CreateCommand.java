package com.rdpk.mars.rest.command;

public class CreateCommand {

  public final String id;

  public CreateCommand(String id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "CreateCommand{" +
            "id='" + id + '\'' +
            '}';
  }
}
