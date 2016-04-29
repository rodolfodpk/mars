package com.rdpk.mars;

import com.rdpk.mars.states.InitialState;
import com.rdpk.mars.states.PlateauWasCreated;
import com.rdpk.mars.states.RoverIsTargeted;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Builder @AllArgsConstructor
public class Mission {

    private final Logger log = LoggerFactory.getLogger(Mission.class);

    @Getter private InitialState initialState;
    @Getter private PlateauWasCreated plateauWasCreated;
    @Getter private RoverIsTargeted roverIsTargeted;

    @Getter @Setter MissionContextState contextState;

    @Getter @Setter private Plateau plateau;
    @Getter @Setter private Rover targetRover;

    public Mission() {
        this.initialState = new InitialState(this);
        this.plateauWasCreated = new PlateauWasCreated(this);
        this.roverIsTargeted = new RoverIsTargeted(this);
        this.contextState = initialState;
    }

    public void createPlateau(String id, int xSize, int ySize) {
        contextState.createPlateau(id, xSize, ySize);
    }

    public void landOrSerTargerRover(String roverId, Location location, Direction direction) {
        contextState.landOrSetTargetRover(roverId, location, direction);
    }

    public void moveRover(String moveCommand) {
        contextState.moveRover(moveCommand);
    }

    public Rover getRoverAt(Location location) {
        return plateau.getRoverAt(location).orElse(null);
    }

}
