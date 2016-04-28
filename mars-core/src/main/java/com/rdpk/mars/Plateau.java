package com.rdpk.mars;

import com.rdpk.mars.exceptions.LocationConflict;
import com.rdpk.mars.exceptions.UnknownLocation;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

/**
 * A Plateau to be explored by the Rovers squad
 */
@Value
@EqualsAndHashCode(exclude={"rovers"})
public class Plateau {

	String id;
	Set<Rover> rovers;
	int xSize;
	int ySize;

	public Plateau(String id, int xSize, int ySize) {
		this.id = id;
		if (xSize < 2 || ySize < 2) {
			throw new IllegalArgumentException("Invalid plateau dimension. x, y must be at least 2, 2 ") ;
		}
		this.rovers = new LinkedHashSet<>();
		this.xSize = xSize;
		this.ySize = ySize;
	}

	/**
	 * Try to receive a rover landing over here
	 * @param rover
	 * @param location
	 */
	public void addRover(Rover rover, Location location) {

		if (isLocationBusy(location)) {
			throw new LocationConflict() ;
		}
		if (isUnknownLocation(location)) {
			throw new UnknownLocation() ;
		}
		rovers.add(rover) ;

	}

	/**
	 * Check if this location already has a rover
	 * @param location
	 * @return isLocationBusy
	 */
	public boolean isLocationBusy(Location location) {

		for (Rover roverOverHere : rovers) {
			if (roverOverHere.getLocation().equals(location)) {
				return true ;
			}
		}
		return false ;
	}

	/**
	 * Check if this location has any rover e return it
	 * @param location
	 * @return rover
	 */
	public Optional<Rover> getRoverAt(Location location) {
		for (Rover roverOverHere : rovers) {
			if (roverOverHere.getLocation().equals(location)) {
				return Optional.of(roverOverHere) ;
			}
		}
		return Optional.empty();
	}

	/**
	 * Check if this location is within this plateau
	 * @param location
	 * @return isUnknownLocation
	 */
	public boolean isUnknownLocation(Location location) {

		return location.getX() > xSize ||
				location.getY() > ySize ;
	}

	/**
	 * Checks if this rover is landed here
	 * @param rover
	 * @return isLandedOverHere
	 */
	public boolean isLandedOverHere(Rover rover) {

		return rovers.contains(rover);
	}

}