package com.capstone15.alterra.service;

import com.capstone15.alterra.constant.AppConstant;
import com.capstone15.alterra.domain.dao.ThreadDao;
import com.capstone15.alterra.domain.dao.ThreadFollowerDao;
import com.capstone15.alterra.domain.dao.ThreadLikeDao;
import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dto.ThreadFollowerDto;
import com.capstone15.alterra.domain.dto.ThreadLikeDto;
import com.capstone15.alterra.repository.ThreadLikeRepository;
import com.capstone15.alterra.repository.ThreadRepository;
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
public class ThreadLikeService {

    @Autowired
    private ThreadLikeRepository threadLikeRepository;

    @Autowired
    private ThreadRepository threadRepository;

    @Autowired
    private ModelMapper mapper;

    public ResponseEntity<Object> likeThread(Long id, UserDao user) {
        log.info("Executing like thread with request: {}", id);
        try{
            log.info("Get thread by id: {}", id);
            Optional<ThreadDao> threadDao = threadRepository.findById(id);
            if (threadDao.isEmpty()) {
                log.info("thread [{}] not found", id);
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            Optional<ThreadLikeDao> threadLikeDaoOptional = threadLikeRepository.findByUserIdAndThreadId(user.getId(), id);
            if (threadLikeDaoOptional.isEmpty()) {
                ThreadLikeDao threadLikeDao = ThreadLikeDao.builder()
                        .thread(threadDao.get())
                        .user(UserDao.builder().id(user.getId()).build())
                        .isLike(true)
                        .build();
                threadLikeDao = threadLikeRepository.save(threadLikeDao);
                log.info("Executing like thread success");
                threadDao.ifPresent(res -> {
                    res.setThread_likes(threadLikeRepository.countLikes(threadDao.get().getId()));
                    threadRepository.save(res);
                });
                return ResponseUtil.build(AppConstant.Message.SUCCESS, mapper.map(threadLikeDao, ThreadLikeDto.class), HttpStatus.OK);
            } else {
                if (threadLikeDaoOptional.get().getIsLike().equals(false)) {
                    threadLikeDaoOptional.get().setIsLike(true);
                    threadLikeRepository.save(threadLikeDaoOptional.get());
                    log.info("Executing like thread success");
                    threadDao.ifPresent(res -> {
                        res.setThread_likes(threadLikeRepository.countLikes(threadDao.get().getId()));
                        threadRepository.save(res);
                    });
                } else {
                    threadLikeDaoOptional.get().setIsLike(false);
                    threadLikeRepository.save(threadLikeDaoOptional.get());
                    log.info("Executing unlike thread success");
                    threadDao.ifPresent(res -> {
                        res.setThread_likes(threadLikeRepository.countLikes(threadDao.get().getId()));
                        threadRepository.save(res);
                    });
                }
                return ResponseUtil.build(AppConstant.Message.SUCCESS, mapper.map(threadLikeDaoOptional, ThreadLikeDto.class), HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error("Happened error when like thread. Error: {}", e.getMessage());
            log.trace("Get error when like thread. ", e);
            throw e;   }
    }

    public ResponseEntity<Object> getLikeByIdThread(Long id) {
        log.info("Executing get like by id thread.");
        try{
            Optional<ThreadDao> threadDaoOptional = threadRepository.findById(id);
            if(threadDaoOptional.isEmpty()) {
                log.info("thread id: {} not found", id);
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            List<ThreadLikeDao> daoList = threadDaoOptional.get().getThreadLikes();
            List<ThreadLikeDto> list = new ArrayList<>();
            for(ThreadLikeDao dao : daoList){
                list.add(mapper.map(dao, ThreadLikeDto.class));
            }
            return ResponseUtil.build(AppConstant.Message.SUCCESS, list, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when get like by id thread. Error: {}", e.getMessage());
            log.trace("Get error when get like by id thread. ", e);
            throw e;
        }  }

}
