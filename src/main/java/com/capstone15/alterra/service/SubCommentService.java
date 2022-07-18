package com.capstone15.alterra.service;

import com.capstone15.alterra.constant.AppConstant;
import com.capstone15.alterra.domain.dao.CommentDao;
import com.capstone15.alterra.domain.dao.NotificationDao;
import com.capstone15.alterra.domain.dao.SubCommentDao;
import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dto.SubCommentDto;
import com.capstone15.alterra.repository.CommentRepository;
import com.capstone15.alterra.repository.NotificationRepository;
import com.capstone15.alterra.repository.SubCommentRepository;
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
public class SubCommentService {

    @Autowired
    private SubCommentRepository subCommentRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ModelMapper mapper;

    public ResponseEntity<Object> addSubComment(SubCommentDto request, UserDao user) {
        log.info("Executing add comment with request: {}", request);
        try{
            log.info("Get comment by id: {}", request.getCommentId());
            Optional<CommentDao> commentDao = commentRepository.findById(request.getCommentId());
            if (commentDao.isEmpty()) {
                log.info("comment [{}] not found", request.getCommentId());
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }

            SubCommentDao subCommentDao = mapper.map(request, SubCommentDao.class);
            subCommentDao.setCreatedBy(user.getUsername());
            subCommentDao.setUser(UserDao.builder().id(user.getId()).build());
            subCommentDao.setComment(commentDao.get());
            subCommentDao = subCommentRepository.save(subCommentDao);


            //  notification
            if(!user.getId().equals(commentDao.get().getUser().getId())){
                NotificationDao notificationDao = NotificationDao.builder()
                        .user(UserDao.builder().id(commentDao.get().getUser().getId()).build())
                        .title(user.getUsername() + " membalas komentar anda.")
                        .commentId(commentDao.get().getId())
                        .message(request.getSubComment())
                        .isRead(false)
                        .build();
                notificationDao = notificationRepository.save(notificationDao);
            }


            log.info("Executing add comment success");
            return ResponseUtil.build(AppConstant.Message.SUCCESS, mapper.map(subCommentDao, SubCommentDto.class), HttpStatus.OK);

        } catch (Exception e) {
            log.error("Happened error when add comment. Error: {}", e.getMessage());
            log.trace("Get error when add comment. ", e);
            throw e;   }
    }

    public ResponseEntity<Object> getSubCommentByIdComment(Long id) {
        log.info("Executing get all comment.");
        try{
            Optional<CommentDao> commentDaoOptional = commentRepository.findById(id);
            if(commentDaoOptional.isEmpty()) {
                log.info("comment id: {} not found", id);
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            List<SubCommentDao> daoList = commentDaoOptional.get().getSubComments();
            List<SubCommentDto> list = new ArrayList<>();
            for(SubCommentDao dao : daoList){
                list.add(mapper.map(dao, SubCommentDto.class));
            }
            return ResponseUtil.build(AppConstant.Message.SUCCESS, list, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when get all comment. Error: {}", e.getMessage());
            log.trace("Get error when get all comment. ", e);
            throw e;
        }
    }

    public ResponseEntity<Object> getSubCommentById(Long id) {
        log.info("Executing get comment by id: {} ", id);
        try {
            Optional<SubCommentDao> subCommentDaoOptional = subCommentRepository.findById(id);
            if(subCommentDaoOptional.isEmpty()) {
                log.info("comment id: {} not found", id);
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            log.info("Executing get comment by id success");
            SubCommentDto subCommentDto = mapper.map(subCommentDaoOptional, SubCommentDto.class);

            return ResponseUtil.build(AppConstant.Message.SUCCESS, subCommentDto, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when get comment by id. Error: {}", e.getMessage());
            log.trace("Get error when get comment by id. ", e);
            throw e;
        }
    }

    public ResponseEntity<Object> deleteSubComment(Long id, UserDao user) {
        log.info("Executing delete comment id: {}", id);
        try{
            Optional<SubCommentDao> subCommentDaoOptional = subCommentRepository.findById(id);
            if(subCommentDaoOptional.isEmpty()) {
                log.info("comment {} not found", id);
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            if(!subCommentDaoOptional.get().getUser().getId().equals(user.getId()) && user.getRoles().equals("USER")) {
                log.info("comment {} cant delete by user {}", id, user.getUsername());
                return ResponseUtil.build(AppConstant.Message.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            subCommentRepository.deleteById(id);
            log.info("Executing delete comment success");
            return ResponseUtil.build(AppConstant.Message.SUCCESS, null, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when delete comment. Error: {}", e.getMessage());
            log.trace("Get error when delete comment. ", e);
            throw e;
        }
    }

    public ResponseEntity<Object> updateSubComment(Long id, SubCommentDto request, UserDao user) {
        log.info("Executing update comment with request: {}", request);
        try {
            Optional<SubCommentDao> subCommentDaoOptional = subCommentRepository.findById(id);
            if(subCommentDaoOptional.isEmpty()) {
                log.info("comment {} not found", id);
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            if(!subCommentDaoOptional.get().getUser().getId().equals(user.getId()) && user.getRoles().equals("USER")) {
                log.info("comment {} cant update by user {}", id, user.getUsername());
                return ResponseUtil.build(AppConstant.Message.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            subCommentDaoOptional.ifPresent(res -> {
                res.setSubComment(request.getSubComment());
                subCommentRepository.save(res);
            });
            log.info("Executing update comment success");
            return ResponseUtil.build(AppConstant.Message.SUCCESS, mapper.map(subCommentDaoOptional, SubCommentDto.class), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when update comment. Error: {}", e.getMessage());
            log.trace("Get error when update comment. ", e);
            throw e;
        }
    }
}
