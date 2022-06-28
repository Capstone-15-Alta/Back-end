package com.capstone15.alterra.repository;

import com.capstone15.alterra.domain.dao.CommentLikeDao;
import com.capstone15.alterra.domain.dao.NotificationDao;
import com.capstone15.alterra.domain.dao.ThreadDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationDao, Long> {

    @Query(value = "SELECT tf FROM NotificationDao tf WHERE tf.isRead = false AND tf.user.id = :id ORDER BY tf.id DESC")
    List<NotificationDao> getNotification(@Param("id") Long id);

    @Query(value = "SELECT tf FROM NotificationDao tf WHERE tf.user.id = :userid AND tf.threadId = :threadid AND tf.info = :info ")
    Optional<NotificationDao> findByUserIdAndThreadIdAndInfo(@Param("userid") Long userid, @Param("threadid") Long threadid, @Param("info") String info);

    @Query(value = "SELECT tf FROM NotificationDao tf WHERE tf.user.id = :userid AND tf.commentId = :commentid AND tf.info = :info ")
    Optional<NotificationDao> findByUserIdAndCommentIdAndInfo(@Param("userid") Long userid, @Param("commentid") Long commentid, @Param("info") String info);

    @Query(value = "SELECT tf FROM NotificationDao tf WHERE tf.user.id = :userid AND tf.followerId = :followerid")
    Optional<NotificationDao> findByUserIdAndFollowerId(@Param("userid") Long userid, @Param("followerid") Long followerid);


}
