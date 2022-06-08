package com.capstone15.alterra.domain.dao;

import com.capstone15.alterra.domain.common.BaseResponseFollower;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Table(name = "M_THREAD_FOLLOWER")
@Where(clause = "followers = 1")
public class ThreadFollowerDao extends BaseResponseFollower {

    private static final long serialVersionUID = -8707777249211253146L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "thread_id")
    private ThreadDao thread;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserDao user;

    @Column(name = "followers")
    private Integer followers = 0;


}
