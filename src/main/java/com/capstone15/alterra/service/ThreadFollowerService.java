package com.capstone15.alterra.service;

import com.capstone15.alterra.constant.AppConstant;
import com.capstone15.alterra.domain.dao.CommentDao;
import com.capstone15.alterra.domain.dao.ThreadDao;
import com.capstone15.alterra.domain.dao.ThreadFollowerDao;
import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dto.CommentDto;
import com.capstone15.alterra.domain.dto.ThreadDto;
import com.capstone15.alterra.domain.dto.ThreadDtoResponse;
import com.capstone15.alterra.domain.dto.ThreadFollowerDto;
import com.capstone15.alterra.repository.ThreadFollowerRepository;
import com.capstone15.alterra.repository.ThreadRepository;
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
import java.util.Optional;

@Slf4j
@Service
public class ThreadFollowerService {

    @Autowired
    private ThreadFollowerRepository threadFollowerRepository;

    @Autowired
    private ThreadRepository threadRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    public ResponseEntity<Object> followThread(Long id, UserDao user) {
        log.info("Executing follow thread with request: {}", id);
        try{
            log.info("Get thread by id: {}", id);
            Optional<ThreadDao> threadDao = threadRepository.findById(id);
            if (threadDao.isEmpty()) {
                log.info("thread [{}] not found", id);
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            Optional<ThreadFollowerDao> threadFollowerDaoOptional = threadFollowerRepository.findByUserIdAndThreadId(user.getId(), id);
            if (threadFollowerDaoOptional.isEmpty()) {
//                ThreadFollowerDao threadFollowerDao = mapper.map(request, ThreadFollowerDao.class);
//                threadFollowerDao.setUser(UserDao.builder().id(user.getId()).build());
//                threadFollowerDao.setThread(threadDao.get());
//                threadFollowerDao.setFollowers(1);
                ThreadFollowerDao threadFollowerDao = ThreadFollowerDao.builder()
                        .thread(threadDao.get())
                        .user(UserDao.builder().id(user.getId()).build())
                        .isFollow(true)
                        .build();
                threadFollowerDao = threadFollowerRepository.save(threadFollowerDao);
                log.info("Executing follow thread success");
                threadDao.ifPresent(res -> {
                    res.setThread_followers(threadFollowerRepository.countFollowers(threadDao.get().getId()));
                    threadRepository.save(res);
                });
                return ResponseUtil.build(AppConstant.Message.SUCCESS, mapper.map(threadFollowerDao, ThreadFollowerDto.class), HttpStatus.OK);
            } else {
                if (threadFollowerDaoOptional.get().getIsFollow().equals(false)) {
                    threadFollowerDaoOptional.get().setIsFollow(true);
                    threadFollowerRepository.save(threadFollowerDaoOptional.get());
                    log.info("Executing follow thread success");
                    threadDao.ifPresent(res -> {
                        res.setThread_followers(threadFollowerRepository.countFollowers(threadDao.get().getId()));
                        threadRepository.save(res);
                    });
                } else {
                    threadFollowerDaoOptional.get().setIsFollow(false);
                    threadFollowerRepository.save(threadFollowerDaoOptional.get());
                    log.info("Executing unfollow thread success");
                    threadDao.ifPresent(res -> {
                        res.setThread_followers(threadFollowerRepository.countFollowers(threadDao.get().getId()));
                        threadRepository.save(res);
                    });
                }


                return ResponseUtil.build(AppConstant.Message.SUCCESS, mapper.map(threadFollowerDaoOptional, ThreadFollowerDto.class), HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error("Happened error when follow thread. Error: {}", e.getMessage());
            log.trace("Get error when follow thread. ", e);
            throw e;   }
    }

    public ResponseEntity<Object> getFollowerByIdThread(Long id) {
        log.info("Executing get follower by id thread.");
        try{
            Optional<ThreadDao> threadDaoOptional = threadRepository.findById(id);
            if(threadDaoOptional.isEmpty()) {
                log.info("thread id: {} not found", id);
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            List<ThreadFollowerDao> daoList = threadDaoOptional.get().getThreadFollowers();
            List<ThreadFollowerDto> list = new ArrayList<>();
            for(ThreadFollowerDao dao : daoList){
                list.add(mapper.map(dao, ThreadFollowerDto.class));
            }
            return ResponseUtil.build(AppConstant.Message.SUCCESS, list, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when get follower by id thread. Error: {}", e.getMessage());
            log.trace("Get error when get follower by id thread. ", e);
            throw e;
        }  }


}
