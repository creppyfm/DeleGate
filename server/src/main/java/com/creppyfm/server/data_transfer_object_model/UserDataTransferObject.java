package com.creppyfm.server.data_transfer_object_model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDataTransferObject {
    private String firstName;
    private String lastName;
    private String email;
}
