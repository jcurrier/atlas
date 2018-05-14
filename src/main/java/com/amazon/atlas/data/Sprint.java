package com.amazon.atlas.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Sprint {
    private String mId;
    private String mProjectId;
    private String mName;
    private Date mStartDate;
    private Date mEndDate;
    private Date mCreatedOn;
    private Date mLastUpdated;

    @JsonProperty
    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    @JsonProperty
    public String getProjectId() {
        return mProjectId;
    }

    public void setProjectId(String projectId) {
        this.mProjectId = projectId;
    }

    @JsonProperty
    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    @JsonProperty
    public Date getStartDate() {
        return mStartDate;
    }

    public void setStartDate(Date startDate) {
        this.mStartDate = startDate;
    }

    @JsonProperty
    public Date getEndDate() {
        return mEndDate;
    }

    public void setEndDate(Date endDate) {
        this.mEndDate = endDate;
    }

    @JsonProperty
    public Date getCreatedOn() {
        return mCreatedOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.mCreatedOn = createdOn;
    }

    @JsonProperty
    public Date getLastUpdated() {
        return mLastUpdated;
    }

    public void setLastUpdated(Date lastUpdate) {
        this.mLastUpdated = lastUpdate;
    }

    public Sprint() {

    }

    public Sprint(String name, Project project, Date startDate, Date endDate) {
       this(UUID.randomUUID().toString(), project, name, startDate, endDate, new Date(), new Date());
    }

    public Sprint(String id, Project project, String name, Date startDate, Date endDate,
        Date createdOn, Date lastUpdated) {
        this.mId = id;
        this.mProjectId = project.getId();
        this.mName = name;
        this.mStartDate = startDate;
        this.mEndDate = endDate;
        this.mCreatedOn = createdOn;
        this.mLastUpdated = lastUpdated;
    }
}

