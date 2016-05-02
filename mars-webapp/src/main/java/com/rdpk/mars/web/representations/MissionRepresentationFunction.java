package com.rdpk.mars.web.representations;

import com.rdpk.mars.Mission;

import java.util.function.Function;

public class MissionRepresentationFunction implements Function<Mission, MissionRepresentation> {
    @Override
    public MissionRepresentation apply(Mission mission) {
        return new MissionRepresentation(
                mission.getContextState().getClass().getSimpleName(),
                new PlateauRepresentationFunction().apply(mission.getPlateau()),
                new RoverRepresentationFunction().apply(mission.getTargetRover()));
    }
}
