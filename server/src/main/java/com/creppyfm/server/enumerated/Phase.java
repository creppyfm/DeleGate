package com.creppyfm.server.enumerated;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public enum Phase {
    NOT_STARTED("Not Started"),
    IN_PROGRESS("In Progress"),
    IN_REVIEW("In Review"),
    COMPLETED("Completed");

    private String value;

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }
}
