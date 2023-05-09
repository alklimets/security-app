package com.fm.music.model.response.wrapper;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@ApiModel(value = "ResponsePayload", description = "Payload response model")
public class ResponsePayload<T> {

    private T data;
    private LocalDateTime occurredAt;

    public static <T>ResponsePayload<T> of(T data) {
       return new ResponsePayload<>(data, LocalDateTime.now());
    }
}
