package com.rdpk.mars.servlet;

import com.rdpk.mars.Direction;
import com.rdpk.mars.Location;
import com.rdpk.mars.Plateau;
import com.rdpk.mars.Rover;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class ProtocolResponseHelperTest {

    @Test
    public void status_with_one_rover() {

        Plateau plateau = new Plateau("plateau-1", 5, 5);
        Rover rover1 = new Rover("rover1");
        rover1.land(plateau, new Location(1, 2), Direction.SOUTH);

        ProtocolResponseHelper h = new ProtocolResponseHelper(plateau);

        assertEquals("1 2 S\n", h.getStatus());

    }

    @Test
    public void status_with_two_rovers() {

        Plateau plateau = new Plateau("plateau-1", 5, 5);
        Rover rover1 = new Rover("rover1");
        rover1.land(plateau, new Location(1, 2), Direction.SOUTH);
        Rover rover2 = new Rover("rover2");
        rover2.land(plateau, new Location(2, 3), Direction.WEST);

        ProtocolResponseHelper h = new ProtocolResponseHelper(plateau);

        assertEquals("1 2 S\n2 3 W\n", h.getStatus());

    }

}