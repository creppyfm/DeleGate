package com.creppyfm.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document(collection = "Step")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Step {
    @Id
    private String id;
    private String projectId;
    private String title;
    private String description;
    private List<String> taskList;

    public List<String> getTaskList() {
        return Objects.requireNonNullElseGet(taskList, ArrayList::new);
    }
}
