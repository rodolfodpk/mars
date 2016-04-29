package com.rdpk.mars.helpers;

import com.rdpk.mars.Rover;
import lombok.val;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class RoverStepsParserTest {

    @Test
    public void rover_move_steps() {
        val p = new RoverStepsHelper("LRMM", new Rover("rover-1"));
        assertEquals(4, p.roverSteps().size());
    }

}