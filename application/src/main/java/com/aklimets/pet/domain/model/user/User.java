package com.aklimets.pet.domain.model.user;

import com.aklimets.pet.buildingblock.interfaces.UsernameAndIdentity;
import com.aklimets.pet.domain.model.user.attribute.Role;
import com.aklimets.pet.domain.model.user.attribute.UserIdNumber;
import com.aklimets.pet.model.attribute.EmailAddress;
import com.aklimets.pet.model.attribute.Password;
import com.aklimets.pet.model.attribute.RefreshToken;
import com.aklimets.pet.model.attribute.Username;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.*;

/**
 * Aggregate root
 */
@Getter
@Entity
@Table(name = "users", schema = "security")
@AllArgsConstructor
public class User implements UsernameAndIdentity {

    @EmbeddedId
    private UserIdNumber id;

    @AttributeOverride(name = "value", column = @Column(name = "username"))
    private Username username;

    @AttributeOverride(name = "value", column = @Column(name = "email"))
    private EmailAddress email;

    @AttributeOverride(name = "value", column = @Column(name = "password"))
    private Password password;

    @AttributeOverride(name = "value", column = @Column(name = "refresh_token"))
    private RefreshToken refreshToken;

    @Enumerated(EnumType.STRING)
    private Role role;

    protected User() {
    }

    public void updateRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }
}
