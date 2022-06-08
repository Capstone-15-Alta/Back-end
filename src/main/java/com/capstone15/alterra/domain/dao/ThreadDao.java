package com.capstone15.alterra.domain.dao;

import com.capstone15.alterra.domain.common.BaseResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Table(name = "M_THREAD")
@SQLDelete(sql = "UPDATE M_THREAD SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
public class ThreadDao extends BaseResponse {

    private static final long serialVersionUID = -8666354185103895747L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(columnDefinition="TEXT", name = "description", nullable = false)
    private String description;

    @Column(name = "image")
    private String image;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserDao user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryDao category;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "thread")
    private List<CommentDao> comments;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "thread")
    private List<ThreadFollowerDao> threadFollowers;

    @Column(name = "followers")
    private Integer thread_followers = 0;


}
