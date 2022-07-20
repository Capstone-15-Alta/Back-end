package com.capstone15.alterra.repository;

import com.capstone15.alterra.domain.dao.CommentLikeDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLikeDao, Long> {

    @Query(value = "SELECT tf FROM CommentLikeDao tf WHERE tf.user.id = :userid AND tf.comment.id = :commentid ")
    Optional<CommentLikeDao> findByUserIdAndCommentId(@Param("userid") Long userid, @Param("commentid") Long commentid);


    @Query(value = "SELECT count(tf) FROM CommentLikeDao tf WHERE tf.isLike = true AND tf.comment.id = :id")
    Integer countLikes(@Param("id") Long id);

    @Query(value = "SELECT count(tf) FROM CommentLikeDao tf WHERE tf.isLike = true AND tf.user.id = :id ")
    Integer countLikeComment(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM CommentLikeDao tf WHERE tf.user.id = :id")
    void deleteAll(@Param("id") Long id);

}
