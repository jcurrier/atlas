package com.amazon.atlas.test;

import com.amazon.atlas.data.User;
import com.amazon.atlas.exceptions.NotFoundException;
import com.amazon.atlas.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.UUID;


public class UserServiceTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testCreateFindDeleteUser() throws Exception {
        String userId = UUID.randomUUID().toString();
        String emailAddr = "someemail@somewhere.com";
        String pw = "testme";
        boolean isAdmin = false;

        UserService svc = UserService.instance();

        User user = svc.create(userId, emailAddr, pw, isAdmin);

        Optional<User> foundUser = svc.find(userId);
        Assert.assertTrue(foundUser.isPresent());

        svc.delete(userId);

        foundUser = svc.find(userId);
        Assert.assertFalse(foundUser.isPresent());

        try {
            svc.delete(userId);
        }catch(NotFoundException ex) {
            // Do nothing. Expected.
        }
    }

    @Test
    public void testSerializeDeserializeUser() {
        User newUser = new User("user_id", "jeff@jeff.com", false);

        try {
            ObjectMapper mapper = new ObjectMapper();
            String userJson = mapper.writeValueAsString(newUser);

            User deserilizedUser = mapper.readValue(userJson, User.class);

            Assert.assertTrue(newUser.getId().compareTo(deserilizedUser.getId()) == 0);
            Assert.assertTrue(newUser.getEmail().compareTo(deserilizedUser.getEmail()) == 0);
            Assert.assertTrue(newUser.getPassword().compareTo(deserilizedUser.getPassword()) == 0);

            //BUG: Date deserialization is not functioning.
            int result = newUser.getLastUpdated().compareTo(deserilizedUser.getLastUpdated());

            Assert.assertEquals(newUser.getIsAdmin(), deserilizedUser.getIsAdmin());

        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }
}
