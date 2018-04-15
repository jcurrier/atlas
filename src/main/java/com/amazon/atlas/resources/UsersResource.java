package com.amazon.atlas.resources;

import com.amazon.atlas.data.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UsersResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(UsersResource.class);

    @GET
    @Path("{id}")
    public Response getUser(@PathParam("id") String userId) {
        LOGGER.info("fetch user request - id = {%s}", userId);

        return Response.ok("", MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(User newUser) {

        LOGGER.info("create user request for {%s}", newUser.getEmail());

        return Response.ok("", MediaType.APPLICATION_JSON).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("id") String userId, User user) {

        LOGGER.info("update user request for {%s}", userId);

        User updatedUser = null;

        return Response.ok("", MediaType.APPLICATION_JSON).build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteUser(@PathParam("id") String userId) {

        LOGGER.info("delete user request for {%s}", userId);

        return Response.ok("", MediaType.APPLICATION_JSON).build();
    }
}
