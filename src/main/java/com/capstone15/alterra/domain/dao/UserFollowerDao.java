package com.capstone15.alterra.domain.dao;

import com.capstone15.alterra.domain.common.BaseResponseFollower;
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
@Table(name = "M_USER_FOLLOWER")
@Where(clause = "is_follow = true")
public class UserFollowerDao extends BaseResponseFollower {


    private static final long serialVersionUID = -489565817356180556L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_followed_id")
    private UserDao userFollowed;

    @ManyToOne
    @JoinColumn(name = "user_follower_id")
    private UserDao userFollower;

    @Column(name = "is_follow")
    private Boolean isFollow = false;
}
