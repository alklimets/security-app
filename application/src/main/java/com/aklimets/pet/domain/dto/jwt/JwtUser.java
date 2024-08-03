package com.aklimets.pet.domain.dto.jwt;

import java.util.Date;


public record JwtUser (String id, String username, Date expiredAt){

}
