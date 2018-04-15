package com.amazon.atlas.resources;

import com.amazon.atlas.data.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/projects")
@Produces(MediaType.APPLICATION_JSON)
public class ProjectsResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectsResource.class);

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") String projectId) {
        LOGGER.info("fetch project request - id = {s}", projectId);

        return Response.ok("", MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Project newProject) {

        LOGGER.info("create project request for {%s}", newProject.getId());

        return Response.ok("", MediaType.APPLICATION_JSON).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") String projectId, Project project) {

        LOGGER.info("update project request for {%s}", projectId);

        Project updatedProject = null;

        return Response.ok("", MediaType.APPLICATION_JSON).build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") String projectId) {

        LOGGER.info("delete project request for {%s}", projectId);

        return Response.ok("", MediaType.APPLICATION_JSON).build();
    }
}
