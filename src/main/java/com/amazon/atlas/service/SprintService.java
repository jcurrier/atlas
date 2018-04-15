package com.amazon.atlas.service;

import com.amazon.atlas.data.Project;
import com.amazon.atlas.data.Sprint;

import java.util.Date;
import java.util.List;

public class SprintService {

    private static SprintService mInstance = null;

    public synchronized final static SprintService instance() {
        if(mInstance == null) {
            mInstance = new SprintService();
        }

        return mInstance;
    }

    public Sprint create(String name, Date startDate, Date endDate, Project project) {
        assert name != null;
        assert startDate != null;
        assert endDate != null;
        assert project != null;

        Sprint newSprint = new Sprint(name, project, startDate, endDate);

        return newSprint;
    }

    public Sprint find(String sprintId) {
        assert sprintId != null;

        return null;
    }

    public List<Sprint> getSprints(Project project) {
        assert project != null;

        return null;
    }

    public boolean update(Sprint sprint) {
        assert sprint != null;

        return true;
    }

    public boolean delete(Sprint sprint) {
        assert sprint != null;

        return true;
    }

    private SprintService() {};
}
