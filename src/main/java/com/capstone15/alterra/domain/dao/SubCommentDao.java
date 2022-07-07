package com.capstone15.alterra.domain.dao;


import com.capstone15.alterra.domain.common.BaseResponse;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Table(name = "M_SUB_COMMENT")
public class SubCommentDao extends BaseResponse {

    private static final long serialVersionUID = -5858334697377224130L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private CommentDao comment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserDao user;

    @Column(name = "sub_comment", nullable = false)
    private String subComment;

}
