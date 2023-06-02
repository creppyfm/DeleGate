package com.creppyfm.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMembers {
    private String id;
    private String role;
    private String firstName;
    private String lastName;
    private List<String> currentTasks;
}
