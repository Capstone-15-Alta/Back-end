package com.capstone15.alterra.service;

import com.capstone15.alterra.constant.AppConstant;
import com.capstone15.alterra.domain.dao.*;
import com.capstone15.alterra.domain.dto.SaveThreadDto;
import com.capstone15.alterra.domain.dto.ThreadFollowerDto;
import com.capstone15.alterra.repository.NotificationRepository;
import com.capstone15.alterra.repository.SaveThreadRepository;
import com.capstone15.alterra.repository.ThreadRepository;
import com.capstone15.alterra.repository.UserRepository;
import com.capstone15.alterra.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class SaveThreadService {

    @Autowired
    private SaveThreadRepository saveThreadRepository;

    @Autowired
    private ThreadRepository threadRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    public ResponseEntity<Object> saveThread(Long id, UserDao user) {
        log.info("Executing save thread with request: {}", id);
        try{
            Optional<ThreadDao> threadDao = threadRepository.findById(id);
            if (threadDao.isEmpty()) {
                log.info("thread [{}] not found", id);
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }

            Optional<SaveThreadDao> saveThreadDaoOptional = saveThreadRepository.findByUserIdAndThreadId(user.getId(), id);

            if (saveThreadDaoOptional.isEmpty()) {
                SaveThreadDao saveThreadDao = SaveThreadDao.builder()
                        .thread(threadDao.get())
                        .user(UserDao.builder().id(user.getId()).build())
                        .isSave(true)
                        .build();
                saveThreadDao = saveThreadRepository.save(saveThreadDao);
                log.info("Executing save thread success");
                return ResponseUtil.build(AppConstant.Message.SUCCESS, mapper.map(saveThreadDao, SaveThreadDto.class), HttpStatus.OK);

            } else {
                if (saveThreadDaoOptional.get().getIsSave().equals(false)) {
                    saveThreadDaoOptional.get().setIsSave(true);
                    saveThreadRepository.save(saveThreadDaoOptional.get());
                    log.info("Executing save thread success");

                } else {
                    saveThreadDaoOptional.get().setIsSave(false);
                    saveThreadRepository.save(saveThreadDaoOptional.get());
                    log.info("Executing unsave thread success");

                }
                return ResponseUtil.build(AppConstant.Message.SUCCESS, mapper.map(saveThreadDaoOptional, SaveThreadDto.class), HttpStatus.OK);
            }
        } catch (Exception e){
            log.error("Happened error when follow thread. Error: {}", e.getMessage());
            log.trace("Get error when follow thread. ", e);
            throw e;
        }
    }

    public ResponseEntity<Object> getSaveThreadByIdUser(Long id) {
        log.info("Executing get save thread by id user.");
        try{
            Optional<UserDao> userDaoOptional = userRepository.findById(id);
            if(userDaoOptional.isEmpty()) {
                log.info("user id: {} not found", id);
                return ResponseUtil.build(AppConstant.Message.NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            List<SaveThreadDao> daoList = userDaoOptional.get().getSaveThread();
            Collections.reverse(daoList);
            List<SaveThreadDto> list = new ArrayList<>();
            for(SaveThreadDao dao : daoList){
                    list.add(mapper.map(dao, SaveThreadDto.class));
            }
            return ResponseUtil.build(AppConstant.Message.SUCCESS, list, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Happened error when get save thread by id user. Error: {}", e.getMessage());
            log.trace("Get error when get save thread by id user. ", e);
            throw e;
        }
    }

}
