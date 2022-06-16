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
    private ModelMapper mapper;

    public ResponseEntity<Object> likeComment(CommentLikeDto request) {
//        log.info("Executing like thread with request: {}", id);
        try {
//            log.info("Get thread by id: {}", id);
            Optional<UserDao> userDao = userRepository.findById(request.getUserId());
            Optional<CommentDao> commentDao = commentRepository.findById(request.getCommentId());
            if (commentDao.isEmpty()) {
                log.info("comment [{}] not found", request.getCommentId());
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);

            }
            Optional<CommentLikeDao> optionalCommentLikeDao = commentLikeRepository.findByUserIdAndCommentId(request.getUserId(), request.getCommentId());
            if (optionalCommentLikeDao.isEmpty()) {
                CommentLikeDao commentLikeDao = CommentLikeDao.builder()
                        .comment(CommentDao.builder().id(request.getCommentId()).build())
                        .user(UserDao.builder().id(request.getUserId()).build())
                        .likes(1)
                        .build();
                commentLikeDao = commentLikeRepository.save(commentLikeDao);
                log.info("Executing like thread success");
                commentDao.ifPresent(res -> {
                    res.setComment_likes(commentLikeRepository.countLikes(commentDao.get().getId()));
                    commentRepository.save(res); //lamda expression
                });
                return ResponseUtil.build(AppConstant.Message.SUCCESS, mapper.map(commentLikeDao, CommentLikeDto.class), HttpStatus.OK);
            } else {
                if (optionalCommentLikeDao.get().getLikes().equals(0)) {
                    optionalCommentLikeDao.get().setLikes(1);
                    commentLikeRepository.save(optionalCommentLikeDao.get());
                    log.info("Executing like thread success");
                    commentDao.ifPresent(res -> {
                        res.setComment_likes(commentLikeRepository.countLikes(commentDao.get().getId()));
                        commentRepository.save(res);
                    });
                } else {
                    optionalCommentLikeDao.get().setLikes(0);
                    commentLikeRepository.save(optionalCommentLikeDao.get());
                    log.info("Executing unlike thread success");
                    commentDao.ifPresent(res -> {
                        res.setComment_likes(commentLikeRepository.countLikes(commentDao.get().getId()));
                        commentRepository.save(res);
                    });
                }
                return ResponseUtil.build(AppConstant.Message.SUCCESS, mapper.map(optionalCommentLikeDao, CommentLikeDto.class), HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error("Happened error when like thread. Error: {}", e.getMessage());
            log.trace("Get error when like thread. ", e);
            throw e;
        }
    }

    public ResponseEntity<Object> getLikeByIdComment(Long id) {
        log.info("Executing get like by id thread.");
        try {
            Optional<CommentDao> commentDaoOptional = commentRepository.findById(id);
            if (commentDaoOptional.isEmpty()) {
                log.info("comment id: {} not found", id);
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            List<CommentLikeDao> daoList = commentDaoOptional.get().getCommentLikeDaoList();
            List<CommentLikeDto> list = new ArrayList<>();
            for (CommentLikeDao dao : daoList) {
                list.add(mapper.map(dao, CommentLikeDto.class));
            }
            return ResponseUtil.build(AppConstant.Message.SUCCESS, list, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when get like by id thread. Error: {}", e.getMessage());
            log.trace("Get error when get like by id thread. ", e);
            throw e;
        }
    }

}

