package com.capstone15.alterra.service;

import com.capstone15.alterra.constant.AppConstant;
import com.capstone15.alterra.domain.dao.NotificationDao;
import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dao.UserFollowerDao;
import com.capstone15.alterra.domain.dto.UserFollowerDto;
import com.capstone15.alterra.repository.NotificationRepository;
import com.capstone15.alterra.repository.UserFollowerRepository;
import com.capstone15.alterra.repository.UserRepository;
import com.capstone15.alterra.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class UserFollowerService {

    @Autowired
    private UserFollowerRepository userFollowerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ModelMapper mapper;

    public ResponseEntity<Object> followUser(Long id, UserDao user) {
        log.info("Executing follow user with request: {}", id);
        try{
            log.info("Get user by id: {}", id);
            Optional<UserDao> userDao = userRepository.findById(id);
            if (userDao.isEmpty()) {
                log.info("user [{}] not found", id);
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            if(user.getId().equals(id)){
                log.info("user [{}] cant follow yourself ", id);
                return ResponseUtil.build(AppConstant.Message.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            Optional<UserFollowerDao> userFollowerDaoOptional = userFollowerRepository.findByUserFollowerIdAndUserFollowedId(user.getId(), id);
            if (userFollowerDaoOptional.isEmpty()) {
                UserFollowerDao userFollowerDao = UserFollowerDao.builder()
                        .userFollowed(userDao.get())
                        .userFollower(UserDao.builder().id(user.getId()).build())
                        .isFollow(true)
                        .build();
                userFollowerDao = userFollowerRepository.save(userFollowerDao);
                log.info("Executing follow user success");
                userDao.ifPresent(res -> {
                    res.setTotalUserFollowers(userFollowerRepository.countFollowers(userDao.get().getId()));
                    userRepository.save(res);
                });
                Optional<UserDao> userDaoOptional = userRepository.findById(user.getId());
                Objects.requireNonNull(userDaoOptional.orElse(null)).setTotalUserFollowing(userFollowerRepository.countFollowing(user.getId()));
                userRepository.save(userDaoOptional.get());

                // fitur notification
                NotificationDao notificationDao = NotificationDao.builder()
                        .user(UserDao.builder().id(id).build())
                        .title(user.getUsername() + " mulai mengikuti anda.")
                        .followerId(user.getId())
                        .isRead(false)
                        .build();
                notificationDao = notificationRepository.save(notificationDao);

                return ResponseUtil.build(AppConstant.Message.SUCCESS, mapper.map(userFollowerDao, UserFollowerDto.class), HttpStatus.OK);
            } else {
                if (userFollowerDaoOptional.get().getIsFollow().equals(false)) {
                    userFollowerDaoOptional.get().setIsFollow(true);
                    userFollowerRepository.save(userFollowerDaoOptional.get());
                    log.info("Executing follow user success");
                    userDao.ifPresent(res -> {
                        res.setTotalUserFollowers(userFollowerRepository.countFollowers(userDao.get().getId()));
                        userRepository.save(res);
                    });
                    Optional<UserDao> userDaoOptional = userRepository.findById(user.getId());
                    Objects.requireNonNull(userDaoOptional.orElse(null)).setTotalUserFollowing(userFollowerRepository.countFollowing(user.getId()));
                    userRepository.save(userDaoOptional.get());

                    Optional<NotificationDao> notification = notificationRepository.findByUserIdAndFollowerId(id, user.getId());
                    Objects.requireNonNull(notification.orElse(null)).setIsRead(false);
                    notificationRepository.save(notification.get());
                } else {
                    userFollowerDaoOptional.get().setIsFollow(false);
                    userFollowerRepository.save(userFollowerDaoOptional.get());
                    log.info("Executing unfollow user success");
                    userDao.ifPresent(res -> {
                        res.setTotalUserFollowers(userFollowerRepository.countFollowers(userDao.get().getId()));
                        userRepository.save(res);
                    });
                    Optional<UserDao> userDaoOptional = userRepository.findById(user.getId());
                    Objects.requireNonNull(userDaoOptional.orElse(null)).setTotalUserFollowing(userFollowerRepository.countFollowing(user.getId()));
                    userRepository.save(userDaoOptional.get());

                    Optional<NotificationDao> notification = notificationRepository.findByUserIdAndFollowerId(id, user.getId());
                    if(notification.isPresent()) {
                        Objects.requireNonNull(notification.orElse(null)).setIsRead(true);
                        notificationRepository.save(notification.get());
                    }

                }


                return ResponseUtil.build(AppConstant.Message.SUCCESS, mapper.map(userFollowerDaoOptional, UserFollowerDto.class), HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error("Happened error when follow user. Error: {}", e.getMessage());
            log.trace("Get error when follow user. ", e);
            throw e;   }
    }

    public ResponseEntity<Object> getFollowerByIdUser(Long id) {
        log.info("Executing get follower by id user.");
        try{
            Optional<UserDao> userDaoOptional = userRepository.findById(id);
            if(userDaoOptional.isEmpty()) {
                log.info("user id: {} not found", id);
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            List<UserFollowerDao> daoList = userDaoOptional.get().getUserFollowers();
            List<UserFollowerDto> list = new ArrayList<>();
            for(UserFollowerDao dao : daoList){
                if(dao.getIsFollow().equals(true))
                {
                    list.add(mapper.map(dao, UserFollowerDto.class));
                }

            }
            return ResponseUtil.build(AppConstant.Message.SUCCESS, list, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when get follower by id user. Error: {}", e.getMessage());
            log.trace("Get error when get follower by id user. ", e);
            throw e;
        }  }

}
