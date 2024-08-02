package com.aklimets.pet.application.service.authentication;

import com.aklimets.pet.application.util.PasswordEncoder;
import com.aklimets.pet.application.util.jwt.JwtUtil;
import com.aklimets.pet.domain.dto.request.UserRequestDTO;
import com.aklimets.pet.domain.dto.response.AuthenticationTokensDTO;
import com.aklimets.pet.domain.model.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationAppServiceHelper {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    boolean isPasswordsEqual(UserRequestDTO requestDTO, User existingUser) {
        var requested = passwordEncoder.encode(requestDTO.password());
        var actual = existingUser.getPassword();
        return requested.equals(actual);
    }

    AuthenticationTokensDTO generateUserTokens(User user) {
        return new AuthenticationTokensDTO(jwtUtil.generateAccessToken(user), jwtUtil.generateRefreshToken(user));
    }
}
