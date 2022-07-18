package com.capstone15.alterra.service;

import com.capstone15.alterra.constant.AppConstant;
import com.capstone15.alterra.domain.dao.CommentDao;
import com.capstone15.alterra.domain.dao.CommentReportDao;
import com.capstone15.alterra.domain.dao.NotificationDao;
import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dto.CommentReportDto;
import com.capstone15.alterra.repository.CommentReportRepository;
import com.capstone15.alterra.repository.CommentRepository;
import com.capstone15.alterra.repository.NotificationRepository;
import com.capstone15.alterra.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CommentReportService {

    @Autowired
    private CommentReportRepository commentReportRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ModelMapper mapper;

    public ResponseEntity<Object> addReport(CommentReportDto request, UserDao user) {
        log.info("Executing add report with request: {}", request);
        try{
            log.info("Get comment by id: {}", request.getCommentId());
            Optional<CommentDao> commentDao = commentRepository.findById(request.getCommentId());
            if (commentDao.isEmpty()) {
                log.info("comment [{}] not found", request.getCommentId());
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }

            CommentReportDao commentReportDao = mapper.map(request, CommentReportDao.class);
            commentReportDao.setUser(UserDao.builder().id(user.getId()).build());
            commentReportDao.setComment(commentDao.get());
            commentReportDao = commentReportRepository.save(commentReportDao);

            // notification
            if(!user.getId().equals(commentDao.get().getUser().getId())){
                NotificationDao notificationDao = NotificationDao.builder()
                        .user(UserDao.builder().id(commentDao.get().getUser().getId()).build())
                        .title("Moderator mereport comment anda: " + commentDao.get().getComment())
                        .commentId(commentDao.get().getId())
                        .message(request.getReport())
                        .isRead(false)
                        .build();
                notificationDao = notificationRepository.save(notificationDao);
            }

            NotificationDao notificationDao = NotificationDao.builder()
                    .user(UserDao.builder().id(2L).build())
                    .title("Terdapat laporan pada comment: " + commentDao.get().getComment())
                    .commentId(commentDao.get().getId())
                    .message(request.getReport())
                    .isRead(false)
                    .build();
            notificationDao = notificationRepository.save(notificationDao);

            log.info("Executing add report success");
            return ResponseUtil.build(AppConstant.Message.SUCCESS, mapper.map(commentReportDao, CommentReportDto.class), HttpStatus.OK);

        } catch (Exception e) {
            log.error("Happened error when add report. Error: {}", e.getMessage());
            log.trace("Get error when add report. ", e);
            throw e;   }
    }

    public ResponseEntity<Object> getReportByIdComment(Long id) {
        log.info("Executing get all report.");
        try{
            Optional<CommentDao> commentDaoOptional = commentRepository.findById(id);
            if(commentDaoOptional.isEmpty()) {
                log.info("comment id: {} not found", id);
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            List<CommentReportDao> daoList = commentDaoOptional.get().getCommentReports();
            List<CommentReportDto> list = new ArrayList<>();
            for(CommentReportDao dao : daoList){
                list.add(mapper.map(dao, CommentReportDto.class));
            }

            return ResponseUtil.build(AppConstant.Message.SUCCESS, list, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when get all report. Error: {}", e.getMessage());
            log.trace("Get error when get all report. ", e);
            throw e;
        }
    }

    public ResponseEntity<Object> getReportCommentById(Long id) {
        log.info("Executing get report by id: {} ", id);
        try {
            Optional<CommentReportDao> commentReportDao = commentReportRepository.findById(id);
            if(commentReportDao.isEmpty()) {
                log.info("report id: {} not found", id);
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            log.info("Executing get report by id success");
            CommentReportDto commentDtoResponse = mapper.map(commentReportDao, CommentReportDto.class);

            return ResponseUtil.build(AppConstant.Message.SUCCESS, commentDtoResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when get report by id. Error: {}", e.getMessage());
            log.trace("Get error when get report by id. ", e);
            throw e;
        }
    }

    public ResponseEntity<Object> deleteReport(Long id, UserDao user) {
        log.info("Executing delete report id: {}", id);
        try{
            Optional<CommentReportDao> commentReportDao = commentReportRepository.findById(id);
            if(commentReportDao.isEmpty()) {
                log.info("report {} not found", id);
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            if(!commentReportDao.get().getUser().getId().equals(user.getId()) && user.getRoles().equals("MODERATOR")) {
                log.info("report {} cant delete by user {}", id, user.getUsername());
                return ResponseUtil.build(AppConstant.Message.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            commentReportRepository.deleteById(id);
            log.info("Executing delete report success");
            return ResponseUtil.build(AppConstant.Message.SUCCESS, null, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when delete report. Error: {}", e.getMessage());
            log.trace("Get error when delete report. ", e);
            throw e;
        }
    }

    public ResponseEntity<Object> updateReport(Long id, CommentReportDto request, UserDao user) {
        log.info("Executing update report with request: {}", request);
        try {
            Optional<CommentReportDao> commentReportDao = commentReportRepository.findById(id);
            if(commentReportDao.isEmpty()) {
                log.info("report {} not found", id);
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            if(!commentReportDao.get().getUser().getId().equals(user.getId()) && user.getRoles().equals("MODERATOR")) {
                log.info("report {} cant update by user {}", id, user.getUsername());
                return ResponseUtil.build(AppConstant.Message.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            commentReportDao.ifPresent(res -> {
                res.setReport(request.getReport());
                commentReportRepository.save(res);
            });
            log.info("Executing update report success");
            return ResponseUtil.build(AppConstant.Message.SUCCESS, mapper.map(commentReportDao, CommentReportDto.class), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when update report. Error: {}", e.getMessage());
            log.trace("Get error when update report. ", e);
            throw e;
        }
    }

    public ResponseEntity<Object> getAllReport(Pageable pageable) {
        log.info("Executing get all report.");
        try{
            Page<CommentReportDao> daoList = commentReportRepository.findAll(pageable);
            if(daoList.isEmpty()) {
                log.info("report not found");
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            Page<CommentReportDto> commentReportDtos = daoList.map(commentReportDao -> mapper.map(commentReportDao, CommentReportDto.class));
            return ResponseUtil.build(AppConstant.Message.SUCCESS, commentReportDtos, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when get all report. Error: {}", e.getMessage());
            log.trace("Get error when get all report. ", e);
            throw e;
        }
    }

}
