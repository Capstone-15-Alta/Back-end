package com.capstone15.alterra.repository;

import com.capstone15.alterra.domain.dao.UserFollowerDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserFollowerRepository extends JpaRepository <UserFollowerDao, Long> {

    @Query(value = "SELECT tf FROM UserFollowerDao tf WHERE tf.userFollower.id = :userfollowerid AND tf.userFollowed.id = :userfollowedid ")
    Optional<UserFollowerDao> findByUserFollowerIdAndUserFollowedId(@Param("userfollowerid") Long userfollowerid, @Param("userfollowedid") Long userfollowedid);

    @Query(value = "SELECT count(tf) FROM UserFollowerDao tf WHERE tf.isFollow = true AND tf.userFollowed.id = :id ")
    Integer countFollowers(@Param("id") Long id);

    @Query(value = "SELECT count(tf) FROM UserFollowerDao tf WHERE tf.isFollow = true AND tf.userFollower.id = :id ")
    Integer countFollowing(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM UserFollowerDao tf WHERE tf.userFollower.id = :id")
    void deleteAll(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM UserFollowerDao tf WHERE tf.userFollowed.id = :id")
    void deleteAll2(@Param("id") Long id);

}
