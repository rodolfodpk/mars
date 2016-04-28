package com.rdpk.mars.web;

import com.rdpk.mars.MarsMissionController;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * A Resource for REST interface
 * Actually this is not RESTFUL but the purpose here is only to serve as an UI for this code challenge
 */
@Path("/mars")
@Api(value = "/mars", description = "Just an UI for our Mars Mission code challenge")
public class MarsMissionResource {

    private final MarsMissionController controller;

    public MarsMissionResource(MarsMissionController controller) {
        this.controller = controller;
    }

    @GET
    @ApiOperation(value = "Get the Plateau representation", response = MissionResponse.class)
    public Response get() {
        return Response.ok(controller.getPlateau()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation(
            value = "Submit a command to Mars Mission",
            notes = "You just need to use our text protocol",
            response = MissionResponse.class
    )
    public MissionResponse postForToken(
            @FormParam("plateauCommand") @ApiParam(defaultValue = "5 5") String plateauCommand) {

        controller.executeCommand(plateauCommand);

        return new MissionResponse(controller.getStatus());

    }
}
