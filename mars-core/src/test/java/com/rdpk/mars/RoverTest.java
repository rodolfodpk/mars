package com.rdpk.mars;

import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class RoverTest {

    final String roverId = "rover1";

	@Test
	public void after_creation_it_is_not_landed() {
		Rover rover = new Rover(roverId) ;
		assertFalse(rover.isLanded()); ;
	}

	@Test
	public void after_land_it_is_landed() {
		Rover rover = new Rover(roverId) ;
		Plateau plateau = new Plateau(roverId, 5, 5) ;
		rover.land(plateau, new Location(1, 2), Direction.NORTH) ;
		assertTrue(rover.isLanded()) ;
	}

	@Test
	public void after_landed_location_must_match() {
		Rover rover = new Rover(roverId) ;
		Plateau plateau = new Plateau(roverId, 5, 5) ;
		Location location = new Location(1, 2) ;
		rover.land(plateau, location, Direction.NORTH) ;
		assertEquals(rover.getLocation(), location) ;
	}

	@Test
	public void after_landed_direction_must_match() {
		Rover rover = new Rover(roverId) ;
		Plateau plateau = new Plateau(roverId, 5, 5) ;
		Location location = new Location(1, 2) ;
		rover.land(plateau, location, Direction.NORTH) ;
		assertEquals(rover.getDirection(), Direction.NORTH) ;
	}

	@Test
	public void move_to_north_twice_then_turn_to_right_and_move() {

		Rover rover = new Rover(roverId) ;
		Plateau plateau = new Plateau(roverId, 5, 5) ;
		Location initialLocation = new Location(0, 0) ;
		rover.land(plateau, initialLocation, Direction.NORTH) ;
		Location expectedLocation =  new Location(1, 2) ;

		rover.moveForward() ;
		rover.moveForward() ;
		rover.turnToRight() ;
		rover.moveForward() ;

		assertEquals(rover.getLocation(), expectedLocation) ;

	}

	@Test
	public void land_then_turn_and_move() {

		// 5 5

		// 1 2 N
		// LMLMLMLMM

		Rover rover = new Rover(roverId) ;
		Plateau plateau = new Plateau(roverId, 5, 5) ;
		Location initialLocation = new Location(1, 2) ;
		rover.land(plateau, initialLocation, Direction.NORTH) ;
		Location expectedLocation =  new Location(1, 3) ; // 1 3 N

		rover.turnToLeft() ;
		rover.moveForward() ;

		rover.turnToLeft() ;
		rover.moveForward() ;

		rover.turnToLeft() ;
		rover.moveForward() ;

		rover.turnToLeft() ;
		rover.moveForward() ;
		rover.moveForward() ;

		assertEquals(rover.getLocation(), expectedLocation) ;
		assertEquals(rover.getDirection(), Direction.NORTH) ;

	}

	@Test
	public void land_then_turn_and_move2() {

		// 5 5

		// 3 3 E
		// MMRMMRMRRM

		Rover rover = new Rover(roverId) ;
		Plateau plateau = new Plateau(roverId, 5, 5) ;
		Location initialLocation = new Location(3, 3) ;
		Location expectedLocation =  new Location(5, 1) ; // 5 1 E

		rover.land(plateau, initialLocation, Direction.EAST) ;
		rover.moveForward() ;
		rover.moveForward() ;

		rover.turnToRight() ;

		rover.moveForward() ;
		rover.moveForward() ;

		rover.turnToRight() ;

		rover.moveForward() ;

		rover.turnToRight() ;
		rover.turnToRight() ;

		rover.moveForward() ;

		assertEquals(rover.getLocation(), expectedLocation) ;
		assertEquals(rover.getDirection(), Direction.EAST) ;

	}

}
