package com.amazon.atlas.service;

import com.amazon.atlas.data.Project;
import com.amazon.atlas.data.User;

import java.util.List;
import java.util.Objects;

public class ProjectService {

    private static ProjectService mInstance = null;

    public synchronized static ProjectService instance() {

       if(mInstance == null) {
           mInstance = new ProjectService();
       }

       return mInstance;
    }

    public Project create(String name, String description, User owner) {
        assert name != null;
        assert owner != null;

        Project newProject = new Project(name, description, owner.getId());

        return newProject;
    }

    public Project find(String projectId) {
        assert(projectId != null);

        return null;
    }

    public List<Project> getProjectsByUser(User user) {
        assert user != null;

        return null;
    }

    public void delete(Project project) {
        assert project != null;

    }


    private ProjectService() { }
}
