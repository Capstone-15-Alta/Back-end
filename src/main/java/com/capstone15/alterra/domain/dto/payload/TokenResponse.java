package com.capstone15.alterra.domain.dto.payload;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TokenResponse {
    private Long id;
    private String username;
    private String email;
    private String token;
}