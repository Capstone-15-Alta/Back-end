package com.capstone15.alterra.service;

import com.capstone15.alterra.domain.dao.NotificationDao;
import com.capstone15.alterra.domain.dao.ThreadDao;
import com.capstone15.alterra.domain.dao.ThreadFollowerDao;
import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.repository.NotificationRepository;
import com.capstone15.alterra.repository.ThreadFollowerRepository;
import com.capstone15.alterra.repository.ThreadRepository;
import com.capstone15.alterra.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ThreadFollowerService.class)
public class ThreadFollowerServiceTest {
    @MockBean
    private ThreadFollowerRepository threadFollowerRepository;

    @MockBean
    private ThreadRepository threadRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private NotificationRepository notificationRepository;

    @MockBean
    private ModelMapper mapper;

    @Autowired
    private ThreadFollowerService service;


    @Test
    void followthread_Failed() {
        NotificationDao notificationDao = NotificationDao.builder()
                .id(1l)
                .build();
        UserDao userDao = UserDao.builder()
                .id(1l)
                .build();
        when(threadRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> response = service.followThread(anyLong(), userDao);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());

    }

    @Test
    void followthread_Success_Test() {
        NotificationDao notificationDao = NotificationDao.builder()
                .id(1l)
                .build();
        UserDao userDao = UserDao.builder()
                .id(1l)
                .build();

        ThreadDao threadDao = ThreadDao.builder()
                .id(1l)
                .user(userDao)
                .build();
        ThreadFollowerDao threadFollowerDao = ThreadFollowerDao.builder()
                .id(1l)
                .isFollow(true)
                .build();
        when(threadRepository.findById(anyLong())).thenReturn(Optional.of(threadDao));
        when(threadFollowerRepository.findByUserIdAndThreadId(anyLong(), anyLong())).thenReturn(Optional.of(threadFollowerDao));
        when(threadFollowerRepository.save(any())).thenReturn(threadFollowerDao);
        when(threadFollowerRepository.countFollowers(anyLong())).thenReturn(12);
        when(threadRepository.save(any())).thenReturn(threadDao);
        when(notificationRepository.save(any())).thenReturn(notificationDao);
        ResponseEntity<Object> response = service.followThread(anyLong(), userDao);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());


    }

    @Test
    void followthread_Success2_Test() {


        NotificationDao notificationDao = NotificationDao.builder()
                .id(1l)
                .isRead(true)
                .build();
        UserDao userDao = UserDao.builder()
                .id(1l)
                .build();

        ThreadDao threadDao = ThreadDao.builder()
                .id(1l)
                .user(userDao)
                .build();
        ThreadFollowerDao threadFollowerDao = ThreadFollowerDao.builder()
                .id(1l)
                .isFollow(true)
                .build();
        when(threadRepository.findById(anyLong())).thenReturn(Optional.of(threadDao));
        when(threadFollowerRepository.findByUserIdAndThreadId(anyLong(), anyLong())).thenReturn(Optional.of(threadFollowerDao));
        when(threadFollowerRepository.save(any())).thenReturn(threadFollowerDao);
        when(threadFollowerRepository.countFollowers(anyLong())).thenReturn(12);
        when(threadRepository.save(any())).thenReturn(threadDao);
        when(notificationRepository.save(any())).thenReturn(notificationDao);
        when(notificationRepository.findByUserIdAndThreadIdAndInfo(anyLong(),anyLong(),anyString())).thenReturn(Optional.of(notificationDao));
        when(notificationRepository.save(any())).thenReturn(notificationDao);
        ResponseEntity<Object> response = service.followThread(anyLong(), userDao);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }


    @Test
    void followthread_Success3_Test() {

        NotificationDao notificationDao = NotificationDao.builder()
                .id(1l)
                .isRead(false)
                .build();
        UserDao userDao = UserDao.builder()
                .id(1l)
                .build();

        ThreadDao threadDao = ThreadDao.builder()
                .id(1l)
                .user(userDao)
                .build();
        ThreadFollowerDao threadFollowerDao = ThreadFollowerDao.builder()
                .id(1l)
                .isFollow(false)
                .build();
        when(threadRepository.findById(anyLong())).thenReturn(Optional.of(threadDao));
        when(threadFollowerRepository.findByUserIdAndThreadId(anyLong(), anyLong())).thenReturn(Optional.of(threadFollowerDao));
        when(threadFollowerRepository.save(any())).thenReturn(threadFollowerDao);
        when(threadFollowerRepository.countFollowers(anyLong())).thenReturn(12);
        when(threadRepository.save(any())).thenReturn(threadDao);
        when(notificationRepository.save(any())).thenReturn(notificationDao);
        when(notificationRepository.findByUserIdAndThreadIdAndInfo(anyLong(),anyLong(),anyString())).thenReturn(Optional.of(notificationDao));
        when(notificationRepository.save(any())).thenReturn(notificationDao);
        ResponseEntity<Object> response = service.followThread(anyLong(), userDao);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }



    @Test
    void followthread_Exception_Test() {


        NotificationDao notificationDao = NotificationDao.builder()
                .id(1l)
                .build();
        UserDao userDao = UserDao.builder()
                .id(1l)
                .build();

        ThreadDao threadDao = ThreadDao.builder()
                .id(1l)
                .user(userDao)
                .build();
        ThreadFollowerDao threadFollowerDao = ThreadFollowerDao.builder()
                .id(1l)
                .isFollow(true)
                .build();
        when(threadRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        when(threadFollowerRepository.findByUserIdAndThreadId(anyLong(), anyLong())).thenReturn(Optional.empty());
        when(threadFollowerRepository.save(any())).thenReturn(threadFollowerDao);
        when(threadFollowerRepository.countFollowers(anyLong())).thenReturn(12);
        when(threadRepository.save(any())).thenReturn(threadDao);
        when(notificationRepository.save(any())).thenReturn(notificationDao);
        assertThrows(Exception.class, () -> service.followThread(anyLong(), userDao));
    }


    @Test
    void getFollowerByIdThread_Failed_Test() {
        ThreadDao threadDao = ThreadDao.builder()
                .id(1l)
                .build();

        when(threadRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> response = service.getFollowerByIdThread(anyLong());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    void getFollowerByIdThread_Success_Test() {
        ThreadFollowerDao threadFollowerDao = ThreadFollowerDao.builder()
                .id(1l)
                .isFollow(true)
                .build();

        ThreadDao threadDao = ThreadDao.builder()
                .id(1l)
                .threadFollowers(List.of(threadFollowerDao))
                .build();


        when(threadRepository.findById(anyLong())).thenReturn(Optional.of(threadDao));
        ResponseEntity<Object> response = service.getFollowerByIdThread(anyLong());
        assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());
    }

    @Test
    void getFollowerByIdThread_Exception_Test(){
        ThreadFollowerDao threadFollowerDao = ThreadFollowerDao.builder()
                .id(1l)
                .isFollow(true)
                .build();

        ThreadDao threadDao = ThreadDao.builder()
                .id(1l)
                .threadFollowers(List.of(threadFollowerDao))
                .build();


        when(threadRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> service.getFollowerByIdThread(anyLong()));

    }

}

