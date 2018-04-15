package com.amazon.atlas.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/tasks")
@Produces(MediaType.APPLICATION_JSON)
public class TasksResource {

    @GET
    @Path("{id}")
    public Response getUser(@PathParam("id") String taskId) {

        return Response.ok("", MediaType.APPLICATION_JSON).build();
    }
}
