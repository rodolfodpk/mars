package com.rdpk.mars.states;

import com.rdpk.mars.Direction;
import com.rdpk.mars.Location;
import com.rdpk.mars.Mission;
import com.rdpk.mars.Plateau;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class InitialStateTest {

    @Test
    public void must_create_plateau() {
        Mission mission = new Mission();
        mission.createPlateau("plateau-1", 5, 5);
        assertEquals(mission.getPlateau(), new Plateau("plateau-1", 5, 5));
        assertEquals(mission.getContextState(), mission.getPlateauWasCreated());
    }

    @Test(expected = IllegalStateException.class)
    public void must_fail_on_target_rover() {
        Mission mission = new Mission();
        mission.landOrSerTargerRover("rover-1", new Location(0, 0), Direction.NORTH);
    }

    @Test(expected = IllegalStateException.class)
    public void must_fail_on_move_rover() {
        Mission mission = new Mission();
        mission.moveRover("MMMMM");
    }

}