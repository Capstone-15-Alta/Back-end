package com.capstone15.alterra.repository;

import com.capstone15.alterra.domain.dao.SaveThreadDao;
import com.capstone15.alterra.domain.dao.ThreadLikeDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SaveThreadRepository extends JpaRepository<SaveThreadDao,Long> {

    @Query(value = "SELECT st FROM SaveThreadDao st WHERE st.user.id = :userid AND st.thread.id = :threadid ")
    Optional<SaveThreadDao> findByUserIdAndThreadId(@Param("userid") Long userid, @Param("threadid") Long threadid);

}
