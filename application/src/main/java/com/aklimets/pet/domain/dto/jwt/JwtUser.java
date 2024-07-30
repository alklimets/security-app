package com.aklimets.pet.domain.dto.jwt;

import java.util.Date;


public record JwtUser (String username, Date expiredAt){

}
