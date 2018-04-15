package com.amazon.atlas.service;

import com.amazon.atlas.data.User;

public class UserService {

    private static UserService mInstance = null;

    public static synchronized  UserService instance() {
        if(mInstance == null) {
            mInstance = new UserService();
        }

        return mInstance;
    }

    public User createUser(String emailAddress, String password, boolean isAdmin) {
        assert emailAddress != null;
        assert password != null;

        User newUser = new User(emailAddress, password, isAdmin);

        return newUser;
    }

    public boolean changePassword(User user, String oldPassword, String newPassword) {
        return true;
    }

    public User find(String emailAddress) {
        assert emailAddress != null;

        return null;
    }

    public boolean delete(User user) {
       assert user != null;

       return true;
    }

    private UserService() { }
}
