package com.rdpk.mars;

import com.rdpk.mars.exceptions.LocationConflict;
import com.rdpk.mars.exceptions.RoverAlreadyLanded;
import com.rdpk.mars.exceptions.UnknownLocation;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


public class PlateauTest {

	final String id = "plateau1";

	@Test
	public void it_must_match_size() {

		Plateau p = new Plateau(id, 5, 5);
		assert (p.getXSize() == 5 && p.getYSize() == 5);
	}

	@Test
	public void it_must_be_landed() {

		Plateau plateau = new Plateau(id, 5, 5);
		Rover rover1 = new Rover(id);
		rover1.land(plateau, new Location(1, 2), Direction.NORTH);
		assertTrue(plateau.isLandedOverHere(rover1));

	}

	@Test(expected = RoverAlreadyLanded.class)
	public void it_must_fail_when_already_landed() {

		Plateau plateau = new Plateau(id, 5, 5);
		Rover rover1 = new Rover(id);
		rover1.land(plateau, new Location(1, 2), Direction.NORTH);
		rover1.land(plateau, new Location(1, 3), Direction.NORTH);
	}

	@Test(expected = LocationConflict.class)
	public void it_must_fail_on_location_conflict() {

		Plateau plateau = new Plateau(id, 5, 5);
		Location location = new Location(0, 0);
		Rover rover1 = new Rover(id);
		rover1.land(plateau, location, Direction.NORTH);
		Rover rover2 = new Rover(id);
		rover2.land(plateau, location, Direction.NORTH);

	}
	
	@Test(expected = UnknownLocation.class)
	public void it_must_fail_on_unknown_location() {
		
		Plateau plateau = new Plateau(id, 5, 5);
		Location location = new Location(6, 0);
		Rover rover1 = new Rover(id);
		rover1.land(plateau, location, Direction.NORTH);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void it_should_fail_on_invalid_dimension() {
		
		@SuppressWarnings("unused")
		Plateau plateau = new Plateau(id, 0, -1);
		
	}

}
