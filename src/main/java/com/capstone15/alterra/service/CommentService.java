package com.capstone15.alterra.service;

import com.capstone15.alterra.constant.AppConstant;
import com.capstone15.alterra.domain.dao.CommentDao;
import com.capstone15.alterra.domain.dao.ThreadDao;
import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dto.CommentDto;
import com.capstone15.alterra.domain.dto.ThreadDto;
import com.capstone15.alterra.domain.dto.ThreadDtoResponse;
import com.capstone15.alterra.repository.CommentRepository;
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
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ThreadRepository threadRepository;

    @Autowired
    private ModelMapper mapper;

    public ResponseEntity<Object> addComment(CommentDto request, UserDao user) {
        log.info("Executing add comment with request: {}", request);
        try{
            log.info("Get thread by id: {}", request.getThreadId());
            Optional<ThreadDao> threadDao = threadRepository.findById(request.getThreadId());
            if (threadDao.isEmpty()) {
                log.info("thread [{}] not found", request.getThreadId());
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }

            CommentDao commentDao = mapper.map(request, CommentDao.class);
            commentDao.setCreatedBy(user.getUsername());
            commentDao.setUser(UserDao.builder().id(user.getId()).build());
            commentDao.setThread(threadDao.get());
            commentDao = commentRepository.save(commentDao);
            log.info("Executing add comment success");
            return ResponseUtil.build(AppConstant.Message.SUCCESS, mapper.map(commentDao, CommentDto.class), HttpStatus.OK);

        } catch (Exception e) {
            log.error("Happened error when add comment. Error: {}", e.getMessage());
            log.trace("Get error when add comment. ", e);
            throw e;   }
    }

    public ResponseEntity<Object> getCommentByIdThread(Long id) {
        log.info("Executing get all comment.");
        try{
            Optional<ThreadDao> threadDaoOptional = threadRepository.findById(id);
            if(threadDaoOptional.isEmpty()) {
                log.info("thread id: {} not found", id);
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            List<CommentDao> daoList = threadDaoOptional.get().getComments();
            List<CommentDto> list = new ArrayList<>();
            for(CommentDao dao : daoList){
                list.add(mapper.map(dao, CommentDto.class));
            }
            return ResponseUtil.build(AppConstant.Message.SUCCESS, list, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when get all comment. Error: {}", e.getMessage());
            log.trace("Get error when get all comment. ", e);
            throw e;
        }  }

    public ResponseEntity<Object> getCommentByIdUser(Long id) {
        log.info("Executing get all comment.");
        try{
            Optional<UserDao> userDaoOptional = userRepository.findById(id);
            if(userDaoOptional.isEmpty()) {
                log.info("user id: {} not found", id);
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            List<CommentDao> daoList = userDaoOptional.get().getComments();
            List<CommentDto> list = new ArrayList<>();
            for(CommentDao dao : daoList){
                list.add(mapper.map(dao, CommentDto.class));
            }
            return ResponseUtil.build(AppConstant.Message.SUCCESS, list, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when get all comment. Error: {}", e.getMessage());
            log.trace("Get error when get all comment. ", e);
            throw e;
        }  }

    public ResponseEntity<Object> deleteComment(Long id, UserDao user) {
        log.info("Executing delete comment id: {}", id);
        try{
            Optional<CommentDao> commentDaoOptional = commentRepository.findById(id);
            if(commentDaoOptional.isEmpty()) {
                log.info("comment {} not found", id);
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            if(!commentDaoOptional.get().getUser().getId().equals(user.getId()) && user.getRoles().equals("USER")) {
                log.info("comment {} cant delete by user {}", id, user.getUsername());
                return ResponseUtil.build(AppConstant.Message.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            commentRepository.deleteById(id);
            log.info("Executing delete comment success");
            return ResponseUtil.build(AppConstant.Message.SUCCESS, null, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when delete comment. Error: {}", e.getMessage());
            log.trace("Get error when delete comment. ", e);
            throw e;
        }
    }

    public ResponseEntity<Object> updateComment(Long id, CommentDto request, UserDao user) {
        log.info("Executing update comment with request: {}", request);
        try {
            Optional<CommentDao> commentDaoOptional = commentRepository.findById(id);
            if(commentDaoOptional.isEmpty()) {
                log.info("comment {} not found", id);
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            if(!commentDaoOptional.get().getUser().getId().equals(user.getId()) && user.getRoles().equals("USER")) {
                log.info("comment {} cant update by user {}", id, user.getUsername());
                return ResponseUtil.build(AppConstant.Message.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            commentDaoOptional.ifPresent(res -> {
                res.setComment(request.getComment());
                commentRepository.save(res);
            });
            log.info("Executing update comment success");
            return ResponseUtil.build(AppConstant.Message.SUCCESS, mapper.map(commentDaoOptional, CommentDto.class), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when update comment. Error: {}", e.getMessage());
            log.trace("Get error when update comment. ", e);
            throw e;
        }
    }

}
