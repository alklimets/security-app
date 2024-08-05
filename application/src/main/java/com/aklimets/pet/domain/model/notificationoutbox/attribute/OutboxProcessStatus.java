package com.aklimets.pet.domain.model.notificationoutbox.attribute;

public enum OutboxProcessStatus {
    N("NEW"), P("PROCESSED"), F("FAILED");

    private String value;

    OutboxProcessStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
