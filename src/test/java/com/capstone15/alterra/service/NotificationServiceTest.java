package com.capstone15.alterra.service;

import com.capstone15.alterra.domain.dao.NotificationDao;
import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dto.NotificationDto;
import com.capstone15.alterra.repository.NotificationRepository;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = NotificationService.class)
public class NotificationServiceTest {
    @MockBean
    private UserRepository userRepository;

    @MockBean
    private NotificationRepository notificationRepository;

    @MockBean
    private ModelMapper mapper;

    @Autowired
    private NotificationService service;

    @Test
    void getAllNotification_Succes_Test(){
        NotificationDao notificationDao = NotificationDao.builder()
                .id(1l)
                .isRead(false)
                .build();
        NotificationDto notificationDto = NotificationDto.builder()
                .id(1l)
                .build();
        UserDao userDao = UserDao.builder()
                .id(1l)
                .build();

        when(notificationRepository.getNotification(anyLong())).thenReturn(List.of(notificationDao));
        when(mapper.map(any(),eq(NotificationDto.class))).thenReturn(notificationDto);
        ResponseEntity<Object> response = service.getAllNotification(userDao);
        assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());
    }

    @Test
    void getAllNotification_Exception_Test(){
        NotificationDao notificationDao = NotificationDao.builder()
                .id(1l)
                .isRead(true)
                .build();
        NotificationDto notificationDto = NotificationDto.builder()
                .id(1l)
                .build();
        UserDao userDao = UserDao.builder()
                .id(1l)
                .build();
        when(notificationRepository.getNotification(anyLong())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> service.getAllNotification(userDao));


    }

    @Test
    void getNotificationById_Failed_Test(){
        UserDao userDao = UserDao.builder()
                .id(1l)
                .build();
        NotificationDao notificationDao = NotificationDao.builder()
                .id(1l)
                .build();
        when(notificationRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> response =service.getNotificationById(anyLong(),userDao);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    void getNotificationById_Error_Test(){
        UserDao userDao= UserDao.builder()
                .id(1l)
                .roles("USER")
                .username("")
                .build();

        UserDao userDao1 = UserDao.builder()
                .id(2l)
                .build();

        NotificationDao notificationDao = NotificationDao.builder()
                .id(1l)
                .isRead(true)
                .user(userDao1)
                .build();

        when(notificationRepository.findById(anyLong())).thenReturn(Optional.of(notificationDao));
        ResponseEntity<Object> response = service.getNotificationById(anyLong(),userDao);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(),response.getStatusCodeValue());
    }
    @Test
    void getNotificationById_Success_Test(){
        UserDao userDao= UserDao.builder()
                .id(1l)
                .roles("")
                .username("")
                .build();
        NotificationDao notificationDao = NotificationDao.builder()
                .id(1l)
                .isRead(true)
                .user(userDao)
                .build();

        when(notificationRepository.findById(anyLong())).thenReturn(Optional.of(notificationDao));
        when(notificationRepository.save(any())).thenReturn(notificationDao);
        ResponseEntity<Object> response = service.getNotificationById(anyLong(),userDao);
        assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());
    }

    @Test
    void getNotificationById_Exception_Test(){
        UserDao userDao= UserDao.builder()
                .id(1l)
                .roles("")
                .username("")
                .build();
        NotificationDao notificationDao = NotificationDao.builder()
                .id(1l)
                .isRead(true)
                .user(userDao)
                .build();

        when(notificationRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        when(notificationRepository.save(any())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class , () -> service.getNotificationById(anyLong(),userDao));
    }




}