package com.capstone15.alterra.service;

import com.capstone15.alterra.constant.AppConstant;
import com.capstone15.alterra.domain.common.ApiResponse;
import com.capstone15.alterra.domain.dao.*;
import com.capstone15.alterra.domain.dto.ThreadDto;
import com.capstone15.alterra.repository.NotificationRepository;
import com.capstone15.alterra.repository.ThreadLikeRepository;
import com.capstone15.alterra.repository.ThreadRepository;
import com.capstone15.alterra.repository.UserRepository;
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
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private UserRepository userRepository;

    @MockBean
    private ModelMapper mapper;

    @Autowired
    private ThreadLikeService service;

    @Test
    void likeThread_Failed_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        when(threadRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> response = service.likeThread(anyLong(), userDao);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    void likeThread_UpdateIsLikeFalseSuccess_Test() {
        ThreadDao threadDao = ThreadDao.builder()
                .id(1L)
                .user(UserDao.builder().id(1L).build())
                .build();

        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        ThreadLikeDao threadLikeDao = ThreadLikeDao.builder()
                .id(1L)
                .isLike(false)
                .build();

        NotificationDao notificationDao = NotificationDao.builder()
                .id(1L)
                .build();

        when(threadRepository.findById(anyLong())).thenReturn(Optional.of(threadDao));
        when(mapper.map(any(), eq(ThreadLikeDao.class))).thenReturn(threadLikeDao);
        when(threadLikeRepository.findByUserIdAndThreadId(anyLong(), anyLong())).thenReturn(Optional.of(threadLikeDao));
        when(threadLikeRepository.save(any())).thenReturn(threadLikeDao);
        when(notificationRepository.findByUserIdAndThreadIdAndInfo(anyLong(), anyLong(), any())).thenReturn(Optional.of(notificationDao));
        when(notificationRepository.save(any())).thenReturn(notificationDao);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userDao));
        when(userRepository.save(any())).thenReturn(userDao);
        ResponseEntity<Object> response = service.likeThread(anyLong(), userDao);

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void likeThread_UpdateIsLikeTrueSuccess_Test() {
        ThreadDao threadDao = ThreadDao.builder()
                .id(1L)
                .user(UserDao.builder().id(1L).build())
                .build();

        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        ThreadLikeDao threadLikeDao = ThreadLikeDao.builder()
                .id(1L)
                .isLike(true)
                .build();

        NotificationDao notificationDao = NotificationDao.builder()
                .id(1L)
                .build();

        when(threadRepository.findById(anyLong())).thenReturn(Optional.of(threadDao));
        when(mapper.map(any(), eq(ThreadLikeDao.class))).thenReturn(threadLikeDao);
        when(threadLikeRepository.findByUserIdAndThreadId(anyLong(), anyLong())).thenReturn(Optional.of(threadLikeDao));
        when(threadLikeRepository.save(any())).thenReturn(threadLikeDao);
        when(notificationRepository.findByUserIdAndThreadIdAndInfo(anyLong(), anyLong(), any())).thenReturn(Optional.of(notificationDao));
        when(notificationRepository.save(any())).thenReturn(notificationDao);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userDao));
        when(userRepository.save(any())).thenReturn(userDao);
        ResponseEntity<Object> response = service.likeThread(anyLong(), userDao);

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void likeThreadNewSuccess_Test() {
        ThreadDao threadDao = ThreadDao.builder()
                .id(1L)
                .user(UserDao.builder().id(1L).build())
                .build();

        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        ThreadLikeDao threadLikeDao = ThreadLikeDao.builder()
                .id(1L)
                .build();

        when(threadRepository.findById(anyLong())).thenReturn(Optional.of(threadDao));
        when(threadLikeRepository.findByUserIdAndThreadId(anyLong(), anyLong())).thenReturn(Optional.empty());
        when(threadLikeRepository.save(any())).thenReturn(threadLikeDao);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userDao));
        when(userRepository.save(any())).thenReturn(userDao);
        ResponseEntity<Object> response = service.likeThread(anyLong(), userDao);

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void likeThreadNewNotificationSuccess_Test() {
        ThreadDao threadDao = ThreadDao.builder()
                .id(1L)
                .user(UserDao.builder().id(1L).build())
                .build();

        UserDao userDao = UserDao.builder()
                .id(2L)
                .build();

        ThreadLikeDao threadLikeDao = ThreadLikeDao.builder()
                .id(1L)
                .build();

        NotificationDao notificationDao = NotificationDao.builder()
                .id(1L)
                .build();

        when(threadRepository.findById(anyLong())).thenReturn(Optional.of(threadDao));
        when(threadLikeRepository.findByUserIdAndThreadId(anyLong(), anyLong())).thenReturn(Optional.empty());
        when(threadLikeRepository.save(any())).thenReturn(threadLikeDao);
        when(notificationRepository.findByUserIdAndThreadIdAndInfo(anyLong(), anyLong(), any())).thenReturn(Optional.of(notificationDao));
        when(notificationRepository.save(any())).thenReturn(notificationDao);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userDao));
        when(userRepository.save(any())).thenReturn(userDao);
        ResponseEntity<Object> response = service.likeThread(anyLong(), userDao);

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void likeThread_Exception_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        when(threadRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> service.likeThread(anyLong(), userDao));
    }

    @Test
    void getLikeByIdThread_Failed_Test() {
        when(threadRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> response = service.getLikeByIdThread(anyLong());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());

    }

    @Test
    void getLikeByIdThread_Success_Test() {
        ThreadLikeDao threadLikeDao = ThreadLikeDao.builder()
                .id(1L)
                .isLike(true)
                .build();

        ThreadDao threadDao = ThreadDao.builder()
                .id(1L)
                .threadLikes(List.of(threadLikeDao))
                .build();

        when(threadRepository.findById(anyLong())).thenReturn(Optional.of(threadDao));
        ResponseEntity<Object> response = service.getLikeByIdThread(anyLong());
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }

    @Test
    void getLikeByIdThread_Exception_Test() {
        when(threadRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> service.getLikeByIdThread(anyLong()));
    }

}