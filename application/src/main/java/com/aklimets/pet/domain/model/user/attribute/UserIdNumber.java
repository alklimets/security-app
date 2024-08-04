package com.aklimets.pet.domain.model.user.attribute;

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
public class UserIdNumber implements DomainAttribute<String> {

    @Column(name = "id")
    @NotNull
    private String value;

    protected UserIdNumber() {
    }

    public static UserIdNumber generate() {
        return new UserIdNumber(UUID.randomUUID().toString());
    }

}