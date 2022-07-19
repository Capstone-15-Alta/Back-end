package com.capstone15.alterra.service;

import com.capstone15.alterra.domain.dao.*;
import com.capstone15.alterra.domain.dto.CommentDto;
import com.capstone15.alterra.domain.dto.SubCommentDto;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SubCommentService.class)
class SubCommentServiceTest {

    @MockBean
    private SubCommentRepository subCommentRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private NotificationRepository notificationRepository;

    @MockBean
    private ModelMapper mapper;

    @Autowired
    private SubCommentService service;

    @Test
    void addCommentSuccess_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        CommentDao commentDao = CommentDao.builder()
                .id(1L)
                .user(UserDao.builder().id(2L).build())
                .build();

        SubCommentDao subCommentDao = SubCommentDao.builder()
                .id(1L)
                .build();

        SubCommentDto subCommentDto = SubCommentDto.builder()
                .id(1L)
                .commentId(1L)
                .subComment("pertamax")
                .build();

        NotificationDao notificationDao = NotificationDao.builder()
                .id(1L)
                .build();

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(commentDao));
        when(mapper.map(any(), eq(SubCommentDao.class))).thenReturn(subCommentDao);
        when(mapper.map(any(), eq(SubCommentDto.class))).thenReturn(subCommentDto);
        when(subCommentRepository.save(any())).thenReturn(subCommentDao);
        when(notificationRepository.save(any())).thenReturn(notificationDao);

        ResponseEntity<Object> response = service.addSubComment(subCommentDto,userDao);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());

    }

    @Test
    void addComment_NotFound_Test() {
        SubCommentDto subCommentDto = SubCommentDto.builder()
                .id(1L)
                .build();

        when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> response = service.addSubComment(subCommentDto, any());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());

    }

    @Test
    void addCommentException_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        SubCommentDto subCommentDto = SubCommentDto.builder()
                .id(1L)
                .build();

        when(commentRepository.findById(any())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> service.addSubComment(subCommentDto, userDao));
    }

    @Test
    void getSubCommentByIdComment_NotFound_Test() {
        when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> response = service.getSubCommentByIdComment(anyLong());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());

    }

    @Test
    void getSubCommentByIdComment_Success_Test() {
        SubCommentDao subCommentDao = SubCommentDao.builder()
                .id(1L)
                .build();

        CommentDao commentDao = CommentDao.builder()
                .id(1L)
                .subComments(List.of(subCommentDao))
                .build();

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(commentDao));
        ResponseEntity<Object> response = service.getSubCommentByIdComment(anyLong());
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }

    @Test
    void getSubCommentByIdComment_Exception_Test() {
        when(commentRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> service.getSubCommentByIdComment(anyLong()));
    }

    @Test
    void getSubCommentById_NotFound_Test() {
        when(subCommentRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> response = service.getSubCommentById(anyLong());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    void getSubCommentById_Success_Test() {
        SubCommentDao subCommentDao = SubCommentDao.builder()
                .id(1L)
                .build();

        when(subCommentRepository.findById(anyLong())).thenReturn(Optional.of(subCommentDao));
        ResponseEntity<Object> response = service.getSubCommentById(anyLong());
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }

    @Test
    void getSubCommentById_Exception_Test() {
        when(subCommentRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> service.getSubCommentById(anyLong()));
    }

    @Test
    void deleteComment_NotFound_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .roles("USER")
                .build();

        when(subCommentRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> response = service.deleteSubComment(anyLong(), userDao);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    void deleteComment_Denied_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .roles("")
                .build();

        UserDao userDao1 = UserDao.builder()
                .id(2L)
                .roles("USER")
                .build();

        SubCommentDao subCommentDao = SubCommentDao.builder()
                .id(1L)
                .user(userDao)
                .build();

        when(subCommentRepository.findById(anyLong())).thenReturn(Optional.of(subCommentDao));
        ResponseEntity<Object> response = service.deleteSubComment(anyLong(), userDao1);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());
    }

    @Test
    void deleteComment_Success_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .roles("USER")
                .build();

        SubCommentDao subCommentDao = SubCommentDao.builder()
                .id(1L)
                .user(userDao)
                .build();

        when(subCommentRepository.findById(anyLong())).thenReturn(Optional.of(subCommentDao));
        doNothing().when(commentRepository).deleteById(anyLong());

        ResponseEntity<Object> response = service.deleteSubComment(anyLong(), userDao);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }

    @Test
    void deleteCommentException_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        when(subCommentRepository.findById(any())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> service.deleteSubComment(any(), userDao));
    }

    @Test
    void updateSubComment_NotFound_Test() {
        SubCommentDto subCommentDto = SubCommentDto.builder()
                .id(1L)
                .build();

        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        when(subCommentRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> response = service.updateSubComment(anyLong(), subCommentDto, userDao);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    void updateComment_Denied_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .roles("USER")
                .build();

        SubCommentDao subCommentDao = SubCommentDao.builder()
                .id(1L)
                .user(UserDao.builder().id(2L).build())
                .build();

        SubCommentDto subCommentDto = SubCommentDto.builder()
                .id(1L)
                .commentId(1L)
                .subComment("pertamax")
                .build();

        when(subCommentRepository.findById(anyLong())).thenReturn(Optional.of(subCommentDao));
        ResponseEntity<Object> response = service.updateSubComment(anyLong(), subCommentDto, userDao);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());
    }

    @Test
    void updateComment_Success_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        SubCommentDao subCommentDao = SubCommentDao.builder()
                .id(1L)
                .user(userDao)
                .build();

        SubCommentDto subCommentDto = SubCommentDto.builder()
                .id(1L)
                .commentId(1L)
                .subComment("pertamax")
                .build();

        when(subCommentRepository.findById(anyLong())).thenReturn(Optional.of(subCommentDao));
        ResponseEntity<Object> response = service.updateSubComment(anyLong(), subCommentDto, userDao);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());

    }

    @Test
    void updateComment_Exception_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        SubCommentDto subCommentDto = SubCommentDto.builder()
                .id(1L)
                .commentId(1L)
                .subComment("pertamax")
                .build();

        when(subCommentRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> service.updateSubComment(anyLong(), subCommentDto, userDao));
    }

}