package com.capstone15.alterra.service;

import com.capstone15.alterra.domain.dao.*;
import com.capstone15.alterra.domain.dto.CategoryDto;
import com.capstone15.alterra.domain.dto.CommentLikeDto;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

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
        CommentLikeDao commentLikeDao = CommentLikeDao.builder()
                .id(1l)
                .build();

        UserDao userDao = UserDao.builder()
                .id(1l)
                .build();
        when(commentLikeRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Object> response = service.likeComment(any(), userDao);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());


    }


    @Test
    void likeComment_Sucess_Test() {
        CommentLikeDao commentLikeDao = CommentLikeDao.builder()
                .id(1l)
                .build();

        UserDao userDao = UserDao.builder()
                .id(1l)
                .build();
        CommentDao commentDao = CommentDao.builder()
                .id(1l)
                .user(userDao)
                .build();
        NotificationDao notificationDao = NotificationDao.builder()
                .id(1l)
                .build();

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(commentDao));
        when(commentLikeRepository.findByUserIdAndCommentId(anyLong(), anyLong())).thenReturn(Optional.empty());
        when(commentLikeRepository.save(any())).thenReturn(commentLikeDao);
        when(commentLikeRepository.countLikes(anyLong())).thenReturn(1);
        when(commentRepository.save(any())).thenReturn(commentDao);
        when(notificationRepository.save(any())).thenReturn(notificationDao);


        ResponseEntity<Object> response = service.likeComment(anyLong(), userDao);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());


    }

    @Test
    void likeComment_Sucess2_Test() {
        CommentLikeDao commentLikeDao = CommentLikeDao.builder()
                .id(1l)
                .isLike(true)
                .build();

        UserDao userDao = UserDao.builder()
                .id(1l)
                .build();
        CommentDao commentDao = CommentDao.builder()
                .id(1l)
                .user(userDao)
                .build();
        NotificationDao notificationDao = NotificationDao.builder()
                .id(1l)
                .build();

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(commentDao));
        when(commentLikeRepository.findByUserIdAndCommentId(anyLong(), anyLong())).thenReturn(Optional.of(commentLikeDao));
        when(commentLikeRepository.save(any())).thenReturn(commentLikeDao);
        when(commentLikeRepository.countLikes(anyLong())).thenReturn(1);
        when(commentRepository.save(any())).thenReturn(commentDao);
        when(notificationRepository.findByUserIdAndCommentIdAndInfo(anyLong(), anyLong(), anyString())).thenReturn(Optional.of(notificationDao));
        when(notificationRepository.save(any())).thenReturn(notificationDao);
        ResponseEntity<Object> response = service.likeComment(anyLong(), userDao);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());


    }

    @Test
    void likeComment_Sucess3_Test() {
        CommentLikeDao commentLikeDao = CommentLikeDao.builder()
                .id(1l)
                .isLike(false)
                .build();

        UserDao userDao = UserDao.builder()
                .id(1l)
                .build();
        CommentDao commentDao = CommentDao.builder()
                .id(1l)
                .user(userDao)
                .build();
        NotificationDao notificationDao = NotificationDao.builder()
                .id(1l)
                .build();

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(commentDao));
        when(commentLikeRepository.findByUserIdAndCommentId(anyLong(), anyLong())).thenReturn(Optional.of(commentLikeDao));
        when(commentLikeRepository.save(any())).thenReturn(commentLikeDao);
        when(commentLikeRepository.countLikes(anyLong())).thenReturn(1);
        when(commentRepository.save(any())).thenReturn(commentDao);
        when(notificationRepository.findByUserIdAndCommentIdAndInfo(anyLong(), anyLong(), anyString())).thenReturn(Optional.of(notificationDao));
        when(notificationRepository.save(any())).thenReturn(notificationDao);


        ResponseEntity<Object> response = service.likeComment(anyLong(), userDao);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());


    }

    @Test
    void likeComment_Error_Test() {


        CommentLikeDao commentLikeDao = CommentLikeDao.builder()
                .id(1l)
                .isLike(true)
                .build();

        UserDao userDao = UserDao.builder()
                .id(1l)
                .build();
        CommentDao commentDao = CommentDao.builder()
                .id(1l)
                .user(userDao)
                .build();
        NotificationDao notificationDao = NotificationDao.builder()
                .id(1l)
                .build();

        when(commentRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        when(commentLikeRepository.findByUserIdAndCommentId(anyLong(), anyLong())).thenReturn(Optional.of(commentLikeDao));
        when(commentLikeRepository.save(any())).thenReturn(commentLikeDao);
        when(commentLikeRepository.countLikes(anyLong())).thenReturn(1);
        when(commentRepository.save(any())).thenReturn(commentDao);
        when(notificationRepository.save(any())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> service.likeComment(anyLong(), userDao));

    }

    @Test
    void getlikebyidcomment_Failed_Test() {
        CommentDao commentDao = CommentDao.builder()
                .id(1l)
                .build();

        CommentLikeDao commentLikeDao = CommentLikeDao.builder()
                .id(1l)
                .build();


        when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Object> response = service.getLikeByIdComment(anyLong());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }



    @Test
    void getlikebyidcomment_Error_Test() {

        when(commentRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> service.getLikeByIdComment(1L));

    }



}