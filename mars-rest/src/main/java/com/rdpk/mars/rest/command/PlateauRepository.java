package com.rdpk.mars.rest.command;

import com.rdpk.mars.domain.Plateau;

import java.util.concurrent.ConcurrentHashMap;

public class PlateauRepository {

  private final ConcurrentHashMap<String, Plateau> storage;

  public PlateauRepository(ConcurrentHashMap<String, Plateau> storage) {
    this.storage = storage;
  }

  void create(String id) {
    if (storage.containsKey(id))
      throw new IllegalArgumentException(String.format("Plateau %s already exists", id));
    storage.put(id, new Plateau(id));
  }

  Plateau findByName(String name) {
    return storage.get(name);
  }

}
