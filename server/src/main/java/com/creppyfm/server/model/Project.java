package com.creppyfm.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "Project")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    @Id
    private String id;
    private String userId;
    private String title;
    private String description;
    private String phase; //ex: yet to begin, working on it, finished
    private LocalDateTime created;
    private LocalDateTime updated;
    private List<ProjectMembers> projectMembers = new ArrayList<>();
    @DocumentReference
    private List<Task> taskList; //to be deleted
    @DocumentReference
    private List<Step> stepList;

    //manual constructor for associating 'Project' with 'User'
    public Project(String userId, String title, String description, String phase, LocalDateTime created, LocalDateTime updated) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.phase = phase;
        this.created = created;
        this.updated = updated;
    }

    public String getId() {
        return this.id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    public List<ProjectMembers> getProjectMembers() {
        return projectMembers;
    }

    public void addProjectMember (ProjectMembers projectMember) {
        projectMembers.add(projectMember);
    }

    public void setProjectMembers(List<ProjectMembers> projectMembers) {
        this.projectMembers = projectMembers;
    }

    public boolean removeProjectMemberByUserId(String userId) {
        return projectMembers.removeIf(projectMember ->
            projectMember.getUserId().equals(userId)
        );
    }

    public boolean updateProjectMemberRoleByUserId(String userId, String newRole) {
        for (ProjectMembers projectMember : projectMembers) {
            if (projectMember.getUserId().equals(userId)) {
                projectMember.setRole(newRole);
                return true;
            }
        }
        return false;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void addTask(Task task) {
        taskList.add(task);
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    public List<Step> getStepList() {
        return stepList;
    }

    public void setStepList(List<Step> stepList) {
        this.stepList = stepList;
    }
}
