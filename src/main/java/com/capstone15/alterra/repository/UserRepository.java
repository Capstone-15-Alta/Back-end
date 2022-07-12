package com.capstone15.alterra.repository;

import com.capstone15.alterra.domain.dao.UserDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserDao, Long> {
    UserDao getDistinctTopByUsername(String username);

    @Query("SELECT c FROM UserDao c WHERE c.email = ?1")
    public UserDao findByEmail(String email);

    @Query("SELECT c FROM UserDao c WHERE c.email = ?1")
    Optional<UserDao> findByEmails(String email);

    @Query("SELECT c FROM UserDao c WHERE c.username = ?1")
    Optional<UserDao> findByUsername(String username);

    public UserDao findByResetPasswordToken(String token);

    @Query(value = "SELECT t FROM UserDao t ORDER BY t.totalUserFollowers DESC")
    Page<UserDao> findAllUserByRanking(Pageable pageable);

   // List<UserDao> findByOrderByThreads_SizeDescThreads_Thread_likesDesc();

    Page<UserDao> findByOrderByTotalThreadsDesc(Pageable pageable);

    @Query(value = "SELECT u FROM UserDao u ORDER BY u.totalThreads DESC, u.totalLikeThread DESC ")
    Page<UserDao> findUserRanking(Pageable pageable);




}
