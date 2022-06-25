package com.capstone15.alterra.service;

import com.capstone15.alterra.constant.AppConstant;
import com.capstone15.alterra.domain.dao.ThreadDao;
import com.capstone15.alterra.domain.dao.ThreadFollowerDao;
import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dao.UserFollowerDao;
import com.capstone15.alterra.domain.dto.ThreadFollowerDto;
import com.capstone15.alterra.domain.dto.UserFollowerDto;
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
                list.add(mapper.map(dao, UserFollowerDto.class));
            }
            return ResponseUtil.build(AppConstant.Message.SUCCESS, list, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when get follower by id user. Error: {}", e.getMessage());
            log.trace("Get error when get follower by id user. ", e);
            throw e;
        }  }

}
