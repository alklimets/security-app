package com.aklimets.pet.domain.model.authenticationhistory.attribute;

import com.aklimets.pet.buildingblock.interfaces.DomainAttribute;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.util.UUID;


@Embeddable
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class AuthenticationHistoryIdNumber implements DomainAttribute<String> {

    @Column(name = "id")
    @NotNull
    private String value;

    protected AuthenticationHistoryIdNumber() {
    }

    public static AuthenticationHistoryIdNumber generate() {
        return new AuthenticationHistoryIdNumber(UUID.randomUUID().toString());
    }
}




