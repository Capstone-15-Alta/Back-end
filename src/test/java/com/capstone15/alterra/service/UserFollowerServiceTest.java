package com.capstone15.alterra.service;

import com.capstone15.alterra.constant.AppConstant;
import com.capstone15.alterra.domain.dao.NotificationDao;
import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dao.UserFollowerDao;
import com.capstone15.alterra.repository.NotificationRepository;
import com.capstone15.alterra.repository.UserFollowerRepository;
import com.capstone15.alterra.repository.UserRepository;
import com.capstone15.alterra.util.ResponseUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = UserFollowerService.class)
public class UserFollowerServiceTest {

    @MockBean
    private UserFollowerRepository userFollowerRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private NotificationRepository notificationRepository;

    @MockBean
    private ModelMapper mapper;

    @Autowired
    private UserFollowerService service;

    @Test
    void followUser_UserNotFound_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> response = service.followUser(anyLong(), userDao);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());

    }

    @Test
    void followUser_UserFollowSuccess_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        UserFollowerDao userFollowerDao = UserFollowerDao.builder()
                .id(1L)
                .userFollower(userDao)
                .userFollowed(userDao)
                .build();

        NotificationDao notificationDao = NotificationDao.builder()
                .id(1L)
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userDao));
        when(userFollowerRepository.findByUserFollowerIdAndUserFollowedId(anyLong(), anyLong())).thenReturn(Optional.empty());
        when(userFollowerRepository.save(any())).thenReturn(userFollowerDao);
        when(notificationRepository.save(any())).thenReturn(notificationDao);

        ResponseEntity<Object> response = service.followUser(anyLong(), userDao);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());

    }


    @Test
    void followUser_UserFollowSuccessTrue_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        UserFollowerDao userFollowerDao = UserFollowerDao.builder()
                .id(1L)
                .userFollower(userDao)
                .userFollowed(userDao)
                .isFollow(true)
                .build();

        NotificationDao notificationDao = NotificationDao.builder()
                .id(1L)
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userDao));
        when(userFollowerRepository.findByUserFollowerIdAndUserFollowedId(anyLong(), anyLong())).thenReturn(Optional.of(userFollowerDao));
        when(userFollowerRepository.save(any())).thenReturn(userFollowerDao);
        when(notificationRepository.findByUserIdAndFollowerId(any(), any())).thenReturn(Optional.of(notificationDao));
        when(notificationRepository.save(any())).thenReturn(notificationDao);

        ResponseEntity<Object> response = service.followUser(anyLong(), userDao);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());

    }

    @Test
    void followUser_UserFollowSuccessFalse_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        UserFollowerDao userFollowerDao = UserFollowerDao.builder()
                .id(1L)
                .userFollower(userDao)
                .userFollowed(userDao)
                .isFollow(false)
                .build();

        NotificationDao notificationDao = NotificationDao.builder()
                .id(1L)
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userDao));
        when(userFollowerRepository.findByUserFollowerIdAndUserFollowedId(anyLong(), anyLong())).thenReturn(Optional.of(userFollowerDao));
        when(userFollowerRepository.save(any())).thenReturn(userFollowerDao);
        when(notificationRepository.findByUserIdAndFollowerId(any(), any())).thenReturn(Optional.of(notificationDao));
        when(notificationRepository.save(any())).thenReturn(notificationDao);

        ResponseEntity<Object> response = service.followUser(anyLong(), userDao);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());

    }

    @Test
    void followUser_UserEquals_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        UserFollowerDao userFollowerDao = UserFollowerDao.builder()
                .id(1L)
                .userFollower(userDao)
                .userFollowed(userDao)
                .isFollow(false)
                .build();

        NotificationDao notificationDao = NotificationDao.builder()
                .id(1L)
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userDao));
        when(userFollowerRepository.findByUserFollowerIdAndUserFollowedId(anyLong(), anyLong())).thenReturn(Optional.of(userFollowerDao));
        when(userFollowerRepository.save(any())).thenReturn(userFollowerDao);
        when(notificationRepository.findByUserIdAndFollowerId(any(), any())).thenReturn(Optional.of(notificationDao));
        when(notificationRepository.save(any())).thenReturn(notificationDao);

        ResponseEntity<Object> response = service.followUser(1L, userDao);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());
    }

    @Test
    void followUserException_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        when(userRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> service.followUser(anyLong(), userDao));
    }

    @Test
    void getFollowerByIdUser_Failed_Test() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> response = service.getFollowerByIdUser(anyLong());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    void getFollowerByIdUser_Success_Test() {
        UserFollowerDao userFollowerDao = UserFollowerDao.builder()
                .id(1L)
                .isFollow(true)
                .build();

        UserDao userDao = UserDao.builder()
                .id(1L)
                .userFollowers(List.of(userFollowerDao))
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userDao));
        ResponseEntity<Object> response = service.getFollowerByIdUser(anyLong());
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());

    }

    @Test
    void getFollowerByIdUser_Exception_Test() {
        when(userRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> service.getFollowerByIdUser(1L));
    }

    @Test
    void getFollowingByIdUser_Failed_Test() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> response = service.getFollowingByIdUser(anyLong());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    void getFollowingByIdUser_Success_Test() {
        UserFollowerDao userFollowerDao = UserFollowerDao.builder()
                .id(1L)
                .isFollow(true)
                .build();

        UserDao userDao = UserDao.builder()
                .id(1L)
                .userFollowing(List.of(userFollowerDao))
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userDao));
        ResponseEntity<Object> response = service.getFollowingByIdUser(anyLong());
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());

    }

    @Test
    void getFollowingByIdUser_Exception_Test() {
        when(userRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> service.getFollowingByIdUser(1L));
    }
}