package com.capstone15.alterra.repository;

import com.capstone15.alterra.domain.dao.NotificationDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationDao, Long> {

    @Query(value = "SELECT tf FROM NotificationDao tf WHERE tf.isRead = false AND tf.user.id = :id ORDER BY tf.id DESC")
    Page<NotificationDao> getNotification(@Param("id") Long id, Pageable pageable);

    @Query(value = "SELECT tf FROM NotificationDao tf WHERE tf.isRead = false AND tf.user.id = :id ORDER BY tf.id DESC")
    List<NotificationDao> getNotificationList(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE NotificationDao tf SET tf.isRead = true WHERE tf.user.id = :id")
    void readAllNotification(@Param("id") Long id);

    @Query(value = "SELECT tf FROM NotificationDao tf WHERE tf.user.id = :userid AND tf.threadId = :threadid AND tf.info = :info ")
    Optional<NotificationDao> findByUserIdAndThreadIdAndInfo(@Param("userid") Long userid, @Param("threadid") Long threadid, @Param("info") String info);

    @Query(value = "SELECT tf FROM NotificationDao tf WHERE tf.user.id = :userid AND tf.commentId = :commentid AND tf.info = :info ")
    Optional<NotificationDao> findByUserIdAndCommentIdAndInfo(@Param("userid") Long userid, @Param("commentid") Long commentid, @Param("info") String info);

    @Query(value = "SELECT tf FROM NotificationDao tf WHERE tf.user.id = :userid AND tf.followerId = :followerid")
    Optional<NotificationDao> findByUserIdAndFollowerId(@Param("userid") Long userid, @Param("followerid") Long followerid);


}
