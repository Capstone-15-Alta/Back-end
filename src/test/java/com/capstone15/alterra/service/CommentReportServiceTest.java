package com.capstone15.alterra.service;

import com.capstone15.alterra.constant.AppConstant;
import com.capstone15.alterra.domain.common.ApiResponse;
import com.capstone15.alterra.domain.dao.*;
import com.capstone15.alterra.domain.dto.CommentReportDto;
import com.capstone15.alterra.domain.dto.ThreadReportDto;
import com.capstone15.alterra.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CommentReportService.class)
class CommentReportServiceTest {
    @MockBean
    private CommentReportRepository commentReportRepository;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private NotificationRepository notificationRepository;

    @MockBean
    private ModelMapper mapper;

    @Autowired
    private CommentReportService service;

    @Test
    void addReport_NotFound_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        CommentReportDto commentReportDto = CommentReportDto.builder()
                .id(1L)
                .build();

        when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> response = service.addReport(commentReportDto, userDao);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());

    }

    @Test
    void addReport_Success_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        CommentReportDao commentReportDao = CommentReportDao.builder()
                .id(1L)
                .user(userDao)
                .build();

        CommentReportDto commentReportDto = CommentReportDto.builder()
                .id(1L)
                .userId(1L)
                .report("user abuse")
                .reportTime(LocalDateTime.now())
                .commentId(1L)
                .build();

        CommentDao commentDao = CommentDao.builder()
                .id(1L)
                .user(userDao)
                .build();

        NotificationDao notificationDao = NotificationDao.builder()
                .id(1L).
                build();

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(commentDao));
        when(mapper.map(any(),eq(CommentReportDao.class))).thenReturn(commentReportDao);
        when(commentReportRepository.save(any())).thenReturn(commentReportDao);
        when(notificationRepository.save(any())).thenReturn(notificationDao);

        ResponseEntity<Object> response = service.addReport(commentReportDto, userDao);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());

    }

    @Test
    void addReport_SuccessUserNotEquals_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        CommentReportDao commentReportDao = CommentReportDao.builder()
                .id(1L)
                .user(userDao)
                .build();

        CommentReportDto commentReportDto = CommentReportDto.builder()
                .id(1L)
                .userId(1L)
                .report("user abuse")
                .reportTime(LocalDateTime.now())
                .commentId(1L)
                .build();

        CommentDao commentDao = CommentDao.builder()
                .id(1L)
                .user(UserDao.builder().id(2L).build())
                .build();

        NotificationDao notificationDao = NotificationDao.builder()
                .id(1L).
                build();

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(commentDao));
        when(mapper.map(any(),eq(CommentReportDao.class))).thenReturn(commentReportDao);
        when(commentReportRepository.save(any())).thenReturn(commentReportDao);
        when(notificationRepository.save(any())).thenReturn(notificationDao);

        ResponseEntity<Object> response = service.addReport(commentReportDto, userDao);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());

    }

    @Test
    void addReport_Exception_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        CommentReportDto commentReportDto = CommentReportDto.builder()
                .id(1L)
                .userId(1L)
                .report("user abuse")
                .reportTime(LocalDateTime.now())
                .commentId(1L)
                .build();

        when(commentRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> service.addReport(commentReportDto,userDao));
    }

    @Test
    void getReportByIdCommentIsEmpty_Test(){
        when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> response= service.getReportByIdComment(anyLong());
        assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
    }

    @Test
    void getReportByIdComment_Success_Test(){
        CommentReportDao commentReportDao = CommentReportDao.builder()
                .id(1L)
                .build();

        CommentDao commentDao = CommentDao.builder()
                .id(1L)
                .commentReports(List.of(commentReportDao))
                .build();

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(commentDao));

        ResponseEntity<Object> response= service.getReportByIdComment(anyLong());
        assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());
    }

    @Test
    void getReportByIdComment_Exception_Test(){
        when(commentRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class,() -> service.getReportByIdComment(anyLong()));
    }

    @Test
    void getReportCommentByIdSuccess_Test() {
        CommentReportDao commentReportDao = CommentReportDao.builder()
                .id(1L)
                .build();

        when(commentReportRepository.findById(any())).thenReturn(Optional.of(commentReportDao));

        ResponseEntity<Object> response = service.getReportCommentById(any());
        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());

    }


    @Test
    void getReportCommentByIdIsEmpty_Test() {
        when(commentReportRepository.findById(any())).thenReturn(Optional.empty());

        ResponseEntity<Object> response = service.getReportCommentById(any());
        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.NOT_FOUND, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void getReportCommentByIdException_Test() {
        when(commentReportRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () ->  service.getReportCommentById(1L));
    }

    @Test
    void deleteReport_NotFound_Test(){
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        when(commentReportRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> response = service.deleteReport(anyLong(),userDao);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    void deleteReportDenied_Test(){
        UserDao userDao = UserDao.builder()
                .id(1L)
                .roles("MODERATOR")
                .build();

        CommentReportDao commentReportDao = CommentReportDao.builder()
                .id(1L)
                .user(UserDao.builder().id(2L).build())
                .build();

        when(commentReportRepository.findById(anyLong())).thenReturn(Optional.of(commentReportDao));
        ResponseEntity<Object> response = service.deleteReport(anyLong(),userDao);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());
    }

    @Test
    void deleteReport_Success_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        CommentReportDao commentReportDao = CommentReportDao.builder()
                .id(1L)
                .user(userDao)
                .build();

        when(commentReportRepository.findById(anyLong())).thenReturn(Optional.of(commentReportDao));
        doNothing().when(commentReportRepository).deleteById(anyLong());

        ResponseEntity<Object> response = service.deleteReport(anyLong(), userDao);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }

    @Test
    void deleteReport_Exception_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .roles("MODERATOR")
                .build();

        when(commentReportRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () ->  service.deleteReport(anyLong(),userDao));
    }

    @Test
    void updateReport_NotFound_Test(){
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        CommentReportDto commentReportDto = CommentReportDto.builder()
                .id(1L)
                .build();

        when(commentReportRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> response = service.updateReport(anyLong(),commentReportDto,userDao);
        assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
    }

    @Test
    void updateReport_Error_Test(){
        UserDao userDao = UserDao.builder()
                .id(1L)
                .roles("MODERATOR")
                .build();

        CommentReportDao commentReportDao = CommentReportDao.builder()
                .id(1L)
                .user(UserDao.builder().id(2L).build())
                .report("")
                .build();

        CommentReportDto commentReportDto = CommentReportDto.builder()
                .id(1L)
                .userId(2L)
                .report("user abuse")
                .reportTime(LocalDateTime.now())
                .commentId(1L)
                .build();

        when(commentReportRepository.findById(anyLong())).thenReturn(Optional.of(commentReportDao));
        ResponseEntity<Object> response = service.updateReport(anyLong(),commentReportDto,userDao);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(),response.getStatusCodeValue());
    }

    @Test
    void updateReport_Success_Test(){
        UserDao userDao = UserDao.builder()
                .id(1L)
                .roles("MODERATOR")
                .build();

        CommentReportDao commentReportDao = CommentReportDao.builder()
                .id(1L)
                .user(userDao)
                .report("")
                .build();

        CommentReportDto commentReportDto = CommentReportDto.builder()
                .id(1L)
                .userId(2L)
                .report("user abuse")
                .reportTime(LocalDateTime.now())
                .commentId(1L)
                .build();

        when(commentReportRepository.findById(anyLong())).thenReturn(Optional.of(commentReportDao));
        ResponseEntity<Object> response = service.updateReport(anyLong(),commentReportDto,userDao);
        assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());

    }

    @Test
    void updateReport_Exception_Test(){
        UserDao userDao = UserDao.builder()
                .id(1L)
                .roles("")
                .build();

        CommentReportDto commentReportDto = CommentReportDto.builder()
                .id(1L)
                .userId(2L)
                .report("user abuse")
                .reportTime(LocalDateTime.now())
                .commentId(1L)
                .build();

        when(commentReportRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> service.updateReport(anyLong(),commentReportDto,userDao));

    }

    @Test
    void getAllReportIsEmpty_Test() {
        Page<CommentReportDao> commentReportDaos = new PageImpl<>(List.of());
        when(commentReportRepository.findAll((Pageable) any())).thenReturn(commentReportDaos);

        ResponseEntity<Object> response = service.getAllReport(any());
        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());
        assertEquals(AppConstant.Message.NOT_FOUND, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void getAllReportSuccess_Test() {
        CommentReportDao commentReportDao = CommentReportDao.builder()
                .id(1L)
                .build();

        Page<CommentReportDao> commentReportDaos = new PageImpl<>(List.of(commentReportDao));
        when(commentReportRepository.findAll((Pageable) any())).thenReturn(commentReportDaos);

        ResponseEntity<Object> response = service.getAllReport(any());
        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void getAllReportException_Test() {
        when(commentReportRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () ->  service.getAllReport(any()));
    }

}