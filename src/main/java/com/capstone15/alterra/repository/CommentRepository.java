package com.capstone15.alterra.repository;

import com.capstone15.alterra.domain.dao.CommentDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentDao, Long> {

    @Query(value = "SELECT count(tf) FROM CommentDao tf WHERE tf.isDeleted = false AND tf.user.id = :id ")
    Integer countComments(@Param("id") Long id);

}
