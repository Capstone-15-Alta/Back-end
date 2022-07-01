package com.capstone15.alterra.domain.dto;

import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dao.UserFollowerDao;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserFollowerDtoResponse implements Serializable {

    private static final long serialVersionUID = 270556033119557186L;

    private UserDao userFollower;

    private UserDao userFollowed;

    private Boolean isFollow;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime followTime;
}
