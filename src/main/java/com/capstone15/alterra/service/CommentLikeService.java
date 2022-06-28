package com.capstone15.alterra.service;


import com.capstone15.alterra.constant.AppConstant;
import com.capstone15.alterra.domain.dao.*;
import com.capstone15.alterra.domain.dto.ThreadLikeDto;
import com.capstone15.alterra.domain.dto.CommentLikeDto;
import com.capstone15.alterra.repository.*;
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
public class CommentLikeService {

    @Autowired
    private CommentLikeRepository commentLikeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ModelMapper mapper;

    public ResponseEntity<Object> likeComment(Long id, UserDao user) {
            log.info("Executing like comment with request: {}", id);
        try {
            log.info("Get comment by id: {}", id);
            //Optional<UserDao> userDao = userRepository.findById(request.getUserId());
            Optional<CommentDao> commentDao = commentRepository.findById(id);
            if (commentDao.isEmpty()) {
                log.info("comment [{}] not found", id);
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);

            }
            Optional<CommentLikeDao> optionalCommentLikeDao = commentLikeRepository.findByUserIdAndCommentId(user.getId(), id);
            if (optionalCommentLikeDao.isEmpty()) {
                CommentLikeDao commentLikeDao = CommentLikeDao.builder()
                        .comment(commentDao.get())
                        .user(UserDao.builder().id(user.getId()).build())
                        .isLike(true)
                        .build();
                commentLikeDao = commentLikeRepository.save(commentLikeDao);
                log.info("Executing like comment success");
                commentDao.ifPresent(res -> {
                    res.setComment_likes(commentLikeRepository.countLikes(commentDao.get().getId()));
                    commentRepository.save(res); //lamda expression
                });
                // fitur notification
                NotificationDao notificationDao = NotificationDao.builder()
                        .user(UserDao.builder().id(commentDao.get().getUser().getId()).build())
                        .title(user.getUsername() + " menyukai komentar anda: " + commentDao.get().getComment())
                        .commentId(commentDao.get().getId())
                        .info("likecomment")
                        .isRead(false)
                        .build();
                notificationDao = notificationRepository.save(notificationDao);
                return ResponseUtil.build(AppConstant.Message.SUCCESS, mapper.map(commentLikeDao, CommentLikeDto.class), HttpStatus.OK);
            } else {
                if (optionalCommentLikeDao.get().getIsLike().equals(false)) {
                    optionalCommentLikeDao.get().setIsLike(true);
                    commentLikeRepository.save(optionalCommentLikeDao.get());
                    log.info("Executing like comment success");
                    commentDao.ifPresent(res -> {
                        res.setComment_likes(commentLikeRepository.countLikes(commentDao.get().getId()));
                        commentRepository.save(res);
                    });
                    Optional<NotificationDao> notification = notificationRepository.findByUserIdAndCommentIdAndInfo(commentDao.get().getUser().getId(), commentDao.get().getId(), "likecomment");
                    Objects.requireNonNull(notification.orElse(null)).setIsRead(false);
                    notificationRepository.save(notification.get());
                } else {
                    optionalCommentLikeDao.get().setIsLike(false);
                    commentLikeRepository.save(optionalCommentLikeDao.get());
                    log.info("Executing unlike comment success");
                    commentDao.ifPresent(res -> {
                        res.setComment_likes(commentLikeRepository.countLikes(commentDao.get().getId()));
                        commentRepository.save(res);
                    });
                    Optional<NotificationDao> notification = notificationRepository.findByUserIdAndCommentIdAndInfo(commentDao.get().getUser().getId(), commentDao.get().getId(), "likecomment");
                    if(notification.isPresent()) {
                        Objects.requireNonNull(notification.orElse(null)).setIsRead(true);
                        notificationRepository.save(notification.get());
                    }

                }
                return ResponseUtil.build(AppConstant.Message.SUCCESS, mapper.map(optionalCommentLikeDao, CommentLikeDto.class), HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error("Happened error when like comment. Error: {}", e.getMessage());
            log.trace("Get error when like comment. ", e);
            throw e;
        }
    }

    public ResponseEntity<Object> getLikeByIdComment(Long id) {
        log.info("Executing get like by id comment.");
        try {
            Optional<CommentDao> commentDaoOptional = commentRepository.findById(id);
            if (commentDaoOptional.isEmpty()) {
                log.info("comment id: {} not found", id);
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            List<CommentLikeDao> daoList = commentDaoOptional.get().getCommentLikeDaoList();
            List<CommentLikeDto> list = new ArrayList<>();
            for (CommentLikeDao dao : daoList) {
                if(dao.getIsLike().equals(true)){
                    list.add(mapper.map(dao, CommentLikeDto.class));
                }

            }
            return ResponseUtil.build(AppConstant.Message.SUCCESS, list, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when get like by id comment. Error: {}", e.getMessage());
            log.trace("Get error when get like by id comment. ", e);
            throw e;
        }
    }

}

