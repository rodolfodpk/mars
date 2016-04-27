package com.rdpk.mars;

import org.junit.Test;

public class LocationTests {
	
	@Test(expected = IllegalArgumentException.class)
	public void must_reject_on_invalid_location() {
		
		@SuppressWarnings("unused")
		Location location = new Location(0, -1);
		
	}

}
