package com.amazon.atlas.resources;

import com.amazon.atlas.data.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/tasks")
@Produces(MediaType.APPLICATION_JSON)
public class TasksResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(TasksResource.class);

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") String taskId) {

        LOGGER.info("fetch task request for - id = {%s}", taskId);

        return Response.ok("", MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Task newTask) {

        LOGGER.info("create task request for {%s}", newTask.getId());

        return Response.ok("", MediaType.APPLICATION_JSON).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") String taskId, Task taskToUpdate) {

        return Response.ok("", MediaType.APPLICATION_JSON).build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") String taskId) {

        LOGGER.info("delete Task request for {%s}", taskId);

        return Response.ok("", MediaType.APPLICATION_JSON).build();
    }
}
