package com.aklimets.pet.application.util;

import com.aklimets.pet.domain.model.profileconfirmation.attribute.ConfirmationCode;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToConfirmationCodeConverter implements Converter<String, ConfirmationCode> {
    @Override
    public ConfirmationCode convert(String source) {
        return new ConfirmationCode(source);
    }
}
