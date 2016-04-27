package com.rdpk.mars;

import org.junit.Test;

public class LocationTests {
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidLocation() {
		
		@SuppressWarnings("unused")
		Location location = new Location(0, -1);
		
	}

}
