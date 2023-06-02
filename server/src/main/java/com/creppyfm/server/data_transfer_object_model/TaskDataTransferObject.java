package com.creppyfm.server.data_transfer_object_model;

import com.creppyfm.server.enumerated.Phase;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskDataTransferObject {
    private String id;
    private String title;
    private Phase phase;
}
