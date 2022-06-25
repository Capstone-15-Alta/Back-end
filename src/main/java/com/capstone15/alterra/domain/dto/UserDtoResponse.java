package com.capstone15.alterra.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;
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

    private String firstName;

    private String lastName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date birthDate;

    private String education;

    private String gender;

    private String country;

    private String city;

    private String zipCode;

    private String image;

    private List<ThreadDto> threads;

    private List<ThreadFollowerDto> threadFollowers;

    private List<ThreadLikeDto> threadLikes;

    private List<CommentDto> comments;

    private List<CommentLikeDto> commentLikes;

    private List<UserFollowerDto> userFollowers;

    private Integer totalUserFollowers;

    private Integer totalUserFollowing;

    private Integer totalThreads;

    private Integer totalPostComments;


}
