package com.rdpk.mars;

import com.rdpk.mars.exceptions.IllegalOperation;
import com.rdpk.mars.exceptions.LocationConflict;
import com.rdpk.mars.exceptions.RoverAlreadyLanded;
import com.rdpk.mars.exceptions.UnknownLocation;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * A Rover to explore Mars
 */
@Getter
public class Rover {

	private final String id;
    private final List<Location> pastLocations ;

    private boolean isLanded ;
    private Direction direction ;

	private Plateau plateau ;
    private Location location ;

	public Rover(String id) {
        this.id = id;
		this.pastLocations = new ArrayList<>() ;
        this.isLanded = false ;
	}

	/**
	 * Land over a plateau
	 * @param plateau
	 * @param location
	 * @param direction
	 */
	public void land(Plateau plateau, Location location, Direction direction) {

		if (this.isLanded) {
			throw new RoverAlreadyLanded() ;
		}

		plateau.addRover(this, location) ;

		this.isLanded = true ;
		this.plateau = plateau ;
		this.location = location ;
		this.direction = direction ;
	}

	/**
	 * Turn to left
	 */
	public void turnToLeft() {
		if (direction == Direction.NORTH) {
			direction = Direction.WEST ;
		} else if(direction == Direction.SOUTH) {
			direction = Direction.EAST ;
		} else if(direction == Direction.EAST) {
			direction = Direction.NORTH ;
		} else if(direction == Direction.WEST) {
			direction = Direction.SOUTH ;
		}
	}

	/**
	 * Turn to right
	 */
	public void turnToRight() {
		if (direction == Direction.NORTH) {
			direction = Direction.EAST ;
		} else if(direction == Direction.SOUTH) {
			direction = Direction.WEST ;
		} else if(direction == Direction.EAST) {
			direction = Direction.SOUTH ;
		} else if(direction == Direction.WEST) {
			direction = Direction.NORTH ;
		}
	}

	/**
	 * Move forward
	 */
	public void moveForward() {

		if (!isLanded) {
			throw new IllegalOperation("Rover must be landed before any move") ;

		}

		Location newLocation = null ;

		if (direction == Direction.NORTH) {
			newLocation = new Location(location.getX(), location.getY()+1) ;
		} else if(direction == Direction.SOUTH) {
			newLocation = new Location(location.getX(), location.getY()-1) ;
		} else if(direction == Direction.EAST) {
			newLocation = new Location(location.getX()+1, location.getY()) ;
		} else if(direction == Direction.WEST) {
			newLocation = new Location(location.getX()-1, location.getY()) ;
		}

		if (plateau.isLocationBusy(newLocation)) {
			throw new LocationConflict() ;
		}

		if (plateau.isUnknownLocation(newLocation)) {
			throw new UnknownLocation() ;
		}

		pastLocations.add(location) ;

		location = newLocation ;

		// System.out.println("Moved from " + pastLocations.get(pastLocations.size()-1) + " to " + location);

	}

    /**
     * Based only on it's ID field
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rover rover = (Rover) o;
        return id.equals(rover.id);
    }

    /**
     * Based only on it's ID field
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }

}