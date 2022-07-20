package com.capstone15.alterra.repository;

import com.capstone15.alterra.domain.dao.ThreadFollowerDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ThreadFollowerRepository extends JpaRepository<ThreadFollowerDao, Long> {

    @Query(value = "SELECT tf FROM ThreadFollowerDao tf WHERE tf.user.id = :userid AND tf.thread.id = :threadid ")
    Optional<ThreadFollowerDao> findByUserIdAndThreadId(@Param("userid") Long userid, @Param("threadid") Long threadid);

    @Query(value = "SELECT count(tf) FROM ThreadFollowerDao tf WHERE tf.isFollow = true AND tf.thread.id = :id ")
    Integer countFollowers(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM ThreadFollowerDao tf WHERE tf.user.id = :id")
    void deleteAll(@Param("id") Long id);

}
