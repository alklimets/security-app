package com.fm.music.model.dto;

import com.fm.music.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String id;
    private String username;
    private String role;

    public static UserDTO fromEntity(User user) {
        return new UserDTO(user.getId(),
                user.getUsername(),
                user.getRole().name());
    }
}
