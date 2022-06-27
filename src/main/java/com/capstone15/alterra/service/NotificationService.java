package com.capstone15.alterra.service;

import com.capstone15.alterra.constant.AppConstant;
import com.capstone15.alterra.domain.dao.NotificationDao;
import com.capstone15.alterra.domain.dao.ThreadDao;
import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dto.NotificationDto;
import com.capstone15.alterra.domain.dto.ThreadDtoResponse;
import com.capstone15.alterra.domain.dto.UserDtoResponse;
import com.capstone15.alterra.repository.NotificationRepository;
import com.capstone15.alterra.repository.UserRepository;
import com.capstone15.alterra.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class NotificationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ModelMapper mapper;

    public ResponseEntity<Object> getAllNotification( UserDao user) {
        log.info("Executing get all notification.");
        try{
            List<NotificationDao> daoList = notificationRepository.getNotification(user.getId());
            List<NotificationDto> list = new ArrayList<>();
            for(NotificationDao dao : daoList){
                list.add(mapper.map(dao, NotificationDto.class));
            }
            return ResponseUtil.build(AppConstant.Message.SUCCESS, list, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when get all notification. Error: {}", e.getMessage());
            log.trace("Get error when get all notification. ", e);
            throw e;
        }
    }

    public ResponseEntity<Object> getNotificationById(Long id, UserDao user) {
        log.info("Executing get notification by id: {} ", id);
        try {
            Optional<NotificationDao> notificationDao = notificationRepository.findById(id);
            if(notificationDao.isEmpty()) {
                log.info("notification id: {} not found", id);
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            if(!notificationDao.get().getUser().getId().equals(user.getId()) && user.getRoles().equals("USER")) {
                log.info("notification {} cant read by user {}", id, user.getUsername());
                return ResponseUtil.build(AppConstant.Message.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            notificationDao.get().setIsRead(true);
            notificationRepository.save(notificationDao.get());
            log.info("Executing get notification by id success");
            NotificationDto notificationDto = mapper.map(notificationDao.get(), NotificationDto.class);
            return ResponseUtil.build(AppConstant.Message.SUCCESS, notificationDto, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when get notification by id. Error: {}", e.getMessage());
            log.trace("Get error when get notification by id. ", e);
            throw e;
        }
    }
}
