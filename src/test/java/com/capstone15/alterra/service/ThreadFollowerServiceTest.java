package com.capstone15.alterra.service;

import com.capstone15.alterra.constant.AppConstant;
import com.capstone15.alterra.domain.common.ApiResponse;
import com.capstone15.alterra.domain.dao.NotificationDao;
import com.capstone15.alterra.domain.dao.ThreadDao;
import com.capstone15.alterra.domain.dao.ThreadFollowerDao;
import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dto.ThreadDto;
import com.capstone15.alterra.domain.dto.ThreadFollowerDto;
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
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ThreadFollowerService.class)
class ThreadFollowerServiceTest {

    @MockBean
    private ThreadFollowerRepository threadFollowerRepository;

    @MockBean
    private ThreadRepository threadRepository;

    @MockBean
    private NotificationRepository notificationRepository;

    @MockBean
    private ModelMapper mapper;

    @Autowired
    private ThreadFollowerService threadFollowerService;

    @Test
    void followThread_UpdateIsFollowFalseSuccess_Test() {
        ThreadDao threadDao = ThreadDao.builder()
                .id(1L)
                .user(UserDao.builder().id(1L).build())
                .build();

        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        ThreadFollowerDao threadFollowerDao = ThreadFollowerDao.builder()
                .id(1L)
                .isFollow(false)
                .build();

        NotificationDao notificationDao = NotificationDao.builder()
                .id(1L)
                .build();

        when(threadRepository.findById(anyLong())).thenReturn(Optional.of(threadDao));
        when(mapper.map(any(), eq(ThreadFollowerDao.class))).thenReturn(threadFollowerDao);
        when(threadFollowerRepository.findByUserIdAndThreadId(anyLong(), anyLong())).thenReturn(Optional.of(threadFollowerDao));
        when(threadFollowerRepository.save(any())).thenReturn(threadFollowerDao);
        when(notificationRepository.findByUserIdAndThreadIdAndInfo(anyLong(), anyLong(), any())).thenReturn(Optional.of(notificationDao));
        when(notificationRepository.save(any())).thenReturn(notificationDao);

        ResponseEntity<Object> response = threadFollowerService.followThread(anyLong(), userDao);

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void followThread_UpdateIsFollowTrueSuccess_Test() {
        ThreadDao threadDao = ThreadDao.builder()
                .id(1L)
                .user(UserDao.builder().id(1L).build())
                .build();

        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        ThreadFollowerDao threadFollowerDao = ThreadFollowerDao.builder()
                .id(1L)
                .isFollow(true)
                .build();

        NotificationDao notificationDao = NotificationDao.builder()
                .id(1L)
                .build();

        when(threadRepository.findById(anyLong())).thenReturn(Optional.of(threadDao));
        when(mapper.map(any(), eq(ThreadFollowerDao.class))).thenReturn(threadFollowerDao);
        when(threadFollowerRepository.findByUserIdAndThreadId(anyLong(), anyLong())).thenReturn(Optional.of(threadFollowerDao));
        when(threadFollowerRepository.save(any())).thenReturn(threadFollowerDao);
        when(notificationRepository.findByUserIdAndThreadIdAndInfo(anyLong(), anyLong(), any())).thenReturn(Optional.of(notificationDao));
        when(notificationRepository.save(any())).thenReturn(notificationDao);

        ResponseEntity<Object> response = threadFollowerService.followThread(anyLong(), userDao);

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void followThreadNewSuccess_Test() {
        ThreadDao threadDao = ThreadDao.builder()
                .id(1L)
                .user(UserDao.builder().id(1L).build())
                .build();

        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        ThreadFollowerDao threadFollowerDao = ThreadFollowerDao.builder()
                .id(1L)
                .build();

        NotificationDao notificationDao = NotificationDao.builder()
                .id(1L)
                .build();

        when(threadRepository.findById(anyLong())).thenReturn(Optional.of(threadDao));
        when(threadFollowerRepository.findByUserIdAndThreadId(anyLong(), anyLong())).thenReturn(Optional.empty());
        when(threadFollowerRepository.save(any())).thenReturn(threadFollowerDao);
        when(notificationRepository.save(any())).thenReturn(notificationDao);

        ResponseEntity<Object> response = threadFollowerService.followThread(anyLong(), userDao);

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void followThreadNewNotificationSuccess_Test() {
        ThreadDao threadDao = ThreadDao.builder()
                .id(1L)
                .user(UserDao.builder().id(1L).build())
                .build();

        UserDao userDao = UserDao.builder()
                .id(2L)
                .build();

        ThreadFollowerDao threadFollowerDao = ThreadFollowerDao.builder()
                .id(1L)
                .build();

        NotificationDao notificationDao = NotificationDao.builder()
                .id(1L)
                .build();

        when(threadRepository.findById(anyLong())).thenReturn(Optional.of(threadDao));
        when(threadFollowerRepository.findByUserIdAndThreadId(anyLong(), anyLong())).thenReturn(Optional.empty());
        when(threadFollowerRepository.save(any())).thenReturn(threadFollowerDao);
        when(notificationRepository.save(any())).thenReturn(notificationDao);

        ResponseEntity<Object> response = threadFollowerService.followThread(anyLong(), userDao);

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void followThread_ThreadIsEmpty_Test() {
        UserDao userDao = UserDao.builder()
                .id(2L)
                .build();

        when(threadRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> response = threadFollowerService.followThread(anyLong(), userDao);

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.NOT_FOUND, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void followThreadException_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        when(threadRepository.findById(any())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> threadFollowerService.followThread(any(), userDao));

    }

    @Test
    void getFollowerByIdThreadSuccess_Test() {

        ThreadFollowerDao threadFollowerDao = ThreadFollowerDao.builder()
                .id(1L)
                .isFollow(true)
                .build();

        ThreadFollowerDto threadFollowerDto = ThreadFollowerDto.builder()
                .isFollow(true)
                .build();

        ThreadDao threadDao = ThreadDao.builder()
                .id(1L)
                .threadFollowers(List.of(threadFollowerDao))
                .build();

        when(threadRepository.findById(anyLong())).thenReturn(Optional.of(threadDao));
        when(mapper.map(any(), eq(ThreadFollowerDao.class))).thenReturn(threadFollowerDao);
        when(mapper.map(any(), eq(ThreadFollowerDto.class))).thenReturn(threadFollowerDto);

        ResponseEntity<Object> response = threadFollowerService.getFollowerByIdThread(anyLong());

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void getFollowerByIdThreadNotFound_Test() {
        when(threadRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Object> response = threadFollowerService.getFollowerByIdThread(anyLong());

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.NOT_FOUND, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void getFollowerByIdThreadException_Test() {
        when(threadRepository.findById(any())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> threadFollowerService.getFollowerByIdThread(anyLong()));

    }




}