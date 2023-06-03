package com.creppyfm.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMembers {
    private String id;
    private String role;
    private String firstName;
    private String lastName;
    private List<String> currentTasks;

    public List<String> getCurrentTasks() {
        return Objects.requireNonNullElseGet(currentTasks, ArrayList::new);
    }

}
