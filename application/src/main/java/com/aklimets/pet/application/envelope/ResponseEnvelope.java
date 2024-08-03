package com.aklimets.pet.application.envelope;

import io.swagger.annotations.ApiModel;

import java.time.LocalDateTime;

@ApiModel(value = "ResponseEnvelope", description = "Payload response envelope") // describes API model in swagger
public record ResponseEnvelope<T> (T data, LocalDateTime occurredAt){

    public static <T> ResponseEnvelope<T> of(T data) {
       return new ResponseEnvelope<>(data, LocalDateTime.now());
    }
}
