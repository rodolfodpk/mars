package com.rdpk.mars.states;

import com.rdpk.mars.Direction;
import com.rdpk.mars.Location;
import com.rdpk.mars.Mission;
import com.rdpk.mars.Rover;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class PlateauWasCreatedTest {

    @Test(expected = IllegalStateException.class)
    public void must_fails_on_create_plateau() {
        Mission mission = new Mission();
        mission.setContextState(mission.getPlateauWasCreated());
        mission.createPlateau("rover-1", 5, 5);
    }

    @Test
    public void must_target_rover() {
        Mission mission = new Mission();
        mission.createPlateau("plateau-1", 5, 5);
        mission.landOrSerTargerRover("rover-1", new Location(0, 0), Direction.NORTH);
        assertEquals(new Rover("rover-1"), mission.getTargetRover());
        assertEquals(mission.getContextState(), mission.getRoverIsTargeted());
    }

    @Test
    public void must_change_rover_direction() {
        Mission mission = new Mission();
        mission.createPlateau("plateau-1", 5, 5);
        mission.landOrSerTargerRover("rover-1", new Location(0, 0), Direction.NORTH);
        mission.landOrSerTargerRover("rover-1", new Location(0, 0), Direction.SOUTH);
        assertEquals(new Rover("rover-1"), mission.getTargetRover());
        assertEquals(Direction.SOUTH, mission.getTargetRover().getDirection());
        assertEquals(mission.getContextState(), mission.getRoverIsTargeted());
    }

    @Test(expected = IllegalStateException.class)
    public void must_fail_on_move_rover() {
        Mission mission = new Mission();
        mission.createPlateau("plateau-1", 5, 5);
        mission.moveRover("MMMMM");
    }
}