package com.capstone15.alterra.service;

import com.capstone15.alterra.constant.AppConstant;
import com.capstone15.alterra.domain.common.ApiResponse;
import com.capstone15.alterra.domain.dao.*;
import com.capstone15.alterra.domain.dto.CategoryDto;
import com.capstone15.alterra.domain.dto.CommentLikeDto;
import com.capstone15.alterra.domain.dto.ThreadFollowerDto;
import com.capstone15.alterra.repository.CommentLikeRepository;
import com.capstone15.alterra.repository.CommentRepository;
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
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CommentLikeService.class)
public class CommentLikeServiceTest {

    @MockBean
    private CommentLikeRepository commentLikeRepository;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private NotificationRepository notificationRepository;

    @MockBean
    private ModelMapper mapper;

    @Autowired
    private CommentLikeService service;

    @Test
    void likeComment_Failed_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        when(commentLikeRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> response = service.likeComment(any(), userDao);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());

    }

    @Test
    void likeComment_UpdateIsLikeTrueSuccess_Test() {
        CommentLikeDao commentLikeDao = CommentLikeDao.builder()
                .id(1L)
                .isLike(true)
                .build();

        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        CommentDao commentDao = CommentDao.builder()
                .id(1L)
                .user(userDao)
                .build();

        NotificationDao notificationDao = NotificationDao.builder()
                .id(1L)
                .build();

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(commentDao));
        when(commentLikeRepository.findByUserIdAndCommentId(anyLong(), anyLong())).thenReturn(Optional.of(commentLikeDao));
        when(commentLikeRepository.save(any())).thenReturn(commentLikeDao);
        when(notificationRepository.findByUserIdAndCommentIdAndInfo(anyLong(), anyLong(), any())).thenReturn(Optional.of(notificationDao));
        when(notificationRepository.save(any())).thenReturn(notificationDao);
        when(userRepository.findById(any())).thenReturn(Optional.of(userDao));
        when(userRepository.save(any())).thenReturn(userDao);

        ResponseEntity<Object> response = service.likeComment(anyLong(), userDao);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());

    }

    @Test
    void likeComment_UpdateIsLikeFalseSuccess_Test() {
        CommentLikeDao commentLikeDao = CommentLikeDao.builder()
                .id(1L)
                .isLike(false)
                .build();

        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        CommentDao commentDao = CommentDao.builder()
                .id(1L)
                .user(userDao)
                .build();

        NotificationDao notificationDao = NotificationDao.builder()
                .id(1L)
                .build();

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(commentDao));
        when(commentLikeRepository.findByUserIdAndCommentId(anyLong(), anyLong())).thenReturn(Optional.of(commentLikeDao));
        when(commentLikeRepository.save(any())).thenReturn(commentLikeDao);
        when(notificationRepository.findByUserIdAndCommentIdAndInfo(anyLong(), anyLong(), any())).thenReturn(Optional.of(notificationDao));
        when(notificationRepository.save(any())).thenReturn(notificationDao);
        when(userRepository.findById(any())).thenReturn(Optional.of(userDao));
        when(userRepository.save(any())).thenReturn(userDao);

        ResponseEntity<Object> response = service.likeComment(anyLong(), userDao);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());

    }

    @Test
    void likeComment_NewSuccess_Test() {
        CommentLikeDao commentLikeDao = CommentLikeDao.builder()
                .id(1L)
                .build();

        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        CommentDao commentDao = CommentDao.builder()
                .id(1L)
                .user(UserDao.builder().id(2L).build())
                .build();

        NotificationDao notificationDao = NotificationDao.builder()
                .id(1L)
                .build();

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(commentDao));
        when(commentLikeRepository.findByUserIdAndCommentId(anyLong(), anyLong())).thenReturn(Optional.empty());
        when(commentLikeRepository.save(any())).thenReturn(commentLikeDao);
        when(notificationRepository.save(any())).thenReturn(notificationDao);
        when(userRepository.findById(any())).thenReturn(Optional.of(userDao));
        when(userRepository.save(any())).thenReturn(userDao);

        ResponseEntity<Object> response = service.likeComment(anyLong(), userDao);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());

    }

    @Test
    void likeComment_Exception_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        when(commentRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> service.likeComment(anyLong(), userDao));
    }

    @Test
    void getLikeByIdComment_Failed_Test() {
        when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Object> response = service.getLikeByIdComment(anyLong());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    void getLikeByIdComment_Exception_Test() {
        when(commentRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> service.getLikeByIdComment(1L));

    }

    @Test
    void getLikeByIdCommentSuccess_Test() {

        CommentLikeDao commentLikeDao = CommentLikeDao.builder()
                .id(1L)
                .isLike(true)
                .build();

        CommentLikeDto commentLikeDto = CommentLikeDto.builder()
                .isLike(true)
                .build();

        CommentDao commentDao = CommentDao.builder()
                .id(1L)
                .commentLikes(List.of(commentLikeDao))
                .build();

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(commentDao));
        when(mapper.map(any(), eq(CommentLikeDao.class))).thenReturn(commentLikeDao);
        when(mapper.map(any(), eq(CommentLikeDto.class))).thenReturn(commentLikeDto);

        ResponseEntity<Object> response = service.getLikeByIdComment(anyLong());

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());

    }

}