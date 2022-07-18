package com.capstone15.alterra.service;

import com.capstone15.alterra.domain.dao.NotificationDao;
import com.capstone15.alterra.domain.dao.ThreadDao;
import com.capstone15.alterra.domain.dao.ThreadLikeDao;
import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dto.ThreadDto;
import com.capstone15.alterra.repository.NotificationRepository;
import com.capstone15.alterra.repository.ThreadLikeRepository;
import com.capstone15.alterra.repository.ThreadRepository;
import org.h2.engine.User;
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
@SpringBootTest(classes = ThreadLikeService.class)
public class ThreadLikeServiceTest {
    @MockBean
    private ThreadLikeRepository threadLikeRepository;

    @MockBean
    private ThreadRepository threadRepository;

    @MockBean
    private NotificationRepository notificationRepository;

    @MockBean
    private ModelMapper mapper;

    @Autowired
    private ThreadLikeService service;

    @Test
    void likethread_Failed_Test() {

        ThreadDao threadDao = ThreadDao.builder()
                .id(1l)
                .build();

        UserDao userDao = UserDao.builder()
                .id(1l)
                .build();
        when(threadRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> response = service.likeThread(anyLong(), userDao);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }


    @Test
    void likethread_Success_Test() {
        UserDao userDao = UserDao.builder()
                .id(1l)
                .build();
        ThreadDao threadDao = ThreadDao.builder()
                .id(1l)
                .user(userDao)
                .build();

        NotificationDao notificationDao = NotificationDao.builder()
                .id(1l)
                .build();
        when(threadRepository.findById(anyLong())).thenReturn(Optional.of(threadDao));
        when(notificationRepository.save(any())).thenReturn(notificationDao);
        ResponseEntity<Object> response = service.likeThread(anyLong(), userDao);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }

    @Test
    void likethread_Success2_Test() {
        UserDao userDao = UserDao.builder()
                .id(1l)
                .build();
        ThreadDao threadDao = ThreadDao.builder()
                .id(1l)
                .user(userDao)
                .build();
        ThreadLikeDao threadLikeDao = ThreadLikeDao.builder()
                .id(1l)
                .isLike(true)
                .build();

        NotificationDao notificationDao = NotificationDao.builder()
                .id(1l)
                .build();
        when(threadRepository.findById(anyLong())).thenReturn(Optional.of(threadDao));
        when(threadLikeRepository.findByUserIdAndThreadId(anyLong(),anyLong())).thenReturn(Optional.of(threadLikeDao));
        when(threadLikeRepository.save(any())).thenReturn(threadLikeDao);
        when(threadLikeRepository.countLikes(anyLong())).thenReturn(12);
        when(threadRepository.save(any())).thenReturn(threadDao);
        when(notificationRepository.findByUserIdAndThreadIdAndInfo(anyLong(),anyLong(),anyString())).thenReturn(Optional.of(notificationDao));
        when(notificationRepository.save(any())).thenReturn(notificationDao);
        ResponseEntity<Object> response = service.likeThread(anyLong(), userDao);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }

    @Test
    void likethread_Succes3_Test(){
        UserDao userDao = UserDao.builder()
                .id(1l)
                .build();
        ThreadDao threadDao = ThreadDao.builder()
                .id(1l)
                .user(userDao)
                .build();
        ThreadLikeDao threadLikeDao = ThreadLikeDao.builder()
                .id(1l)
                .isLike(false)
                .build();

        NotificationDao notificationDao = NotificationDao.builder()
                .id(1l)
                .isRead(false)
                .build();
        when(threadRepository.findById(anyLong())).thenReturn(Optional.of(threadDao));
        when(threadLikeRepository.findByUserIdAndThreadId(anyLong(),anyLong())).thenReturn(Optional.of(threadLikeDao));
        when(threadLikeRepository.save(any())).thenReturn(threadLikeDao);
        when(threadLikeRepository.countLikes(anyLong())).thenReturn(12);
        when(threadRepository.save(any())).thenReturn(threadDao);
        when(notificationRepository.findByUserIdAndThreadIdAndInfo(anyLong(),anyLong(),anyString())).thenReturn(Optional.of(notificationDao));
        when(notificationRepository.save(any())).thenReturn(notificationDao);
        ResponseEntity<Object> response = service.likeThread(anyLong(), userDao);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
}
    @Test
    void likethread_Exception_Test() {
        UserDao userDao = UserDao.builder()
                .id(1l)
                .build();
        ThreadDao threadDao = ThreadDao.builder()
                .id(1l)
                .user(userDao)
                .build();
        ThreadLikeDao threadLikeDao = ThreadLikeDao.builder()
                .id(1l)
                .build();

        NotificationDao notificationDao = NotificationDao.builder()
                .id(1l)
                .build();
        when(threadRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        when(notificationRepository.save(any())).thenReturn(notificationDao);
        when(threadLikeRepository.save(any())).thenReturn(threadLikeDao);
        when(threadLikeRepository.countLikes(anyLong())).thenReturn(1);
        when(threadRepository.save(any())).thenReturn(threadDao);
        when(notificationRepository.findByUserIdAndThreadIdAndInfo(anyLong(), anyLong(), anyString())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> service.likeThread(anyLong(), userDao));
    }

    @Test
    void getLikeByIdThread_Failed_Test() {
        ThreadDao threadDao = ThreadDao.builder()
                .id(1l)
                .build();

        when(threadRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> response = service.getLikeByIdThread(anyLong());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());

    }

    @Test
    void getLikeByIdThread_Success_Test() {
        ThreadLikeDao threadLikeDao = ThreadLikeDao.builder()
                .id(1l)
                .isLike(true)
                .build();

        ThreadDao threadDao = ThreadDao.builder()
                .id(1l)
                .threadLikes(List.of(threadLikeDao))
                .build();

        ThreadDto threadDto = ThreadDto.builder()
                .id(1l)
                .build();
        when(threadRepository.findById(anyLong())).thenReturn(Optional.of(threadDao));

        ResponseEntity<Object> response = service.getLikeByIdThread(anyLong());
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }

    @Test
    void getLikeByIdThread_Exception_Test() {
        ThreadLikeDao threadLikeDao = ThreadLikeDao.builder()
                .id(1l)
                .isLike(true)
                .build();

        ThreadDao threadDao = ThreadDao.builder()
                .id(1l)
                .threadLikes(List.of(threadLikeDao))
                .build();

        ThreadDto threadDto = ThreadDto.builder()
                .id(1l)
                .build();
        when(threadRepository.findById(anyLong())).thenThrow(NullPointerException.class);


        assertThrows(Exception.class, () -> service.getLikeByIdThread(anyLong()));
    }


}