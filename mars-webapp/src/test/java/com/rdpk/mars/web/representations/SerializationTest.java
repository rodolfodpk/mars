package com.rdpk.mars.web.representations;

import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.rdpk.mars.Direction;
import com.rdpk.mars.Location;
import com.rdpk.mars.Mission;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class SerializationTest {

    ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDefaultPrettyPrinter(new MinimalPrettyPrinter());
        mapper.registerModule(new Jdk8Module());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test @Ignore
    public void mission() throws IOException {

        Mission mission = new Mission();
        mission.createPlateau("plateau-1", 5, 5);
        mission.landOrSerTargerRover("rover-1", new Location(0, 0), Direction.NORTH);
        mission.landOrSerTargerRover("rover-2", new Location(1, 1), Direction.SOUTH);

        String asJson = mapper.writeValueAsString(mission);

        assertEquals(mapper.readValue(asJson, Mission.class), mission);

    }

    @Test
    public void mission_representation() throws IOException {

        Mission mission = new Mission();
        mission.createPlateau("plateau-1", 5, 5);
        mission.landOrSerTargerRover("rover-1", new Location(0, 0), Direction.NORTH);
        mission.landOrSerTargerRover("rover-2", new Location(1, 1), Direction.SOUTH);

        MissionRepresentation repr = new MissionRepresentationFunction().apply(mission);

        String asJson = mapper.writeValueAsString(repr);

        assertEquals(mapper.readValue(asJson, MissionRepresentation.class), repr);

    }

}