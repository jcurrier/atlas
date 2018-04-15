package com.amazon.atlas.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.UUID;

public class Project {
    private String mId;
    private String mName;
    private String mDescription;
    private String mOwner;
    private Date mCreatedOn;
    private Date mLastUpdated;

    @JsonProperty
    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = mId;
    }

    @JsonProperty
    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    @JsonProperty
    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    @JsonProperty
    public String getOwner() {
        return mOwner;
    }

    public void setOwner(String owner) {
        this.mOwner = owner;
    }

    @JsonProperty
    public Date getCreatedOn() {
        return mCreatedOn;
    }

    public void setCreatedOwn(Date createdOn) {
        this.mCreatedOn = createdOn;
    }

    @JsonProperty
    public Date getLastUpdated() {
        return mLastUpdated;
    }

    public void setLastUpdated(Date lastUpdate) {
        this.mLastUpdated = lastUpdate;
    }

    public Project() {

    }

    public Project(String name, String description, String owner) {
        this(UUID.randomUUID().toString(), name, description, owner, new Date(), new Date());
    }

    public Project(String id, String name, String description, String owner, Date createdOn, Date lastUpdated) {
        this.mId = id;
        this.mName = name;
        this.mDescription = description;
        this.mOwner = owner;
        this.mCreatedOn = createdOn;
        this.mLastUpdated = lastUpdated;
    }
}
