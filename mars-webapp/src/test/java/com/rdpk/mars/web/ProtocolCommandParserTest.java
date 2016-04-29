package com.rdpk.mars.web;

import com.rdpk.mars.Direction;
import com.rdpk.mars.Location;
import com.rdpk.mars.ProtocolCommandParser;
import javaslang.Tuple;
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
        assertEquals(Tuple.of(5, 5), p.plateauAttributes());
    }

    @Test
    public void rover_coordinates() {
        ProtocolCommandParser p = new ProtocolCommandParser("1 2 N");
        assertFalse(p.isPlateauCreation());
        assertTrue(p.isRoverCoordinates());
        assertFalse(p.isRoverMoving());
        assertEquals(Tuple.of(new Location(1, 2), Direction.NORTH), p.roverAttributes());
    }

}