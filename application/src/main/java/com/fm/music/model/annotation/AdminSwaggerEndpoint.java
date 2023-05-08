package com.fm.music.model.annotation;

import com.fm.music.model.response.ErrorResponsePayload;
import com.fm.music.model.response.ResponsePayload;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ApiResponses(
        value = {
                @ApiResponse(responseCode = "200", description = "OK",
                        content = {@Content(schema = @Schema(implementation = ResponsePayload.class))}),
                @ApiResponse(responseCode = "400", description = "Bad request",
                        content = {@Content(schema = @Schema(implementation = ErrorResponsePayload.class))}),
                @ApiResponse(responseCode = "401", description = "Unauthorized",
                        content = {@Content(schema = @Schema(implementation = ErrorResponsePayload.class))}),
                @ApiResponse(responseCode = "403", description = "Access denied",
                        content = {@Content(schema = @Schema(implementation = ErrorResponsePayload.class))}),
                @ApiResponse(responseCode = "404", description = "Not found",
                        content = {@Content(schema = @Schema(implementation = ErrorResponsePayload.class))}),
                @ApiResponse(responseCode = "500", description = "Internal server error",
                        content = {@Content(schema = @Schema(implementation = ErrorResponsePayload.class))})
        }
)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AdminSwaggerEndpoint {
}
