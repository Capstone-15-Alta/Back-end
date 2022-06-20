package com.capstone15.alterra.domain.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserDtoResponse implements Serializable {

    private static final long serialVersionUID = 4941849631522075637L;

    private Long id;

    private String username;

    private String phone;

    private String email;

    private String roles;

    private List<ThreadDto> threads;

    private List<ThreadFollowerDto> threadFollowers;

    private List<ThreadLikeDto> threadLikes;

    private List<CommentDto> comments;

    private List<CommentLikeDto> commentLikes;

    private List<UserFollowerDto> userFollowers;

    private Integer totalUserFollowers;


}
