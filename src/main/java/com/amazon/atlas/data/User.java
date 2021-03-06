package com.amazon.atlas.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.UUID;

public class User {
    private String mId;
    private String mEmail;
    private String mPassword;
    private Date mLastUpdated;
    private boolean mIsAdmin = false;

    @JsonProperty("Id")
    public String getId() {
        return mId;
    }

    @JsonProperty("Id")
    public void setId(String id) {
        this.mId = id;
    }

    @JsonProperty("EmailAddress")
    public String getEmail() {
        return mEmail;
    }

    @JsonProperty("EmailAddress")
    public void setEmail(String email) {
        this.mEmail = email;
    }

    @JsonProperty("Password")
    public String getPassword() {
        return mPassword;
    }

    @JsonProperty("Password")
    public void setPassword(String password) {
        this.mPassword = password;
    }

    @JsonProperty("LastUpdated")
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "dd-MM-yyyy hh:mm:ss")
    public Date getLastUpdated() {
        return mLastUpdated;
    }

    @JsonProperty("LastUpdated")
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "dd-MM-yyyy hh:mm:ss")
    public void setLastUpdated(Date lastUpdated) {
        this.mLastUpdated = lastUpdated;
    }

    @JsonProperty("IsAdmin")
    public boolean getIsAdmin() {
        return mIsAdmin;
    }

    @JsonProperty("IsAdmin")
    public void setIsAdmin(boolean isAdmin) {
        this.mIsAdmin = isAdmin;
    }

    public User() { };

    public User(String email, String password, boolean isAdmin) {
        this(UUID.randomUUID().toString(), email, password, new Date(), isAdmin);
    }

    public User(String id, String email, String password, Date lastUpdated, boolean isAdmin) {
        this.mId = id;
        this.mEmail = email;
        this.mPassword = password;
        this.mLastUpdated = lastUpdated;
        this.mIsAdmin = isAdmin;
    }
}
