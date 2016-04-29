package com.rdpk.mars.web;

import com.rdpk.mars.MarsMissionController;
import com.rdpk.mars.Mission;
import lombok.val;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

public class MissionAcceptanceTest {

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
        val controller = new MarsMissionController(new Mission(), new AtomicInteger()) ;

        // plateau
        controller.executeCommand("5 5") ;

        // rover1
        controller.executeCommand("1 2 N") ;
        controller.executeCommand("LMLMLMLMM") ;

        // rover 2
        controller.executeCommand("3 3 E") ;
        controller.executeCommand("MMRMMRMRRM") ;

        // expected output
        assertEquals(controller.getStatus(), "1 3 N\n5 1 E\n");

    }

    @Test
    public void io_test_one_rover_moving() {
        val controller = new MarsMissionController(new Mission(), new AtomicInteger()) ;
        controller.executeCommand("5 10") ;
        controller.executeCommand("1 2 N") ;
        controller.executeCommand("LMLMLMLMM") ;
        assertEquals(controller.getStatus(), "1 3 N\n");
    }

    @Test
    public void io_test_one_rover_without_moving() {
        val controller = new MarsMissionController(new Mission(), new AtomicInteger()) ;
        controller.executeCommand("5 10") ;
        controller.executeCommand("1 2 N") ;
        assertEquals(controller.getStatus(), "1 2 N\n");
    }

}
