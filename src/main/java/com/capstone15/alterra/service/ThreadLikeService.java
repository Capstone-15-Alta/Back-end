package com.capstone15.alterra.service;

import com.capstone15.alterra.constant.AppConstant;
import com.capstone15.alterra.domain.dao.NotificationDao;
import com.capstone15.alterra.domain.dao.ThreadDao;
import com.capstone15.alterra.domain.dao.ThreadLikeDao;
import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dto.ThreadLikeDto;
import com.capstone15.alterra.repository.NotificationRepository;
import com.capstone15.alterra.repository.ThreadLikeRepository;
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
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class ThreadLikeService {

    @Autowired
    private ThreadLikeRepository threadLikeRepository;

    @Autowired
    private ThreadRepository threadRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

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
                log.info("Executing new like thread success");
                threadDao.ifPresent(res -> {
                    res.setThread_likes(threadLikeRepository.countLikes(threadDao.get().getId()));
                    threadRepository.save(res);
                });
                // fitur notification
                if(!user.getId().equals(threadDao.get().getUser().getId())){
                    NotificationDao notificationDao = NotificationDao.builder()
                            .user(UserDao.builder().id(threadDao.get().getUser().getId()).build())
                            .title(user.getUsername() + " menyukai thread anda: " + threadDao.get().getTitle())
                            .threadId(threadDao.get().getId())
                            .info("likethread")
                            .isRead(false)
                            .build();
                    notificationDao = notificationRepository.save(notificationDao);
                }

                // total like thread
                Optional<UserDao> userDao = userRepository.findById(user.getId());
                Objects.requireNonNull(userDao.orElse(null)).setTotalLikeThread(threadLikeRepository.countLikeThread(user.getId()));
                userRepository.save(userDao.get());



                return ResponseUtil.build(AppConstant.Message.SUCCESS, mapper.map(threadLikeDao, ThreadLikeDto.class), HttpStatus.OK);
            } else {
                if (threadLikeDaoOptional.get().getIsLike().equals(false)) {
                    threadLikeDaoOptional.get().setIsLike(true);
                    threadLikeRepository.save(threadLikeDaoOptional.get());
                    log.info("Executing if like thread success");
                    threadDao.ifPresent(res -> {
                        res.setThread_likes(threadLikeRepository.countLikes(threadDao.get().getId()));
                        threadRepository.save(res);
                    });
                    Optional<NotificationDao> notification = notificationRepository.findByUserIdAndThreadIdAndInfo(threadDao.get().getUser().getId(), threadDao.get().getId(), "likethread");
                   Objects.requireNonNull(notification.orElse(null)).setIsRead(false);
                    notificationRepository.save(notification.get());

                    Optional<UserDao> userDao = userRepository.findById(user.getId());
                    Objects.requireNonNull(userDao.orElse(null)).setTotalLikeThread(threadLikeRepository.countLikeThread(user.getId()));
                    userRepository.save(userDao.get());

                } else {
                    threadLikeDaoOptional.get().setIsLike(false);
                    threadLikeRepository.save(threadLikeDaoOptional.get());
                    log.info("Executing unlike thread success");
                    threadDao.ifPresent(res -> {
                        res.setThread_likes(threadLikeRepository.countLikes(threadDao.get().getId()));
                        threadRepository.save(res);
                    });

                    Optional<NotificationDao> notification = notificationRepository.findByUserIdAndThreadIdAndInfo(threadDao.get().getUser().getId(), threadDao.get().getId(), "likethread");
                    if(notification.isPresent()) {
                        Objects.requireNonNull(notification.orElse(null)).setIsRead(true);
                        notificationRepository.save(notification.get());
                    }

                    Optional<UserDao> userDao = userRepository.findById(user.getId());
                    Objects.requireNonNull(userDao.orElse(null)).setTotalLikeThread(threadLikeRepository.countLikeThread(user.getId()));
                    userRepository.save(userDao.get());

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
                if(dao.getIsLike().equals(true)){
                    list.add(mapper.map(dao, ThreadLikeDto.class));
                }
            }
            return ResponseUtil.build(AppConstant.Message.SUCCESS, list, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when get like by id thread. Error: {}", e.getMessage());
            log.trace("Get error when get like by id thread. ", e);
            throw e;
        }  }


//    public ResponseEntity<Object> getAllThreadWithPaginate(Pageable pageable) {
//        log.info("Executing get thread with paginate: {}", pageable);
//        try{
//            Page<ThreadLikeDao> threadLikeDaoPage = threadLikeRepository.findAllthreadlike(pageable);
//
//            List<ThreadLikeDto> list = new ArrayList<>();
//            for(ThreadLikeDao dao : threadLikeDaoPage){
//                list.add(mapper.map(dao, ThreadLikeDto.class));
//            }
//            return ResponseUtil.build(AppConstant.Message.SUCCESS, list, HttpStatus.OK);
//        } catch (Exception e) {
//            log.error("Happened error when get thread with paginate . Error: {}", e.getMessage());
//            log.trace("Get error when get thread with paginate. ", e);
//            throw e;
//        }
//
//    }
}
