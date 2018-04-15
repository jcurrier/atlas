package com.amazon.atlas.resources;

import com.amazon.atlas.data.Sprint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/sprints")
@Produces(MediaType.APPLICATION_JSON)
public class SprintResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(SprintResource.class);

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") String sprintId) {
        LOGGER.info("fetch sprint request id = {s}", sprintId);

        return Response.ok("", MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Sprint newSprint) {

        LOGGER.info("create sprint request for {%s}", newSprint.getId());

        return Response.ok("", MediaType.APPLICATION_JSON).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") String sprintId, Sprint sprint) {

        LOGGER.info("update sprint request for {%s}", sprintId);

        return Response.ok("", MediaType.APPLICATION_JSON).build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") String sprintId) {

        LOGGER.info("delete sprint request for {%s}", sprintId);

        return Response.ok("", MediaType.APPLICATION_JSON).build();
    }
}
