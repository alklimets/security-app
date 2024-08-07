package com.aklimets.pet.application.service.authentication;

import com.aklimets.pet.application.util.PasswordEncoder;
import com.aklimets.pet.domain.dto.request.AuthenticationRequest;
import com.aklimets.pet.domain.dto.response.AuthenticationTokensResponse;
import com.aklimets.pet.domain.model.user.User;
import com.aklimets.pet.jwt.util.JwtGenerator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationAppServiceHelper {

    private final JwtGenerator jwtUtil;
    private final PasswordEncoder passwordEncoder;

    boolean isPasswordsEqual(AuthenticationRequest requestDTO, User existingUser) {
        var requested = passwordEncoder.encode(requestDTO.password());
        var actual = existingUser.getPassword();
        return requested.equals(actual);
    }

    AuthenticationTokensResponse generateUserTokens(User user) {
        return new AuthenticationTokensResponse(jwtUtil.generateAccessToken(user), jwtUtil.generateRefreshToken(user));
    }
}
