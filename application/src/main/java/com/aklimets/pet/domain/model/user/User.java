package com.aklimets.pet.domain.model.user;

import com.aklimets.pet.domain.model.user.attribute.AccountStatus;
import com.aklimets.pet.domain.model.user.attribute.UserIdNumber;
import com.aklimets.pet.jwt.model.JwtClaims;
import com.aklimets.pet.jwt.model.attribute.RefreshToken;
import com.aklimets.pet.jwt.model.attribute.Role;
import com.aklimets.pet.model.attribute.EmailAddress;
import com.aklimets.pet.model.attribute.Password;
import com.aklimets.pet.model.attribute.Username;
import lombok.AllArgsConstructor;
import lombok.Getter;

import jakarta.persistence.*;

import static com.aklimets.pet.domain.model.user.attribute.AccountStatus.ACTIVE;

/**
 * Aggregate root
 */
@Getter
@Entity
@Table(name = "users", schema = "security")
@AllArgsConstructor
public class User implements JwtClaims {

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

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    protected User() {
    }

    public void updateRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void confirmProfile() {
        this.status = ACTIVE;
    }

    public void changePassword(Password password) {
        this.password = password;
    }
}
