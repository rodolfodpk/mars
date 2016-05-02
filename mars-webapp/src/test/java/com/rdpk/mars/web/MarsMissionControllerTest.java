package com.rdpk.mars.web;

import com.rdpk.mars.Direction;
import com.rdpk.mars.Location;
import com.rdpk.mars.Mission;
import com.rdpk.mars.web.MarsMissionController;
import lombok.val;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MarsMissionControllerTest {

    @Test
    public void create_plateau() {
        val mission = mock(Mission.class);
        val controller = new MarsMissionController(mission, new AtomicInteger()) ;
        controller.executeCommand("5 10") ;
        verify(mission).createPlateau(any(String.class), eq(5), eq(10));
    }

    @Test
    public void must_land_or_locate_rover() {
	    val mission = mock(Mission.class);
		val controller = new MarsMissionController(mission, new AtomicInteger()) ;
		controller.executeCommand("5 5 N") ;
        verify(mission).landOrSerTargerRover(any(String.class), eq(new Location(5, 5)), eq(Direction.NORTH));
	}

    @Test
	public void must_move_rover() {
        val mission = mock(Mission.class);
        val controller = new MarsMissionController(mission, new AtomicInteger()) ;
        controller.executeCommand("MMMLRRRLMM") ;
        verify(mission).moveRover("MMMLRRRLMM");
	}

}
