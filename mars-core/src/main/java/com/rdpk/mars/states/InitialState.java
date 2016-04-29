package com.rdpk.mars.states;

import com.rdpk.mars.*;
import lombok.Value;

@Value
public class InitialState implements MissionContextState {

    Mission mission;

    @Override
    public void createPlateau(String id, int xSize, int ySize) {
        if (mission.getPlateau()!=null) {
            throw new IllegalStateException("plateau is already created");
        }
        mission.setPlateau(new Plateau(id, xSize, ySize));
        mission.setContextState(mission.getPlateauWasCreated());
    }

    @Override
    public void landOrSetTargetRover(String roverId, Location location, Direction direction) {
        throw new IllegalStateException("before this operation, you must create a plateau");
    }

    @Override
    public void moveRover(String moveCommand) {
        throw new IllegalStateException("before this operation, you must create a plateau");
    }
}
