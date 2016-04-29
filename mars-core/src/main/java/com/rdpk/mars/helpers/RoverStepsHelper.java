package com.rdpk.mars.helpers;

import com.rdpk.mars.Rover;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * A parser for rover steps
 */
public class RoverStepsHelper {

    @Getter
    private final String protocolCommand ;
    private final Rover targetRover;

    public RoverStepsHelper(String protocolCommand, Rover targetRover) {
        this.protocolCommand = protocolCommand;
        this.targetRover = targetRover;
    }

    public List<Runnable> roverSteps() {
        List<Runnable> stepsList = new ArrayList<>();
        // iterate our actions
        char[] actionsList = protocolCommand.toCharArray();
        for (int i = 0; i < actionsList.length; i++) {
            // System.out.println(i + " action for the rover = " + actionsList[i]) ;
            if (actionsList[i] == 'L') { // turn to left action
                stepsList.add(targetRover::turnToLeft) ;
            } else if (actionsList[i] == 'R') { // turn to right action
                stepsList.add(targetRover::turnToRight) ;
            } else if (actionsList[i] == 'M') { // move action
                stepsList.add(targetRover::moveForward) ;
            }
        }
        return stepsList;
    }

}
