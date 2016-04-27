package com.rdpk.mars;

import lombok.Value;

/**
 * A location point given by x and y coordinates.
 */
@Value
public class Location {

	private final int x;
	private final int y;

	public Location(int x, int y) {
		if (x <0 || y <0) {
			throw new IllegalArgumentException("Invalid location. x, y must be zero or positive ") ;
		}
		this.x = x ;
		this.y = y ;
	}

	@Override
	public String toString() {
		return x + " " + y ;
	}

}
