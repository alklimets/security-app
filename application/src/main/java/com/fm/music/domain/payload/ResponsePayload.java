package com.fm.music.domain.payload;

import io.swagger.annotations.ApiModel;

import java.time.LocalDateTime;

@ApiModel(value = "ResponsePayload", description = "Payload response model") // describes API model in swagger
public record ResponsePayload<T> (T data, LocalDateTime occurredAt){

    public static <T>ResponsePayload<T> of(T data) {
       return new ResponsePayload<>(data, LocalDateTime.now());
    }
}
