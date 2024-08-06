package com.aklimets.pet.application.util;

import com.aklimets.pet.model.attribute.Password;
import com.google.common.hash.Hashing;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class PasswordEncoder {

    public Password encode(Password original) {
        return new Password(Hashing.sha256()
                .hashString(original.getValue(), StandardCharsets.UTF_8)
                .toString());
    }
}
