package com.capstone15.alterra.service;

import com.capstone15.alterra.constant.AppConstant;
import com.capstone15.alterra.domain.dao.NotificationDao;
import com.capstone15.alterra.domain.dao.ThreadDao;
import com.capstone15.alterra.domain.dao.ThreadReportDao;
import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dto.ThreadReportDto;
import com.capstone15.alterra.domain.dto.ThreadReportDtoResponse;
import com.capstone15.alterra.repository.NotificationRepository;
import com.capstone15.alterra.repository.ThreadReportRepository;
import com.capstone15.alterra.repository.ThreadRepository;
import com.capstone15.alterra.repository.UserRepository;
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
public class ThreadReportService {

    @Autowired
    private ThreadReportRepository threadReportRepository;

    @Autowired
    private ThreadRepository threadRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ModelMapper mapper;

    public ResponseEntity<Object> addReport(ThreadReportDto request, UserDao user) {
        log.info("Executing add report with request: {}", request);
        try{
            log.info("Get thread by id: {}", request.getThreadId());
            Optional<ThreadDao> threadDao = threadRepository.findById(request.getThreadId());
            if (threadDao.isEmpty()) {
                log.info("thread [{}] not found", request.getThreadId());
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }

            ThreadReportDao threadReportDao = mapper.map(request, ThreadReportDao.class);
            threadReportDao.setUser(UserDao.builder().id(user.getId()).build());
            threadReportDao.setThread(threadDao.get());
            threadReportDao = threadReportRepository.save(threadReportDao);

            // fitur notification
            if(!user.getId().equals(threadDao.get().getUser().getId())){
                NotificationDao notificationDao = NotificationDao.builder()
                        .user(UserDao.builder().id(threadDao.get().getUser().getId()).build())
                        .title("Moderator mereport thread anda: " + threadDao.get().getTitle())
                        .threadId(threadDao.get().getId())
                        .message(request.getReport())
                        .isRead(false)
                        .build();
                notificationDao = notificationRepository.save(notificationDao);
            }

            NotificationDao notificationDao = NotificationDao.builder()
                    .user(UserDao.builder().id(2L).build())
                    .title("Terdapat laporan pada thread: " + threadDao.get().getTitle())
                    .threadId(threadDao.get().getId())
                    .message(request.getReport())
                    .isRead(false)
                    .build();
            notificationDao = notificationRepository.save(notificationDao);



            log.info("Executing add report success");
            return ResponseUtil.build(AppConstant.Message.SUCCESS, mapper.map(threadReportDao, ThreadReportDto.class), HttpStatus.OK);

        } catch (Exception e) {
            log.error("Happened error when add report. Error: {}", e.getMessage());
            log.trace("Get error when add report. ", e);
            throw e;   }
    }

    public ResponseEntity<Object> getReportByIdThread(Long id) {
        log.info("Executing get all report.");
        try{
            Optional<ThreadDao> threadDaoOptional = threadRepository.findById(id);
            if(threadDaoOptional.isEmpty()) {
                log.info("thread id: {} not found", id);
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            List<ThreadReportDao> daoList = threadDaoOptional.get().getThreadReports();
            List<ThreadReportDto> list = new ArrayList<>();
            for(ThreadReportDao dao : daoList){
                list.add(mapper.map(dao, ThreadReportDto.class));
            }
            return ResponseUtil.build(AppConstant.Message.SUCCESS, list, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when get all report. Error: {}", e.getMessage());
            log.trace("Get error when get all report. ", e);
            throw e;
        }
    }

    public ResponseEntity<Object> getReportThreadById(Long id) {
        log.info("Executing get report by id: {} ", id);
        try {
            Optional<ThreadReportDao> threadReportDao = threadReportRepository.findById(id);
            if(threadReportDao.isEmpty()) {
                log.info("report id: {} not found", id);
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            log.info("Executing get report by id success");
            ThreadReportDto threadReportDto = mapper.map(threadReportDao, ThreadReportDto.class);

            return ResponseUtil.build(AppConstant.Message.SUCCESS, threadReportDto, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when get report by id. Error: {}", e.getMessage());
            log.trace("Get error when get report by id. ", e);
            throw e;
        }
    }

    public ResponseEntity<Object> deleteReport(Long id, UserDao user) {
        log.info("Executing delete report id: {}", id);
        try{
            Optional<ThreadReportDao> threadReportDao = threadReportRepository.findById(id);
            if(threadReportDao.isEmpty()) {
                log.info("report {} not found", id);
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            if(!threadReportDao.get().getUser().getId().equals(user.getId()) && user.getRoles().equals("MODERATOR")) {
                log.info("report {} cant delete by user {}", id, user.getUsername());
                return ResponseUtil.build(AppConstant.Message.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            threadReportRepository.deleteById(id);
            log.info("Executing delete report success");
            return ResponseUtil.build(AppConstant.Message.SUCCESS, null, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when delete report. Error: {}", e.getMessage());
            log.trace("Get error when delete report. ", e);
            throw e;
        }
    }

    public ResponseEntity<Object> updateReport(Long id, ThreadReportDto request, UserDao user) {
        log.info("Executing update report with request: {}", request);
        try {
            Optional<ThreadReportDao> threadReportDao = threadReportRepository.findById(id);
            if(threadReportDao.isEmpty()) {
                log.info("report {} not found", id);
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            if(!threadReportDao.get().getUser().getId().equals(user.getId()) && user.getRoles().equals("MODERATOR")) {
                log.info("report {} cant update by user {}", id, user.getUsername());
                return ResponseUtil.build(AppConstant.Message.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            threadReportDao.ifPresent(res -> {
                res.setReport(request.getReport());
                threadReportRepository.save(res);
            });
            log.info("Executing update report success");
            return ResponseUtil.build(AppConstant.Message.SUCCESS, mapper.map(threadReportDao, ThreadReportDto.class), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when update report. Error: {}", e.getMessage());
            log.trace("Get error when update report. ", e);
            throw e;
        }
    }

    public ResponseEntity<Object> getAllReport(Pageable pageable) {
        log.info("Executing get all report.");
        try{
            Page<ThreadReportDao> daoList = threadReportRepository.findAll(pageable);
            if(daoList.isEmpty()) {
                log.info("report not found");
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, daoList, HttpStatus.OK);
            }
            Page<ThreadReportDtoResponse> threadReportDtos = daoList.map(threadReportDao -> mapper.map(threadReportDao, ThreadReportDtoResponse.class));
            return ResponseUtil.build(AppConstant.Message.SUCCESS, threadReportDtos, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when get all report. Error: {}", e.getMessage());
            log.trace("Get error when get all report. ", e);
            throw e;
        }  }

}
