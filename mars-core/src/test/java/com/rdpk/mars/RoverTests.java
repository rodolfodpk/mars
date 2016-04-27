package com.rdpk.mars;

import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class RoverTests {

	@Test
	public void after_creation_it_is_not_landed() {
		Rover rover = new Rover() ;
		assertFalse(rover.isLanded); ;
	}

	@Test
	public void after_land_it_is_landed() {
		Rover rover = new Rover() ;
		Plateau plateau = new Plateau(5, 5) ;
		rover.land(plateau, new Location(1, 2), Direction.NORTH) ;
		assertTrue(rover.isLanded) ;
	}

	@Test
	public void after_landed_location_must_match() {
		Rover rover = new Rover() ;
		Plateau plateau = new Plateau(5, 5) ;
		Location location = new Location(1, 2) ;
		rover.land(plateau, location, Direction.NORTH) ;
		assertEquals(rover.getLocation(), location) ;
	}

	@Test
	public void after_landed_direction_must_match() {
		Rover rover = new Rover() ;
		Plateau plateau = new Plateau(5, 5) ;
		Location location = new Location(1, 2) ;
		rover.land(plateau, location, Direction.NORTH) ;
		assertEquals(rover.getDirection(), Direction.NORTH) ;
	}

}
