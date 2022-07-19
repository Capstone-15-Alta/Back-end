package com.capstone15.alterra.service;

import com.capstone15.alterra.domain.dao.*;
import com.capstone15.alterra.domain.dto.CommentDto;
import com.capstone15.alterra.domain.dto.ThreadDto;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void addCommentSuccess_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        ThreadDao threadDao = ThreadDao.builder()
                .id(1L)
                .user(UserDao.builder().id(2L).build())
                .build();

        CommentDao commentDao = CommentDao.builder()
                .id(1L)
                .build();

        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .comment("oke")
                .threadId(1L)
                .build();

        NotificationDao notificationDao = NotificationDao.builder()
                .id(1L)
                .build();

        when(threadRepository.findById(anyLong())).thenReturn(Optional.of(threadDao));
        when(mapper.map(any(), eq(CommentDao.class))).thenReturn(commentDao);
        when(mapper.map(any(), eq(CommentDto.class))).thenReturn(commentDto);
        when(commentRepository.save(any())).thenReturn(commentDao);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userDao));
        when(notificationRepository.save(any())).thenReturn(notificationDao);

        ResponseEntity<Object> response = service.addComment(commentDto,userDao);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());

    }

    @Test
    void addCommentException_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .build();

        when(threadRepository.findById(any())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> service.addComment(commentDto, userDao));
    }

    @Test
    void addComment_Failed_Test() {
        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .build();

        when(threadRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> response = service.addComment(commentDto, any());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());

    }

    @Test
    void getCommentByIdThread_Failed_Test() {
        when(threadRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> response = service.getCommentByIdThread(anyLong());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());

    }

    @Test
    void getCommentByIdThread_Success_Test() {
        CommentDao commentDao = CommentDao.builder()
                .id(1L)
                .build();

        ThreadDao threadDao = ThreadDao.builder()
                .id(1L)
                .comments(List.of(commentDao))
                .build();

        when(threadRepository.findById(anyLong())).thenReturn(Optional.of(threadDao));
        ResponseEntity<Object> response = service.getCommentByIdThread(anyLong());
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }

    @Test
    void getCommentByIdThread_Exception_Test() {
        when(threadRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> service.getCommentByIdThread(anyLong()));
    }

    @Test
    void getCommentByIdUser_Failed_Test() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> response = service.getCommentByIdUser(anyLong());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    void getCommentByIdUser_Success_Test() {
        CommentDao commentDao = CommentDao.builder()
                .id(1L)
                .build();

        UserDao userDao = UserDao.builder()
                .id(1L)
                .comments(List.of(commentDao))
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userDao));
        ResponseEntity<Object> response = service.getCommentByIdUser(anyLong());
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }

    @Test
    void getCommentByIdUser_Exception_Test() {
        when(userRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> service.getCommentByIdUser(anyLong()));
    }

    @Test
    void getCommentById_Success_Test() {
        CommentDao commentDao = CommentDao.builder()
                .id(1L)
                .build();

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(commentDao));
        ResponseEntity<Object> response = service.getCommentById(anyLong());
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }

    @Test
    void getCommentById_IsEmpty_Test() {
        when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> response = service.getCommentById(anyLong());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    void getCommentById_Exception_Test() {
        when(commentRepository.findById(any())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> service.getCommentById(any()));
    }

    @Test
    void deleteComment_Failed_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .roles("USER")
                .build();

        when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> response = service.deleteComment(anyLong(), userDao);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    void deleteComment_Error_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .roles("")
                .build();

        UserDao userDao1 = UserDao.builder()
                .id(2L)
                .roles("USER")
                .build();

        CommentDao commentDao = CommentDao.builder()
                .id(1L)
                .user(userDao)
                .build();

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(commentDao));
        ResponseEntity<Object> response = service.deleteComment(anyLong(), userDao1);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());
    }

    @Test
    void deleteComment_Success_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .roles("USER")
                .build();

        CommentDao commentDao = CommentDao.builder()
                .id(1L)
                .user(userDao)
                .build();

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(commentDao));
        doNothing().when(commentRepository).deleteById(anyLong());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userDao));

        ResponseEntity<Object> response = service.deleteComment(anyLong(), userDao);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }

    @Test
    void deleteCommentException_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        when(commentRepository.findById(any())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> service.deleteComment(any(), userDao));
    }

    @Test
    void updateComment_Failed_Test() {
        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .build();

        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> response = service.updateComment(anyLong(), commentDto, userDao);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    void updateComment_Error_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .roles("USER")
                .build();

        CommentDao commentDao = CommentDao.builder()
                .id(1L)
                .user(UserDao.builder().id(2L).build())
                .build();

        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .userId(2L)
                .comment("bagus")
                .threadId(1L)
                .build();

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(commentDao));
        ResponseEntity<Object> response = service.updateComment(anyLong(), commentDto, userDao);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());
    }

    @Test
    void updateComment_Success_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        CommentDao commentDao = CommentDao.builder()
                .id(1L)
                .user(userDao)
                .build();

        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .userId(2L)
                .comment("bagus")
                .threadId(1L)
                .build();

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(commentDao));
        when(commentRepository.save(any())).thenReturn(commentDao);

        ResponseEntity<Object> response = service.updateComment(anyLong(), commentDto, userDao);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());

    }

    @Test
    void updateComment_Exception_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .userId(2L)
                .comment("bagus")
                .threadId(1L)
                .build();

        when(commentRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> service.updateComment(anyLong(), commentDto, userDao));
    }
}



