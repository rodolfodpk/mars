package com.rdpk.mars.command;

import com.rdpk.mars.domain.Plateau;

import java.util.Map;

class PlateauRepository {

  private final Map<String, Plateau> storage;

  PlateauRepository(Map<String, Plateau> storage) {
    this.storage = storage;
  }

  void create(String name) {
    if (storage.containsKey(name))
      throw new IllegalArgumentException(String.format("Plateau %s already exists", name));
    storage.put(name,new Plateau(name) );
  }

  Plateau findByName(String name) {
    return storage.get(name);
  }

}
