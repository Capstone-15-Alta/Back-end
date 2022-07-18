package com.capstone15.alterra.service;

import com.capstone15.alterra.domain.dao.*;
import com.capstone15.alterra.domain.dto.CommentDto;
import com.capstone15.alterra.domain.dto.UserDto;
import com.capstone15.alterra.repository.CommentRepository;
import com.capstone15.alterra.repository.NotificationRepository;
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
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CommentService.class)
public class CommentServiceTest {

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ThreadRepository threadRepository;

    @MockBean
    private NotificationRepository notificationRepository;

    @MockBean
    private ModelMapper mapper;

    @Autowired
    private CommentService service;


    @Test
    void addcomment_Failed_Test() {

        CommentDao commentDao = CommentDao.builder()
                .id(1l)
                .build();
        CommentDto commentDto = CommentDto.builder()
                .id(1l)
                .build();
        UserDao userDao = UserDao.builder()
                .id(1l)
                .build();
        ThreadDao threadDao = ThreadDao.builder()
                .id(1l)
                .build();

        when(threadRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Object> response = service.addComment(commentDto, userDao);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());

    }

    @Test
    void addcomment_Success_Test() {


        CommentDto commentDto = CommentDto.builder()
                .id(1l)
                .threadId(1l)
                .userId(12l)
                .comment("like")
                .build();
        CommentLikeDao commentLikeDao = CommentLikeDao.builder()
                .id(1l)
                .isLike(true)
                .build();
        UserDao userDao = UserDao.builder()
                .id(1l)
                .build();
        ThreadDao threadDao = ThreadDao.builder()
                .id(1l)
                .user(userDao)
                .build();
        CommentDao commentDao = CommentDao.builder()
                .id(1l)
                .thread(threadDao)
                .user(userDao)
                .commentLikeDaoList(List.of(commentLikeDao))
                .build();
        NotificationDao notificationDao = NotificationDao.builder()
                .id(1l)
                .build();
        when(threadRepository.findById(anyLong())).thenReturn(Optional.of(threadDao));
        when(mapper.map(any(), eq(CommentDao.class))).thenReturn(commentDao);
        when(commentRepository.save(any())).thenReturn(commentDao);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userDao));
        when(commentRepository.countComments(anyLong())).thenReturn(12);
        when(userRepository.save(any())).thenReturn(userDao);
        when(notificationRepository.save(any())).thenReturn(notificationDao);

        ResponseEntity<Object> response = service.addComment(commentDto, userDao);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());

    }


    @Test
    void addcomment_Error_Test() {


        CommentDto commentDto = CommentDto.builder()
                .id(1l)
                .threadId(1l)
                .userId(12l)
                .comment("like")
                .build();
        CommentLikeDao commentLikeDao = CommentLikeDao.builder()
                .id(1l)
                .isLike(true)
                .build();
        UserDao userDao = UserDao.builder()
                .id(1l)
                .build();
        ThreadDao threadDao = ThreadDao.builder()
                .id(1l)
                .user(userDao)
                .build();
        CommentDao commentDao = CommentDao.builder()
                .id(1l)
                .thread(threadDao)
                .user(userDao)
                .commentLikeDaoList(List.of(commentLikeDao))
                .build();
        NotificationDao notificationDao = NotificationDao.builder()
                .id(1l)
                .build();

        when(threadRepository.findById(anyLong())).thenReturn(Optional.of(threadDao));
        when(mapper.map(any(), eq(CommentDao.class))).thenReturn(commentDao);
        when(commentRepository.save(any())).thenThrow(NullPointerException.class);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userDao));
        when(commentRepository.countComments(anyLong())).thenReturn(12);
        when(userRepository.save(any())).thenReturn(userDao);
        when(notificationRepository.save(any())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> service.addComment(commentDto, userDao));

    }

    @Test
    void getCommentByIdThread_Failed_Test() {
        ThreadDao threadDao = ThreadDao.builder()
                .id(1l)
                .build();

        when(threadRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> response = service.getCommentByIdThread(anyLong());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());

    }

    @Test
    void getCommentByIdThread_Succes_Test() {
        CommentDao commentDao = CommentDao.builder()
                .id(1l)
                .build();

        ThreadDao threadDao = ThreadDao.builder()
                .id(1l)
                .comments(List.of(commentDao))
                .build();

        when(threadRepository.findById(anyLong())).thenReturn(Optional.of(threadDao));
        ResponseEntity<Object> response = service.getCommentByIdThread(anyLong());
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }


    @Test
    void getCommentByIdThread_Exception_Test() {
        CommentDao commentDao = CommentDao.builder()
                .id(1l)
                .build();

        ThreadDao threadDao = ThreadDao.builder()
                .id(1l)
                .comments(List.of(commentDao))
                .build();

        when(threadRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> service.getCommentByIdThread(anyLong()));
    }

    @Test
    void getCommentByIdUser_Failed_Test() {
        ThreadDao threadDao = ThreadDao.builder()
                .id(1l)
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> response = service.getCommentByIdUser(anyLong());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }


    @Test
    void getCommentByIdUser_Success_Test() {


        CommentDao commentDao = CommentDao.builder()
                .id(1l)
                .build();
        UserDao userDao = UserDao.builder()
                .id(1l)
                .comments(List.of(commentDao))
                .build();
        ThreadDao threadDao = ThreadDao.builder()
                .id(1l)
                .comments(List.of(commentDao))
                .build();
        CommentDto commentDto = CommentDto.builder()
                .id(1l)
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userDao));
        ResponseEntity<Object> response = service.getCommentByIdUser(anyLong());
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }

    @Test
    void getCommentByIdUser_Exception_Test() {

        CommentDao commentDao = CommentDao.builder()
                .id(1l)
                .build();
        ThreadDao threadDao = ThreadDao.builder()
                .id(1l)
                .comments(List.of(commentDao))
                .build();
        CommentDto commentDto = CommentDto.builder()
                .id(1l)
                .build();

        when(userRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> service.getCommentByIdUser(anyLong()));
    }

    @Test
    void deleteComment_Failed_Test() {

        CommentDao commentDao = CommentDao.builder()
                .id(1l)
                .build();
        UserDao userDao = UserDao.builder()
                .id(1l)
                .roles("USER")
                .build();
        when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> response = service.deleteComment(anyLong(), userDao);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }


    @Test
    void deleteComment_Error_Test() {

        UserDao userDao = UserDao.builder()
                .id(1l)
                .roles("")
                .build();
        UserDao userDao1 = UserDao.builder()
                .id(2l)
                .roles("USER")
                .build();
        CommentDao commentDao = CommentDao.builder()
                .id(1l)
                .user(userDao)
                .build();
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(commentDao));
        ResponseEntity<Object> response = service.deleteComment(anyLong(), userDao1);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());
    }

    @Test
    void deleteComment_Succes_Test() {

        UserDao userDao = UserDao.builder()
                .id(1l)
                .roles("USER")
                .build();
        CommentDao commentDao = CommentDao.builder()
                .id(1l)
                .user(userDao)
                .build();
        NotificationDao notificationDao = NotificationDao.builder()
                .id(1l)
                .isRead(true)
                .build();
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(commentDao));
        doNothing().when(commentRepository).deleteById(anyLong());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userDao));
        when(commentRepository.countComments(anyLong())).thenReturn(12);
        when(userRepository.save(any())).thenReturn(userDao);
        ResponseEntity<Object> response = service.deleteComment(anyLong(), userDao);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }

    @Test
    void updatecomment_Failed_Test() {

        CommentDao commentDao = CommentDao.builder()
                .id(1l)
                .build();
        CommentDto commentDto = CommentDto.builder()
                .id(1l)
                .build();
        UserDao userDao = UserDao.builder()
                .id(1l)
                .build();
        when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> response = service.updateComment(anyLong(), commentDto, userDao);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }


    @Test
    void updatecomment_Error_Test() {


        UserDao userDao = UserDao.builder()
                .id(1l)
                .roles("USER")
                .build();
        UserDao userDao1 = UserDao.builder()
                .id(2l)
                .roles("USER")
                .build();
        CommentDao commentDao = CommentDao.builder()
                .id(1l)
                .user(userDao)
                .build();
        CommentDto commentDto = CommentDto.builder()
                .id(1l)
                .userId(2l)
                .comment("bagus")
                .threadId(1l)
                .build();

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(commentDao));
        ResponseEntity<Object> response = service.updateComment(anyLong(), commentDto, userDao1);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());


    }


    @Test
    void updatecomment_Success_Test() {
        UserDao userDao = UserDao.builder()
                .id(1l)
                .build();

        CommentDao commentDao = CommentDao.builder()
                .id(1l)
                .user(userDao)
                .build();

        CommentDto commentDto = CommentDto.builder()
                .id(1l)
                .userId(2l)
                .comment("bagus")
                .threadId(1l)
                .build();

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(commentDao));
        when(commentRepository.save(any())).thenReturn(commentDao);

        ResponseEntity<Object> response = service.updateComment(anyLong(), commentDto, userDao);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());

    }

    @Test
    void updatecomment_Exception_Test() {
        UserDao userDao = UserDao.builder()
                .id(1l)
                .build();

        CommentDao commentDao = CommentDao.builder()
                .id(1l)
                .user(userDao)
                .build();

        CommentDto commentDto = CommentDto.builder()
                .id(1l)
                .userId(2l)
                .comment("bagus")
                .threadId(1l)
                .build();

        when(commentRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> service.updateComment(anyLong(), commentDto, userDao));
    }
}



