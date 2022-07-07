package com.capstone15.alterra.service;

import com.capstone15.alterra.constant.AppConstant;
import com.capstone15.alterra.domain.common.ApiResponse;
import com.capstone15.alterra.domain.dao.NotificationDao;
import com.capstone15.alterra.domain.dao.ThreadDao;
import com.capstone15.alterra.domain.dao.ThreadFollowerDao;
import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dto.NotificationDto;
import com.capstone15.alterra.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
@SpringBootTest(classes = NotificationService.class)
class NotificationServiceTest {

    @MockBean
    private NotificationRepository notificationRepository;

    @MockBean
    private ModelMapper mapper;

    @Autowired
    private NotificationService notificationService;

    @Test
    void getAllNotificationSuccess_Test() {
        NotificationDao notificationDao = NotificationDao.builder()
                .id(1L)
                .build();

        NotificationDto notificationDto = NotificationDto.builder()
                .id(1L)
                .build();

        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        Page<NotificationDao> notificationDaos = new PageImpl<>(List.of(notificationDao));
        when(notificationRepository.getNotification(anyLong(), any())).thenReturn(notificationDaos);
        when(mapper.map(any(), eq(NotificationDao.class))).thenReturn(notificationDao);
        when(mapper.map(any(), eq(NotificationDto.class))).thenReturn(notificationDto);

        ResponseEntity<Object> response = notificationService.getAllNotification(userDao, null);

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void getAllNotificationNotFound_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        Page<NotificationDao> notificationDaos = new PageImpl<>(List.of());
        when(notificationRepository.getNotification(anyLong(), any())).thenReturn(notificationDaos);

        ResponseEntity<Object> response = notificationService.getAllNotification(userDao, null);

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.NOT_FOUND, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void getAllNotificationException_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        when(notificationRepository.getNotification(anyLong(), any())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> notificationService.getAllNotification(userDao, null));
    }

    @Test
    void readNotificationByIdSuccess_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        NotificationDao notificationDao = NotificationDao.builder()
                .id(1L)
                .user(userDao)
                .build();

        NotificationDto notificationDto = NotificationDto.builder()
                .id(1L)
                .build();

        when(notificationRepository.findById(anyLong())).thenReturn(Optional.of(notificationDao));
        when(mapper.map(any(), eq(NotificationDao.class))).thenReturn(notificationDao);
        when(mapper.map(any(), eq(NotificationDto.class))).thenReturn(notificationDto);
        when(notificationRepository.save(any())).thenReturn(notificationDao);

        ResponseEntity<Object> response = notificationService.readNotificationById(anyLong(), userDao);

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void readNotificationByIdNotFound_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        when(notificationRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> response = notificationService.readNotificationById(anyLong(), userDao);

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.NOT_FOUND, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void readNotificationByIdDenied_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .roles("USER")
                .build();

        NotificationDao notificationDao = NotificationDao.builder()
                .id(1L)
                .user(UserDao.builder().id(2L).build())
                .build();

        when(notificationRepository.findById(anyLong())).thenReturn(Optional.of(notificationDao));
        ResponseEntity<Object> response = notificationService.readNotificationById(anyLong(), userDao);

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.UNKNOWN_ERROR, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void readNotificationByIdException_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        when(notificationRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> notificationService.readNotificationById(anyLong(), userDao));
    }

    @Test
    void readAllNotificationSuccess_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        NotificationDao notificationDao = NotificationDao.builder()
                .id(1L)
                .user(userDao)
                .build();

        when(notificationRepository.getNotificationList(anyLong())).thenReturn(List.of(notificationDao));
        when(mapper.map(any(), eq(NotificationDao.class))).thenReturn(notificationDao);

        ResponseEntity<Object> response = notificationService.readAllNotification(userDao);

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void readAllNotificationNotFound_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        when(notificationRepository.getNotificationList(anyLong())).thenReturn(List.of());
        ResponseEntity<Object> response = notificationService.readAllNotification(userDao);

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.NOT_FOUND, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void readAllNotificationException_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        when(notificationRepository.getNotificationList(anyLong())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> notificationService.readAllNotification(userDao));
    }


}