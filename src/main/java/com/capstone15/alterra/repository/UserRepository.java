package com.capstone15.alterra.repository;

import com.capstone15.alterra.domain.dao.UserDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserDao, Long> {
    UserDao getDistinctTopByUsername(String username);

    @Query("SELECT c FROM UserDao c WHERE c.email = ?1")
    public UserDao findByEmail(String email);

    public UserDao findByResetPasswordToken(String token);

}
