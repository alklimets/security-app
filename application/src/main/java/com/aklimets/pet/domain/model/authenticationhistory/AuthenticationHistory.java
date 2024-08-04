package com.aklimets.pet.domain.model.authenticationhistory;

import com.aklimets.pet.domain.model.authenticationhistory.attribute.AuthenticationHistoryIdNumber;
import com.aklimets.pet.domain.model.authenticationhistory.attribute.AuthenticationTimestamp;
import com.aklimets.pet.domain.model.authenticationhistory.attribute.IpAddress;
import com.aklimets.pet.domain.model.user.attribute.UserIdNumber;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "authentication_history", schema = "security")
@AllArgsConstructor
public class AuthenticationHistory {

    @EmbeddedId
    private AuthenticationHistoryIdNumber id;

    @AttributeOverride(name = "value", column = @Column(name = "user_id"))
    private UserIdNumber userId;

    private IpAddress ipAddress;

    private AuthenticationTimestamp timestamp;

    protected AuthenticationHistory() {
    }
}
