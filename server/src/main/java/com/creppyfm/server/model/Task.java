package com.creppyfm.server.model;

import com.creppyfm.server.enumerated.Phase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "Task")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    private String id;
    private String stepId;
    private String title;
    private String description;
    private int weight;
    private Phase phase;
    private LocalDateTime created;
    private LocalDateTime updated;
    private List<String> assignedUsers = new ArrayList<>();


    //manual constructor for associating 'Task' with 'Project'
    public Task(String stepId, String title, String description, int weight, Phase phase, LocalDateTime created, LocalDateTime updated) {
        this.stepId = stepId;
        this.title = title;
        this.description = description;
        this.weight = weight;
        this.phase = phase;
        this.created = created;
        this.updated = updated;
    }

    public String getId() {
        return this.id;
    }

    public int getWeight() {
        return weight;
    }

    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    public void setWeight(int weight) {
        this.weight = weight;
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

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
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

    public List<String> getAssignedUsers() {
        return assignedUsers;
    }

    public void setAssignedUsers(List<String> assignedUsers) {
        this.assignedUsers = assignedUsers;
    }

}
