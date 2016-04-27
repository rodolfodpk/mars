package com.rdpk.mars.servlet;

import com.rdpk.mars.exceptions.IllegalOperation;
import com.rdpk.mars.servet.MarsMissionController;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MarsMissionControllerTest {

	@Test(expected = IllegalOperation.class)
	public void sendCommandWithoutPlateau() {
		MarsMissionController o = new MarsMissionController() ;
		o.executeCommand("5 5 M") ;
	}

	@Test
	public void sendPlateauCommand() {
		MarsMissionController o = new MarsMissionController() ;
		o.executeCommand("5 10") ;
		assertTrue(o.getPlateau().getXSize()==5 && o.getPlateau().getYSize()==10) ;
	}

	@Test(expected = IllegalOperation.class)
	public void sendInvalidLandRoverCommand() {
		MarsMissionController o = new MarsMissionController() ;
		o.executeCommand("5 10") ;
		o.executeCommand("1 2 X") ;
	}

}
