package com.rdpk.mars.states;

import com.rdpk.mars.*;
import com.rdpk.mars.helpers.RoverStepsHelper;
import lombok.Value;
import lombok.val;

@Value
public class RoverIsTargeted implements MissionContextState {

    Mission mission;

    @Override
    public void createPlateau(String id, int xSize, int ySize) {
        throw new IllegalStateException("there are a plateau already");
    }

    @Override
    public void landOrSetTargetRover(String roverId, Location location, Direction direction) {
        Rover targetRover = mission.getRoverAt(location);
        if (targetRover == null) {
            targetRover = new Rover(roverId);
            targetRover.land(mission.getPlateau(), location, direction);
        }
        targetRover.changeDirection(direction);
        mission.setTargetRover(targetRover);
        mission.setContextState(mission.getRoverIsTargeted());
    }

    @Override
    public void moveRover(String moveCommand) {
        Rover targetRover = mission.getTargetRover();
        if (targetRover == null) {
            throw new IllegalStateException("inconsistent state: there should be a target rover");
        }
        val commandParser = new RoverStepsHelper(moveCommand, targetRover);
        commandParser.roverSteps().forEach(Runnable::run);
        mission.setContextState(mission.getRoverIsTargeted());
    }

}