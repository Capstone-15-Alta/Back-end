package com.capstone15.alterra.domain.dao;

import com.capstone15.alterra.domain.common.BaseResponse;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Table(name = "M_NOTIFICATION")
@Where(clause = "is_read = false")
public class NotificationDao extends BaseResponse {


    private static final long serialVersionUID = -1707882019098659784L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserDao user;

    @Column(name = "title")
    private String title;

    @Column(name = "message")
    private String message;

    @Column(name = "thread_id")
    private Long threadId;

    @Column(name = "comment_id")
    private Long commentId;

    @Column(name = "follower_id")
    private Long followerId;

    @Column(name = "info")
    private String info;

    @Column(name = "is_read")
    private Boolean isRead = false;



}
