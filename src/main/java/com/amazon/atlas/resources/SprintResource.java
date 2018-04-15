package com.amazon.atlas.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/sprints")
@Produces(MediaType.APPLICATION_JSON)
public class SprintResource {

    @GET
    @Path("{id}")
    public Response getUser(@PathParam("id") String sprintId) {

        return Response.ok("", MediaType.APPLICATION_JSON).build();
    }
}
