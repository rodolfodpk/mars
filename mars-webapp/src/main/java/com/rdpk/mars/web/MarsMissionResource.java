package com.rdpk.mars.web;

import com.rdpk.mars.MarsMissionController;
import com.rdpk.mars.web.representations.PlateauRepresentation;
import com.rdpk.mars.web.representations.PlateauRepresentationFunction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

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

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(
            value = "Submit a command to Mars Mission",
            notes = "You just need to use our text protocol"
    )
    public String postForToken(
            @FormParam("plateauCommand") @ApiParam(defaultValue = "5 5") String plateauCommand) {

        System.out.println("*** command -> " + plateauCommand);
        controller.executeCommand(plateauCommand);

        return controller.getStatus();

    }

    @GET
    @ApiOperation(value = "Get the Plateau representation", response = PlateauRepresentation.class)
    @Produces(MediaType.APPLICATION_JSON)
    public Response get() {
        if (controller.getPlateau()==null) {
            return Response.noContent().build();
        }
        return Response.ok(new PlateauRepresentationFunction().apply(controller.getPlateau())).build();
    }

}
