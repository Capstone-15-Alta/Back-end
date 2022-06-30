package com.capstone15.alterra.domain.dto;

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
public class UserDto implements Serializable {

    private static final long serialVersionUID = 2318082842538632891L;

    private Long id;

    private String username;

    private String password;

    private String email;

    private String phone;

    private String image;

    private String imageCover;

    private String roles;

    private Integer totalUserFollowers;

    private Integer totalUserFollowing;

    private Integer totalThreads;

    private Integer totalPostComments;

}
