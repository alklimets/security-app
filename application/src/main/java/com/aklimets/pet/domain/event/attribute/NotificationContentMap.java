package com.aklimets.pet.domain.event.attribute;

import com.aklimets.pet.buildingblock.interfaces.DomainAttribute;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import jakarta.validation.constraints.NotNull;
import java.util.Map;

@Getter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class NotificationContentMap extends DomainAttribute<Map<String, String>> {

    @NotNull
    private Map<String, String> value;

}
