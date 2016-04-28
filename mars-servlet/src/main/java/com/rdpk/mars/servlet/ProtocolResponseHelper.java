package com.rdpk.mars.servlet;

import com.rdpk.mars.Plateau;
import com.rdpk.mars.Rover;
import lombok.Value;

@Value
public class ProtocolResponseHelper {

    Plateau plateau;

    /**
     * Return the current status of the Plateau in text format
     * @return status
     */
    public String getStatus() {
        StringBuffer status = new StringBuffer();
        for (Rover rover : plateau.getRovers()) {
            status.append(rover.getLocation() + " " + rover.getDirection().name().charAt(0)
                    + "\n");
        }
        return status.toString();
    }

}
