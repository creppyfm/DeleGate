package com.creppyfm.server.model;

import com.creppyfm.server.enumerated.Provider;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document(collection = "User")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String id;
    private String token;
    private String firstName;
    private String lastName;
    private String email;
    private Provider provider;
    private List<String> projectIds;
    private List<String> strengths;
    @JsonBackReference
    private List<String> currentTasks = new ArrayList<>();

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public List<String> getProjectIds() {
        return projectIds;
    }

    public void setProjectIds(List<String> projectIds) {
        this.projectIds = projectIds;
    }

    public List<String> getStrengths() {
        return strengths;
    }

    public void setStrengths(List<String> strengths) {
        this.strengths = strengths;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getCurrentTasks() {
        return Objects.requireNonNullElseGet(currentTasks, ArrayList::new);
    }
    public void setCurrentTasks(List<String> currentTasks) {
        this.currentTasks = currentTasks;
    }

}
