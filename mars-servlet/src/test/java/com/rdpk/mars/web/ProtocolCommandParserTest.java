package com.rdpk.mars.web;

import com.rdpk.mars.*;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class ProtocolCommandParserTest {

    @Test
    public void creating_a_plateau() {
        ProtocolCommandParser p = new ProtocolCommandParser("5 5");
        assertTrue(p.isPlateauCreation());
        assertFalse(p.isRoverCoordinates());
        assertFalse(p.isRoverMoving());
        assertEquals(new Plateau("plateau-1", 5, 5), p.plateau("plateau-1"));
    }

    @Test
    public void rover_coordinates() {
        ProtocolCommandParser p = new ProtocolCommandParser("1 2 N");
        assertFalse(p.isPlateauCreation());
        assertTrue(p.isRoverCoordinates());
        assertFalse(p.isRoverMoving());
        assertEquals(new Location(1, 2), p.location());
        assertEquals(Direction.NORTH, p.direction());
    }

    @Test
    public void rover_move() {
        ProtocolCommandParser p = new ProtocolCommandParser("LRMM");
        assertFalse(p.isPlateauCreation());
        assertFalse(p.isRoverCoordinates());
        assertTrue(p.isRoverMoving());
        assertEquals(4, p.roverSteps(new Rover("anyone")).size());
    }

}