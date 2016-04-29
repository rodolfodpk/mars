package com.rdpk.mars;

import lombok.Getter;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * An object to receive text commands from NASA, validate it, interpret it and finally proceed
 */
public class MarsMissionController {

    private final Logger log = LoggerFactory.getLogger(MarsMissionController.class);

    @Getter
    private final Mission mission ;
    private final AtomicInteger roverIdCounter;

    public MarsMissionController(Mission mission, AtomicInteger roverIdCounter) {
        this.mission = mission;
        this.roverIdCounter = roverIdCounter;
    }

    /**
     * Execute a command received from NASA.
     * @param command
     */
    public void executeCommand(String command) {

        ProtocolCommandParser parser = new ProtocolCommandParser(command);

        if (parser.isPlateauCreation()) {
            log.info("trying to create plateau");
            val tuple = parser.plateauAttributes();
            mission.createPlateau("target-plateau", tuple._1(), tuple._2());
            return ;
        }

        if (parser.isRoverCoordinates()) {
            log.info("trying to land or locate rover");
            val tuple = parser.roverAttributes();
            mission.landOrSerTargerRover(nextRoverId(), tuple._1(), tuple._2());
            return ;
        }

        if (parser.isRoverMoving()) {
            log.info("trying to move rover");
            mission.moveRover(command);
            return ;
        }

        throw new IllegalArgumentException("Unknown command");
    }

    /**
     * Return the current status of the Plateau in text format
     * @return status
     */
    public String getStatus() {
        return new ProtocolResponseHelper(mission.getPlateau()).getStatus();
    }

    private String nextRoverId() {
        return String.format("rover-%d", roverIdCounter.incrementAndGet());
    }

}