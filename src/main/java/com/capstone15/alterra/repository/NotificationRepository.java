package com.capstone15.alterra.repository;

import com.capstone15.alterra.domain.dao.NotificationDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationDao, Long> {

    @Query(value = "SELECT tf FROM NotificationDao tf WHERE tf.isRead = false AND tf.user.id = :id ORDER BY tf.id DESC")
    List<NotificationDao> getNotification(@Param("id") Long id);

}
