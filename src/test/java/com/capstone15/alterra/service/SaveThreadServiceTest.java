package com.capstone15.alterra.service;

import com.capstone15.alterra.constant.AppConstant;
import com.capstone15.alterra.domain.common.ApiResponse;
import com.capstone15.alterra.domain.dao.*;
import com.capstone15.alterra.domain.dto.ThreadFollowerDto;
import com.capstone15.alterra.repository.*;
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
@SpringBootTest(classes = SaveThreadService.class)
class SaveThreadServiceTest {

    @MockBean
    private SaveThreadRepository saveThreadRepository;

    @MockBean
    private ThreadRepository threadRepository;

    @MockBean
    private NotificationRepository notificationRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ModelMapper mapper;

    @Autowired
    private SaveThreadService saveThreadService;

    @Test
    void saveThread_UpdateIsSaveFalseSuccess_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        ThreadDao threadDao = ThreadDao.builder()
                .id(1L)
                .user(userDao)
                .build();

        SaveThreadDao saveThreadDao = SaveThreadDao.builder()
                .id(1L)
                .isSave(false)
                .build();

        when(threadRepository.findById(anyLong())).thenReturn(Optional.of(threadDao));
        when(mapper.map(any(), eq(SaveThreadDao.class))).thenReturn(saveThreadDao);
        when(saveThreadRepository.findByUserIdAndThreadId(anyLong(), anyLong())).thenReturn(Optional.of(saveThreadDao));
        when(saveThreadRepository.save(any())).thenReturn(saveThreadDao);

        ResponseEntity<Object> response = saveThreadService.saveThread(anyLong(), userDao);

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void saveThread_UpdateIsSaveTrueSuccess_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        ThreadDao threadDao = ThreadDao.builder()
                .id(1L)
                .user(userDao)
                .build();

        SaveThreadDao saveThreadDao = SaveThreadDao.builder()
                .id(1L)
                .isSave(true)
                .build();

        when(threadRepository.findById(anyLong())).thenReturn(Optional.of(threadDao));
        when(mapper.map(any(), eq(SaveThreadDao.class))).thenReturn(saveThreadDao);
        when(saveThreadRepository.findByUserIdAndThreadId(anyLong(), anyLong())).thenReturn(Optional.of(saveThreadDao));
        when(saveThreadRepository.save(any())).thenReturn(saveThreadDao);

        ResponseEntity<Object> response = saveThreadService.saveThread(anyLong(), userDao);

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void saveThread_NewSuccess_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        ThreadDao threadDao = ThreadDao.builder()
                .id(1L)
                .user(userDao)
                .build();

        SaveThreadDao saveThreadDao = SaveThreadDao.builder()
                .id(1L)
                .build();

        when(threadRepository.findById(anyLong())).thenReturn(Optional.of(threadDao));
        when(mapper.map(any(), eq(SaveThreadDao.class))).thenReturn(saveThreadDao);
        when(saveThreadRepository.findByUserIdAndThreadId(anyLong(), anyLong())).thenReturn(Optional.empty());
        when(saveThreadRepository.save(any())).thenReturn(saveThreadDao);

        ResponseEntity<Object> response = saveThreadService.saveThread(anyLong(), userDao);

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void saveThread_IsEmpty_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        SaveThreadDao saveThreadDao = SaveThreadDao.builder()
                .id(1L)
                .build();

        when(threadRepository.findById(anyLong())).thenReturn(Optional.empty());
         when(saveThreadRepository.save(any())).thenReturn(saveThreadDao);

        ResponseEntity<Object> response = saveThreadService.saveThread(anyLong(), userDao);

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.NOT_FOUND, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void saveThreadException_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        when(threadRepository.findById(any())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> saveThreadService.saveThread(any(), userDao));

    }

    @Test
    void getSaveThreadByIdUserSuccess_Test() {
        SaveThreadDao saveThreadDao = SaveThreadDao.builder()
                .id(1L)
                .build();

        UserDao userDao = UserDao.builder()
                .id(1L)
                .saveThread(List.of(saveThreadDao))
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userDao));
        when(mapper.map(any(), eq(SaveThreadDao.class))).thenReturn(saveThreadDao);

        ResponseEntity<Object> response = saveThreadService.getSaveThreadByIdUser(anyLong());

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void getSaveThreadByIdUserIsEmpty_Test() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Object> response = saveThreadService.getSaveThreadByIdUser(anyLong());

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.NOT_FOUND, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void getSaveThreadByIdUserException_Test() {
        when(userRepository.findById(any())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> saveThreadService.getSaveThreadByIdUser(anyLong()));

    }

}