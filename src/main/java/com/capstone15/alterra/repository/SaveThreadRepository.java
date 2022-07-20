package com.capstone15.alterra.repository;

import com.capstone15.alterra.domain.dao.SaveThreadDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface SaveThreadRepository extends JpaRepository<SaveThreadDao,Long> {

    @Query(value = "SELECT st FROM SaveThreadDao st WHERE st.user.id = :userid AND st.thread.id = :threadid ")
    Optional<SaveThreadDao> findByUserIdAndThreadId(@Param("userid") Long userid, @Param("threadid") Long threadid);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM SaveThreadDao tf WHERE tf.user.id = :id")
    void deleteAll(@Param("id") Long id);
}
