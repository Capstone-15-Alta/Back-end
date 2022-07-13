package com.capstone15.alterra.domain.dto;

import com.capstone15.alterra.domain.dao.CategoryDao;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ThreadDtoResponse2 implements Serializable {


    private static final long serialVersionUID = 1532541802460113432L;
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

    private Long userId;

    private List<CommentDto> comments;

    private List<ThreadFollowerDto> followers;

    private List<ThreadLikeDto> likes;

    private List<SaveThreadDto> save;

    private List<ThreadReportDto> reports;

    private Integer thread_followers;

    private Integer thread_likes;

    private Integer totalComments;

    private ThreadViewDto view;

}
