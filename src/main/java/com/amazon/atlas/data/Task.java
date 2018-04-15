package com.amazon.atlas.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.UUID;

public class Task {
    private String mId;
    private String mTitle;
    private String mDescription;
    private String mAssignedTo;
    private String mCreatedBy;
    private Date mCreatedOn;
    private String mProjectId;
    private String mSprintId;
    private Date mLastUpdated;

    @JsonProperty
    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    @JsonProperty
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    @JsonProperty
    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = mDescription;
    }

    @JsonProperty
    public String getAssignedTo() {
        return mAssignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.mAssignedTo = assignedTo;
    }

    @JsonProperty
    public String getCreatedBy() {
        return mCreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        this.mCreatedBy = createdBy;
    }

    @JsonProperty
    public Date getCreatedOn() {
        return mCreatedOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.mCreatedOn = createdOn;
    }

    @JsonProperty
    public String getProjectId() {
        return mProjectId;
    }

    public void setProjectId(String projectId) {
        this.mProjectId = projectId;
    }

    @JsonProperty
    public String getSprintId() {
        return mSprintId;
    }

    public void setSprintId(String sprintId) {
        this.mSprintId = sprintId;
    }

    @JsonProperty
    public Date getLastUpdated() {
        return mLastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.mLastUpdated = lastUpdated;
    }

    public Task() { };

    public Task(String title, String description, User assignedTo, User creator, Project project, Sprint sprint)
    {
        this(UUID.randomUUID().toString(), title, description, assignedTo.getId(), creator.getId(), new Date(),
                project.getId(), sprint.getId(), new Date());
    }

    public Task(String title, String description, User assignedTo, String creator, Project project, Sprint sprint)
    {
        this(UUID.randomUUID().toString(), title, description, assignedTo.getId(), creator, new Date(), project.getId(),
                sprint.getId(), new Date());
    }

    public Task(String id, String title, String description, String assignedTo, String creator, Date createdOn,
                String projectId, String sprintId, Date lastUpdate)
    {
        this.mId = id;
        this.mTitle = title;
        this.mDescription = description;
        this.mAssignedTo = assignedTo;
        this.mCreatedBy = creator;
        this.mCreatedOn = createdOn;
        this.mProjectId = projectId;
        this .mSprintId = sprintId;
        this.mLastUpdated = lastUpdate;
    }
}
