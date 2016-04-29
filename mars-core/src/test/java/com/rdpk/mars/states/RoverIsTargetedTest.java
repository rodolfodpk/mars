package com.rdpk.mars.states;

import com.rdpk.mars.Direction;
import com.rdpk.mars.Location;
import com.rdpk.mars.Mission;
import com.rdpk.mars.Rover;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class RoverIsTargetedTest {

    @Test(expected = IllegalStateException.class)
    public void must_fails_on_create_plateau() {
        Mission mission = new Mission();
        mission.setContextState(mission.getPlateauWasCreated());
        mission.createPlateau("plateau-1", 5, 5);
    }

    @Test
    public void must_target_rover() {
        Mission mission = new Mission();
        mission.createPlateau("plateau-1", 5, 5);
        mission.landOrSerTargerRover("rover-1", new Location(0, 0), Direction.NORTH);
        // System.out.println(mission.getTargetRover());
        mission.landOrSerTargerRover("rover-2", new Location(1, 1), Direction.SOUTH);
        // System.out.println(mission.getTargetRover());
        assertEquals(new Rover("rover-2"), mission.getTargetRover());
        assertEquals(mission.getContextState(), mission.getRoverIsTargeted());
    }

    @Test
    public void must_move_rover() {
        Mission mission = new Mission();
        mission.createPlateau("plateau-1", 5, 5);
        mission.landOrSerTargerRover("rover-1", new Location(0, 0), Direction.NORTH);
        mission.moveRover("MMMMM");
        assertEquals(new Rover("rover-1"), mission.getTargetRover());
        assertEquals(mission.getContextState(), mission.getRoverIsTargeted());
    }

}