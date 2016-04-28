package com.rdpk.mars;

import lombok.Value;

/**
 * Renders the status in the protocol format
 */
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
