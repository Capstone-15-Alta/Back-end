package com.capstone15.alterra.service;

import com.capstone15.alterra.constant.AppConstant;
import com.capstone15.alterra.domain.common.ApiResponse;
import com.capstone15.alterra.domain.dao.NotificationDao;
import com.capstone15.alterra.domain.dao.ThreadDao;
import com.capstone15.alterra.domain.dao.ThreadReportDao;
import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dto.ThreadDto;
import com.capstone15.alterra.domain.dto.ThreadReportDto;
import com.capstone15.alterra.repository.NotificationRepository;
import com.capstone15.alterra.repository.ThreadReportRepository;
import com.capstone15.alterra.repository.ThreadRepository;
import com.capstone15.alterra.repository.UserRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ThreadReportService.class)
public class ThreadReportServiceTest {
    @MockBean
    private ThreadReportRepository threadReportRepository;

    @MockBean
    private ThreadRepository threadRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private NotificationRepository notificationRepository;

    @MockBean
    private ModelMapper mapper;

    @Autowired
    private ThreadReportService service;

    @Test
    void addReport_Failed_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        ThreadReportDto threadReportDto = ThreadReportDto.builder()
                .id(1L)
                .build();

        when(threadRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Object> response = service.addReport(threadReportDto, userDao);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());

    }

    @Test
    void addReport_Success_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        ThreadReportDao threadReportDao = ThreadReportDao.builder()
                .id(1L)
                .user(userDao)
                .build();

        ThreadReportDto threadReportDto = ThreadReportDto.builder()
                .id(1L)
                .userId(1L)
                .report("unsur kekerasan")
                .reportTime(LocalDateTime.now())
                .threadId(1L)
                .build();

        ThreadDao threadDao = ThreadDao.builder()
                .id(1L)
                .user(userDao)
                .build();

        NotificationDao notificationDao = NotificationDao.builder()
                .id(1L).
                build();

        when(threadRepository.findById(anyLong())).thenReturn(Optional.of(threadDao));
        when(mapper.map(any(),eq(ThreadReportDao.class))).thenReturn(threadReportDao);
        when(threadReportRepository.save(any())).thenReturn(threadReportDao);
        when(notificationRepository.save(any())).thenReturn(notificationDao);

        ResponseEntity<Object> response = service.addReport(threadReportDto, userDao);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());

    }

    @Test
    void addReportNotEquals_Success_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        ThreadReportDao threadReportDao = ThreadReportDao.builder()
                .id(1L)
                .user(userDao)
                .build();

        ThreadReportDto threadReportDto = ThreadReportDto.builder()
                .id(1L)
                .userId(1L)
                .report("unsur kekerasan")
                .reportTime(LocalDateTime.now())
                .threadId(1L)
                .build();

        ThreadDao threadDao = ThreadDao.builder()
                .id(1L)
                .user(UserDao.builder().id(2L).build())
                .build();

        NotificationDao notificationDao = NotificationDao.builder()
                .id(1L).
                build();

        when(threadRepository.findById(anyLong())).thenReturn(Optional.of(threadDao));
        when(mapper.map(any(),eq(ThreadReportDao.class))).thenReturn(threadReportDao);
        when(threadReportRepository.save(any())).thenReturn(threadReportDao);
        when(notificationRepository.save(any())).thenReturn(notificationDao);

        ResponseEntity<Object> response = service.addReport(threadReportDto, userDao);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());

    }

    @Test
    void addReport_Exception_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        ThreadReportDto threadReportDto = ThreadReportDto.builder()
                .id(1L)
                .userId(1L)
                .report("unsur kekerasan")
                .reportTime(LocalDateTime.now())
                .threadId(1L)
                .build();

        when(threadRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> service.addReport(threadReportDto,userDao));
    }

    @Test
    void getReportByIdThread_Failed_Test(){
        when(threadRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> response= service.getReportByIdThread(anyLong());
        assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
    }

    @Test
    void getReportByIdThread_Success_Test(){
        ThreadReportDao threadReportDao = ThreadReportDao.builder()
                .id(1L)
                .build();

        ThreadDao threadDao = ThreadDao.builder()
                .id(1L)
                .threadReports(List.of(threadReportDao))
                .build();

        when(threadRepository.findById(anyLong())).thenReturn(Optional.of(threadDao));
        ResponseEntity<Object> response= service.getReportByIdThread(anyLong());
        assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());
    }

    @Test
    void getReportByIdThread_Exception_Test(){
        when(threadRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class,() -> service.getReportByIdThread(anyLong()));
    }

    @Test
    void updateReport_Failed_Test(){
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        ThreadReportDto threadReportDto = ThreadReportDto.builder()
                .id(1L)
                .build();

        when(threadReportRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> response = service.updateReport(anyLong(),threadReportDto,userDao);
        assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
    }

    @Test
    void updateReport_Error_Test(){
        UserDao userDao = UserDao.builder()
                .id(1L)
                .roles("MODERATOR")
                .build();

        ThreadReportDao threadReportDao = ThreadReportDao.builder()
                .id(2L)
                .user(UserDao.builder().id(2L).build())
                .report("")
                .build();

        ThreadReportDto threadReportDto = ThreadReportDto.builder()
                .id(1L)
                .userId(2L)
                .report("unsur kekerasan")
                .reportTime(LocalDateTime.now())
                .threadId(1L)
                .build();

        when(threadReportRepository.findById(anyLong())).thenReturn(Optional.of(threadReportDao));
        ResponseEntity<Object> response = service.updateReport(anyLong(),threadReportDto,userDao);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(),response.getStatusCodeValue());
    }

    @Test
    void updateReport_Success_Test(){
        UserDao userDao = UserDao.builder()
                .id(1L)
                .roles("MODERATOR")
                .build();

        ThreadReportDao threadReportDao = ThreadReportDao.builder()
                .id(1L)
                .user(userDao)
                .build();

        ThreadReportDto threadReportDto = ThreadReportDto.builder()
                .id(1L)
                .userId(1L)
                .report("unsur kekerasan")
                .reportTime(LocalDateTime.now())
                .threadId(1L)
                .build();

        when(threadReportRepository.findById(anyLong())).thenReturn(Optional.of(threadReportDao));
        ResponseEntity<Object> response = service.updateReport(anyLong(),threadReportDto,userDao);
        assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());

}

    @Test
    void updateReport_Exception_Test(){
        UserDao userDao = UserDao.builder()
                .id(1L)
                .roles("")
                .build();

        ThreadReportDto threadReportDto = ThreadReportDto.builder()
                .id(1L)
                .userId(1L)
                .report("unsur kekerasan")
                .reportTime(LocalDateTime.now())
                .threadId(1L)
                .build();

        when(threadReportRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> service.updateReport(anyLong(),threadReportDto,userDao));

    }

    @Test
    void deleteReport_Failed_Test(){
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        when(threadReportRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> response = service.deleteReport(anyLong(),userDao);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    void deleteReport_Error_Test(){
        UserDao userDao = UserDao.builder()
                .id(1L)
                .roles("MODERATOR")
                .build();

        UserDao userDao1 = UserDao.builder()
                .id(2L)
                .build();

        ThreadReportDao threadReportDao = ThreadReportDao.builder()
                .id(1L)
                .user(userDao1)
                .build();

        when( threadReportRepository.findById(anyLong())).thenReturn(Optional.of(threadReportDao));
        ResponseEntity<Object> response = service.deleteReport(anyLong(),userDao);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());
    }

    @Test
    void deleteReport_Success_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        ThreadReportDao threadReportDao = ThreadReportDao.builder()
                .id(1L)
                .user(userDao)
                .build();

        when(threadReportRepository.findById(anyLong())).thenReturn(Optional.of(threadReportDao));
        doNothing().when(threadReportRepository).deleteById(anyLong());

        ResponseEntity<Object> response = service.deleteReport(anyLong(), userDao);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }

    @Test
    void deleteReport_Exception_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .roles("MODERATOR")
                .build();

        when(threadReportRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () ->  service.deleteReport(anyLong(),userDao));
    }

    @Test
    void getAllReportIsEmpty_Test() {
        Page<ThreadReportDao> threadReportDaos = new PageImpl<>(List.of());
        when(threadReportRepository.findAll((Pageable) any())).thenReturn(threadReportDaos);

        ResponseEntity<Object> response = service.getAllReport(any());
        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.NOT_FOUND, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void getAllReportSuccess_Test() {
        ThreadReportDao threadReportDao = ThreadReportDao.builder()
                .id(1L)
                .build();

        Page<ThreadReportDao> threadReportDaos = new PageImpl<>(List.of(threadReportDao));
        when(threadReportRepository.findAll((Pageable) any())).thenReturn(threadReportDaos);

        ResponseEntity<Object> response = service.getAllReport(any());
        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void getAllReportException_Test() {
        when(threadReportRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () ->  service.getAllReport(any()));
    }

    @Test
    void getReportThreadByIdSuccess_Test() {
        ThreadReportDao threadReportDao = ThreadReportDao.builder()
                .id(1L)
                .build();

        when(threadReportRepository.findById(any())).thenReturn(Optional.of(threadReportDao));

        ResponseEntity<Object> response = service.getReportThreadById(any());
        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void getReportThreadByIdIsEmpty_Test() {
        when(threadReportRepository.findById(any())).thenReturn(Optional.empty());

        ResponseEntity<Object> response = service.getReportThreadById(any());
        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.NOT_FOUND, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void getReportThreadByIdException_Test() {
        when(threadReportRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () ->  service.getReportThreadById(1L));
    }
}