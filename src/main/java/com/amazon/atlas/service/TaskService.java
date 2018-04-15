package com.amazon.atlas.service;

import com.amazon.atlas.data.Project;
import com.amazon.atlas.data.Sprint;
import com.amazon.atlas.data.Task;
import com.amazon.atlas.data.User;

import java.util.List;

public class TaskService {

    private static TaskService mInstance = null;

    public static synchronized TaskService TaskService() {
        if(mInstance == null) {
            mInstance = new TaskService();
        }

        return mInstance;
    }

    public Task create(String title, String description, User assignee, User creator, Project project, Sprint sprint) {
        assert title != null;
        assert description != null;
        assert assignee != null;
        assert creator != null;
        assert project != null;
        assert sprint != null;

        Task newTask = new Task(title, description, assignee, creator, project, sprint);


        return newTask;
    }

    public List<Task> findTasks(Project project, Sprint sprint) {
        assert project != null;
        assert sprint != null;

        return null;
    }

    public boolean update(Task task) {
        assert task != null;

        return true;
    }

    public boolean delete(Task task) {
        assert task != null;

        return true;
    }

    private TaskService() { }
}
