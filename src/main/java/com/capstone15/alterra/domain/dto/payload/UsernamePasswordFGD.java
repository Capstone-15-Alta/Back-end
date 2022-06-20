package com.capstone15.alterra.domain.dto.payload;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UsernamePasswordFGD implements Serializable {

    private static final long serialVersionUID = 2487045866600038881L;

    private String username;
    private String password;
    private String email;
    private Integer totalUserFollowers = 0;
}
