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

import static org.junit.Assert.*;
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
    void followUser_Usernotfound() {
        UserDao userDao = UserDao.builder()
                .id(1l)
                .build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Object> response = service.followUser(anyLong(), userDao);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());

    }

    @Test
    void followUser_Userfollowsucces() {

        UserDao userDao = UserDao.builder()
                .id(1l)
                .build();
        UserFollowerDao userFollowerDao = UserFollowerDao.builder()
                .id(1l)
                .userFollower(userDao)
                .userFollowed(userDao)
                .build();

        NotificationDao notificationDao = NotificationDao.builder()
                .id(1l)
                .build();

        when(userFollowerRepository.findByUserFollowerIdAndUserFollowedId(anyLong(), anyLong())).thenReturn(Optional.empty());
        when(userFollowerRepository.save(any())).thenReturn(userFollowerDao);
        when(userFollowerRepository.countFollowers(anyLong())).thenReturn(1);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userDao));
        when(notificationRepository.save(any())).thenReturn(notificationDao);

        ResponseEntity<Object> response = service.followUser(anyLong(), userDao);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());

    }


    @Test
    void followUser_Userfollowsucces1() {
        UserDao userDao = UserDao.builder()
                .id(1l)
                .build();
        UserFollowerDao userFollowerDao = UserFollowerDao.builder()
                .id(1l)
                .userFollower(userDao)
                .userFollowed(userDao)
                .isFollow(true)
                .build();

        NotificationDao notificationDao = NotificationDao.builder()
                .id(1l)
                .build();

        when(userFollowerRepository.findByUserFollowerIdAndUserFollowedId(anyLong(), anyLong())).thenReturn(Optional.of(userFollowerDao));
        when(userFollowerRepository.save(any())).thenReturn(userFollowerDao);
        when(userFollowerRepository.countFollowers(anyLong())).thenReturn(1);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userDao));
        when(notificationRepository.save(any())).thenReturn(notificationDao);

        ResponseEntity<Object> response = service.followUser(anyLong(), userDao);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());


    }

    @Test
    void getFollowerByIdUser_Getfollowerfailed() {
        UserDao userDao = UserDao.builder()
                .id(1l)
                .build();

        UserFollowerDao userFollowerDao = UserFollowerDao.builder()
                .id(1l)
                .userFollower(userDao)
                .userFollowed(userDao)
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Object> response = service.getFollowerByIdUser(anyLong());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    void getFollowerByIdUser_Getfollowersucces() {

        UserFollowerDao userFollowerDao2 = UserFollowerDao.builder()
                .id(1l)
                .isFollow(true)
                .build();


        UserDao userDao = UserDao.builder()
                .id(1l)
                .userFollowers(List.of(userFollowerDao2))
                .build();

        UserFollowerDao userFollowerDao = UserFollowerDao.builder()
                .id(1l)
                .userFollower(userDao)
                .userFollowed(userDao)
                .isFollow(true)
                .build();
        List<UserFollowerDao> userFollowerDaos = userDao.getUserFollowers();



        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userDao));
        ResponseEntity<Object> response = service.getFollowerByIdUser(anyLong());
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());

    }

    @Test
    void getFollowerByIdUser_error() {
        when(userRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> service.getFollowerByIdUser(1L));
    }
}