package com.rdpk.mars.servet;

import com.rdpk.mars.Plateau;
import com.rdpk.mars.Rover;
import com.rdpk.mars.exceptions.IllegalOperation;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * An object to receive text commands from NASA, validate it, interpret it and finally proceed
 */
public class MarsMissionController {

    private final Logger log = LoggerFactory.getLogger(MarsMissionController.class);

    @Getter private Plateau plateau;
    @Getter Optional<Rover> targetRover = Optional.empty();

    private final AtomicInteger roverIdCounter = new AtomicInteger();

    /**
     * Execute a command received from NASA.
     * @param command
     */
    public void executeCommand(String command) {

        ProtocolCommandParser parser = new ProtocolCommandParser(command);

        if (plateau == null && !parser.isPlateauCreation()) {
            throw new IllegalOperation("The first command must be a plateau creation");
        }

        if (parser.isPlateauCreation()) {
            log.info("Plateau created");
            this.plateau = parser.plateau("plateau-1");
            return ;
        }

        if (parser.isRoverCoordinates()) {
            targetRover = plateau.getRoverAt(parser.location());
            if (!targetRover.isPresent()) {
                log.info("Rover landing");
                targetRover = Optional.of(new Rover(String.format("rover-%d", roverIdCounter.incrementAndGet())));
                targetRover.get().land(plateau, parser.location(), parser.direction());
            }
            return;
        }

        if (parser.isRoverMoving()) {
            log.info("Rover moving");
            if (targetRover.isPresent()) {
                log.info("Rover moving");
                parser.roverSteps(targetRover.get()).forEach(Runnable::run);
            } else {
                throw new IllegalOperation("There is not any rover in context to move");
            }
            return;
        }

        throw new IllegalOperation("Unknown command");
    }

    /**
     * Return the current status of the Plateau in text format
     * @return status
     */
    public String getStatus() {
        return new ProtocolResponseHelper(plateau).getStatus();
    }

}