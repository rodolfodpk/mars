package com.rdpk.mars.web;

import com.rdpk.mars.web.representations.MissionRepresentation;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.*;
import org.junit.runners.MethodSorters;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HappyPathScenarioTest {

    private static final String CONFIG_PATH = ResourceHelpers.resourceFilePath("mars-config.yaml");

    @ClassRule
    public static final DropwizardAppRule<MarsMIssionConfiguration> RULE = new DropwizardAppRule<>(
            MarsMissionApplication.class, CONFIG_PATH);

            // ConfigOverride.config("database.url", "jdbc:h2:" + TMP_FILE));

    private Client client;

    @Before
    public void setUp() throws Exception {
        client = ClientBuilder.newClient();
    }

    @After
    public void tearDown() throws Exception {
        client.close();
    }


    @Test
    public void stage1_post_plateau_creation() throws Exception {

        Form form = new Form();
        form.param("missionCommand", "5 5");

        final Response response = client.target("http://localhost:" + RULE.getLocalPort() + "/mars")
                .request().buildPost(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE))
                .invoke();

        assertEquals(200, response.getStatus());
        assertFalse(response.hasEntity());

    }

    @Test
    public void stage2_post_land_rover_cmd() throws Exception {

        Form form = new Form();
        form.param("missionCommand", "0 0 N");

        final Response response = client.target("http://localhost:" + RULE.getLocalPort() + "/mars")
                .request().buildPost(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE))
                .invoke();

        assertEquals(response.getStatus(), 200);
        assertEquals(response.readEntity(String.class), "0 0 N\n");

    }

    @Test
    public void stage3_move_rover_cmd() throws Exception {

        Form form = new Form();
        form.param("missionCommand", "MM");

        final Response response = client.target("http://localhost:" + RULE.getLocalPort() + "/mars")
                .request().buildPost(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE))
                .invoke();

        assertEquals(response.getStatus(), 200);
        assertEquals(response.readEntity(String.class), "0 2 N\n");

    }

    @Test @Ignore // TODO getting ser/des error
    public void stage4_get_on_empty_mission_must_return_no_content() throws Exception {

        final Response response = client.target("http://localhost:" + RULE.getLocalPort() + "/mars")
                .request().buildGet().invoke();

        assertEquals(200, response.getStatus());

        MissionRepresentation missionRepr = response.readEntity(MissionRepresentation.class);

        System.out.println(missionRepr);
    }


}
