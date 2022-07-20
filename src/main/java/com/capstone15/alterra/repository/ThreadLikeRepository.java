package com.capstone15.alterra.repository;

import com.capstone15.alterra.domain.dao.ThreadLikeDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ThreadLikeRepository extends JpaRepository<ThreadLikeDao, Long> {

    @Query(value = "SELECT tf FROM ThreadLikeDao tf WHERE tf.user.id = :userid AND tf.thread.id = :threadid ")
    Optional<ThreadLikeDao> findByUserIdAndThreadId(@Param("userid") Long userid, @Param("threadid") Long threadid);

    @Query(value = "SELECT count(tf) FROM ThreadLikeDao tf WHERE tf.isLike = true AND tf.thread.id = :id")
    Integer countLikes(@Param("id") Long id);

   // Page<ThreadLikeDao> findAllthreadlike (Pageable pageable);

    @Query(value = "SELECT count(tf) FROM ThreadLikeDao tf WHERE tf.isLike = true AND tf.user.id = :id ")
    Integer countLikeThread(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM ThreadLikeDao tf WHERE tf.user.id = :id")
    void deleteAll(@Param("id") Long id);

}
