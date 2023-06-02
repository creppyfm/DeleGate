package com.creppyfm.server.data_transfer_object_model;

import com.creppyfm.server.enumerated.Phase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponse {
    private String projectId;
    private String title;
    private String description;
    private Phase phase;
    private String updated;
}
