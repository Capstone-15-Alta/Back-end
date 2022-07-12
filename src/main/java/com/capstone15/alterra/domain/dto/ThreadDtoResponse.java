package com.capstone15.alterra.domain.dto;

import com.capstone15.alterra.domain.dao.CategoryDao;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ThreadDtoResponse implements Serializable {


    private static final long serialVersionUID = -6648996209848744895L;
    private Long id;

    private String title;

    private String description;

    private String image;

    private CategoryDao category;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime updatedAt;

    private String createdBy;

    private UserDto user;

    private List<CommentDto> comments;

    private List<ThreadFollowerDto> followers;

    private List<ThreadLikeDto> likes;

    private List<SaveThreadDtoResponse> save;

    private List<ThreadReportDto> reports;

    private Integer thread_followers;

    private Integer thread_likes;

    private ThreadViewDto view;




}
