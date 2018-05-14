package com.amazon.atlas.test;

import com.amazon.atlas.AtlasServiceApplication;
import com.amazon.atlas.AtlasServiceConfiguration;
import com.amazon.atlas.data.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.eclipse.jetty.http.HttpStatus;
import org.joda.time.DateTime;
import org.junit.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.contentOf;

public class UsersTest {
    private static final String CONFIG_PATH = ResourceHelpers.resourceFilePath("atlas-test.yml");
    public static final String RESOURCE_PATH="/users/";

    @ClassRule
    public static final DropwizardAppRule<AtlasServiceConfiguration> RULE = new DropwizardAppRule<>(
            AtlasServiceApplication.class, CONFIG_PATH);

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
    public void createTestUser() throws Exception {

        User testUser = new User("test@test.com", "super_secret", true);
        String json = new ObjectMapper().writeValueAsString(testUser);

        Response r = client.target("http://localhost:" + RULE.getLocalPort() + RESOURCE_PATH)
                .request()
                .post(Entity.entity(testUser, MediaType.APPLICATION_JSON_TYPE));

        User u = r.readEntity(User.class);
    }

    @Test
    public void testPostAndGetUser() throws Exception {
        final User newUser = createUser("test user@test.com", "password", true);
        final User fetchedUser = fetchUser(newUser.getId());

        assertThat(fetchedUser.getId()).isEqualTo(newUser.getId());
        assertThat(fetchedUser.getPassword()).isEqualTo(newUser.getPassword());
        assertThat(fetchedUser.getEmail()).isEqualTo(newUser.getEmail());
        assertThat(fetchedUser.getIsAdmin()).isEqualTo(newUser.getIsAdmin());
    }

    private User createUser(String email, String password, boolean isAdmin) throws Exception {

        final User user = new User(email, password, isAdmin);
        final User newUser = client.target("http://localhost:" + RULE.getLocalPort() + RESOURCE_PATH)
                .request()
                .post(Entity.entity(user, MediaType.APPLICATION_JSON_TYPE))
                .readEntity(User.class);

        assertThat(newUser.getId()).isNotNull();
        assertThat(newUser.getId()).isNotEqualTo("");

        return newUser;
    }

    private User fetchUser(String userId) throws Exception {

        final User fetchedUser = client.target("http://localhost:" + RULE.getLocalPort() + RESOURCE_PATH + userId)
                .request()
                .get()
                .readEntity(User.class);

        return fetchedUser;
    }
}