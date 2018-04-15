package com.amazon.atlas.resources;

import com.amazon.atlas.data.User;
import com.amazon.atlas.exceptions.NotFoundException;
import com.amazon.atlas.exceptions.ObjectExistsException;
import com.amazon.atlas.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UsersResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(UsersResource.class);
    UserService mInstance = UserService.instance();

    @GET
    @Path("{id}")
    public Response getUser(@PathParam("id") String userId) {
        LOGGER.info("fetch user request for {%s}", userId);

        try {
            Optional<User> foundUser = mInstance.find(userId);
            if(!foundUser.isPresent()) {
                LOGGER.info("user id {s} - not found", userId);
                return Response.status(Response.Status.NOT_FOUND).build();

            }
            LOGGER.info("Found user {s}; returning it", userId);
            return Response.ok(foundUser.get(), MediaType.APPLICATION_JSON).build();
        } catch(Exception ex) {
           LOGGER.error("Caught unexpected error", ex);
           return Response.serverError().build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(User newUser) {
        LOGGER.info("create user request for {%s}", newUser.getEmail());

        User user = null;
        try {
            user = mInstance.create(newUser.getId(), newUser.getEmail(), newUser.getPassword(),
                    newUser.getIsAdmin());
        } catch(ObjectExistsException ex) {
            LOGGER.error("User {s} already exists", newUser.getId());
            Response.status(Response.Status.BAD_REQUEST).build();
        } catch(Exception ex) {
            LOGGER.error("Unexpected server error", ex);
            Response.serverError().build();
        }
        LOGGER.info("User {s} created!", newUser.getId());
        return Response.ok(user, MediaType.APPLICATION_JSON).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("id") String userId, User user) {

        LOGGER.info("update user request for {%s}", userId);

        try {
            mInstance.update(userId, user.getPassword(), user.getIsAdmin());
        } catch(NotFoundException ex) {
            LOGGER.error("User {s} not found", userId);
            Response.status(Response.Status.NOT_FOUND).build();
        } catch(Exception ex) {
            LOGGER.error("Unexpected server error");
            Response.serverError().build();
        }

        LOGGER.info("returning updated user {s}", userId);
        return getUser(userId);
    }

    @DELETE
    @Path("{id}")
    public Response deleteUser(@PathParam("id") String userId) {

        LOGGER.info("delete user request for {%s}", userId);

        try {

            mInstance.delete(userId);

        }catch(NotFoundException ex) {
            LOGGER.error("User {s} not deleted; not found", userId);
            Response.status(Response.Status.NOT_FOUND).build();
        }catch(Exception ex) {
            LOGGER.error("Unexpected server error");
            Response.serverError().build();
        }

        LOGGER.info("User {s} removed", userId);
        return Response.ok("", MediaType.APPLICATION_JSON).build();
    }
}
