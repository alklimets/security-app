package com.aklimets.pet.adapter.controller.advice;

import com.aklimets.pet.buildingblock.interfaces.ResponseData;
import com.aklimets.pet.model.envelope.MetaInformation;
import com.aklimets.pet.model.envelope.ResponseEnvelope;
import com.aklimets.pet.util.datetime.TimeSource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
@AllArgsConstructor
public class CustomResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private TimeSource timeSource;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return ResponseData.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        MetaInformation metaInformation = new MetaInformation(
                getUrl(),
                request.getMethodValue(),
                getRequestId(),
                timeSource.getCurrentLocalDateTime()
        );
        return new ResponseEnvelope<>(body, metaInformation);
    }

    private String getUrl() {
        var attrs =  (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            HttpServletRequest req = attrs.getRequest();
            return req.getRequestURI();
        }
        return null;
    }

    private String getRequestId() {
        var attrs =  (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            HttpServletRequest req = attrs.getRequest();
            return req.getHeader("X-Request-ID");
        }
        return null;
    }
}
