package com.capstone15.alterra.domain.dao;

import com.capstone15.alterra.domain.common.BaseResponseLike;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

@Data
@Builder //bisa lgsg create objek tanpa getter setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Table(name = "M_COMMENT_LIKE")
@Where(clause = "likes = 1")
public class CommentLikeDao extends BaseResponseLike implements Serializable {


    private static final long serialVersionUID = -850554511300929273L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private CommentDao comment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserDao user;

    @Column(name = "likes")
    private Integer likes = 0;


}

