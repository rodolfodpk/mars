package com.rdpk.mars.servlet;

import com.rdpk.mars.Location;
import com.rdpk.mars.exceptions.IllegalOperation;
import com.rdpk.mars.servet.MarsMissionController;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MarsMissionControllerTest {

	@Test(expected = IllegalOperation.class)
	public void cmd_without_previsou_plateau() {
		MarsMissionController o = new MarsMissionController() ;
		o.executeCommand("5 5 M") ;
	}

	@Test
	public void create_plateau() {
		MarsMissionController o = new MarsMissionController() ;
		o.executeCommand("5 10") ;
		assertTrue(o.getPlateau().getXSize()==5 && o.getPlateau().getYSize()==10) ;
	}

	@Test(expected = IllegalOperation.class)
	public void invalid_rover_plateau() {
		MarsMissionController o = new MarsMissionController() ;
		o.executeCommand("5 10") ;
		o.executeCommand("1 2 X") ;
	}

    @Test
    public void finally_lets_see_that_input_and_output() {
		/*
		 * Test Input:
			5 5
			1 2 N
			LMLMLMLMM
			3 3 E
			MMRMMRMRRM

			Expected Output:
			1 3 N
			5 1 E
		 */
        MarsMissionController o = new MarsMissionController() ;

        // plateau
        o.executeCommand("5 5") ;

        // rover1
        o.executeCommand("1 2 N") ;
        o.executeCommand("LMLMLMLMM") ;

        // rover 2
        o.executeCommand("3 3 E") ;
        o.executeCommand("MMRMMRMRRM") ;

        // expected output
        assertEquals(o.getStatus(), "1 3 N\n5 1 E\n");

        // also asserts there are 2 rovers
        assertEquals(o.getPlateau().getRovers().size(), 2);

    }

    @Test
    public void io_test_one_rover_moving() {
        MarsMissionController o = new MarsMissionController() ;
        o.executeCommand("5 10") ;
        o.executeCommand("1 2 N") ;
        o.executeCommand("LMLMLMLMM") ;
        assertEquals(o.getStatus(), "1 3 N\n");
    }

    @Test
    public void io_test_one_rover_without_moving() {
        MarsMissionController o = new MarsMissionController() ;
        o.executeCommand("5 10") ;
        o.executeCommand("1 2 N") ;
        assertTrue(o.getPlateau().isLocationBusy(new Location(1, 2))) ;
        assertEquals(o.getStatus(), "1 2 N\n");
    }

}
