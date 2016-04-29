package com.rdpk.mars;

public interface MissionContextState {

    void createPlateau(String id, int xSize, int ySize);

    void landOrSetTargetRover(String roverId, Location location, Direction direction);

    void moveRover(String moveCommand);

}
