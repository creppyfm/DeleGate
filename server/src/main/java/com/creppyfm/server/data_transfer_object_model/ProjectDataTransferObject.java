package com.creppyfm.server.data_transfer_object_model;

import lombok.Data;

import java.util.List;

@Data
public class ProjectDataTransferObject {
    private String title;
    private String description;
    private List<StepDataTransferObject> steps;
}
