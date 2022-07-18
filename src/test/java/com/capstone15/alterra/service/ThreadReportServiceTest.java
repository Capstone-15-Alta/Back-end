package com.capstone15.alterra.service;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
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
                .id(1l)
                .build();

        ThreadDao threadDao = ThreadDao.builder()
                .id(1l)
                .build();
        ThreadReportDao threadReportDao = ThreadReportDao.builder()
                .id(1l)
                .build();
        ThreadReportDto threadReportDto = ThreadReportDto.builder()
                .id(1l)
                .build();

        when(threadRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> response = service.addReport(threadReportDto, userDao);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());

    }

    @Test
    void addReport_Success_Test() {

        UserDao userDao = UserDao.builder()
                .id(1l)
                .build();
        ThreadReportDao threadReportDao = ThreadReportDao.builder()
                .id(1l)
                .user(userDao)
                .build();

        ThreadReportDto threadReportDto = ThreadReportDto.builder()
                .id(1l)
                .userId(1l)
                .report("unsur kekerasan")
                .reportTime(LocalDateTime.now())
                .threadId(1l)
                .build();
        ThreadDao threadDao = ThreadDao.builder()
                .id(1l)
                .user(userDao)
                .build();
        NotificationDao notificationDao = NotificationDao.builder()
                .id(1l).
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
                .id(1l)
                .build();
        ThreadReportDao threadReportDao = ThreadReportDao.builder()
                .id(1l)
                .user(userDao)
                .build();

        ThreadReportDto threadReportDto = ThreadReportDto.builder()
                .id(1l)
                .userId(1l)
                .report("unsur kekerasan")
                .reportTime(LocalDateTime.now())
                .threadId(1l)
                .build();
        ThreadDao threadDao = ThreadDao.builder()
                .id(1l)
                .user(userDao)
                .build();
        NotificationDao notificationDao = NotificationDao.builder()
                .id(1l).
                build();

        when(threadRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        when(mapper.map(any(),eq(ThreadReportDao.class))).thenReturn(threadReportDao);
        when(threadReportRepository.save(any())).thenReturn(threadReportDao);
        when(notificationRepository.save(any())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> service.addReport(threadReportDto,userDao));


    }
    @Test
    void getReportByIdThread_Failed_Test(){
        ThreadDao threadDao = ThreadDao.builder()
                .id(1l)
                .build();
        UserDao userDao = UserDao.builder()
                .id(1l)
                .build();
        when(threadRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> response= service.getReportByIdThread(anyLong());
        assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
    }


    @Test
    void getReportByIdThread_Success_Test(){

        ThreadReportDao threadReportDao = ThreadReportDao.builder()
                .id(1l)
                .build();
        UserDao userDao = UserDao.builder()
                .id(1l)
                .build();

        ThreadDao threadDao = ThreadDao.builder()
                .id(1l)
                .threadReports(List.of(threadReportDao))
                .build();


        when(threadRepository.findById(anyLong())).thenReturn(Optional.of(threadDao));
        ResponseEntity<Object> response= service.getReportByIdThread(anyLong());
        assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());
    }

    @Test
    void getReportByIdThread_Exception_Test(){
        ThreadReportDao threadReportDao = ThreadReportDao.builder()
                .id(1l)
                .build();
        UserDao userDao = UserDao.builder()
                .id(1l)
                .build();
        ThreadDao threadDao = ThreadDao.builder()
                .id(1l)
                .build();

        when(threadRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class,() -> service.getReportByIdThread(anyLong()));
    }

    @Test
    void updateReport_Failed_Test(){
        UserDao userDao = UserDao.builder()
                .id(1l)
                .build();
        ThreadDao threadDao = ThreadDao.builder()
                .id(1l)
                .build();
        ThreadReportDao threadReportDao = ThreadReportDao.builder()
                .id(1l)
                .build();
        ThreadReportDto threadReportDto = ThreadReportDto.builder()
                .id(1l)
                .build();

        when(threadReportRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> response = service.updateReport(anyLong(),threadReportDto,userDao);
        assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
    }

    @Test
    void updateReport_Error_Test(){
        UserDao userDao = UserDao.builder()
                .id(1l)
                .roles("MODERATOR")
                .build();
        ThreadDao threadDao = ThreadDao.builder()
                .id(1l)
                .build();

        UserDao userDao1 = UserDao.builder()
                .id(2l)
                .roles("")
                .build();
        ThreadReportDao threadReportDao = ThreadReportDao.builder()
                .id(2l)
                .user(userDao1)
                .report("")
                .build();
        ThreadReportDto threadReportDto = ThreadReportDto.builder()
                .id(1l)
                .userId(2l)
                .report("unsur kekerasan")
                .reportTime(LocalDateTime.now())
                .threadId(1l)
                .build();

        when(threadReportRepository.findById(anyLong())).thenReturn(Optional.of(threadReportDao));
        ResponseEntity<Object> response = service.updateReport(anyLong(),threadReportDto,userDao);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(),response.getStatusCodeValue());
    }
    @Test
    void updateReport_Success_Test(){
        UserDao userDao = UserDao.builder()
                .id(1l)
                .roles("")
                .build();
        ThreadReportDao threadReportDao = ThreadReportDao.builder()
                .id(1l)
                .user(userDao)
                .build();

        ThreadReportDto threadReportDto = ThreadReportDto.builder()
                .id(1l)
                .userId(1l)
                .report("unsur kekerasan")
                .reportTime(LocalDateTime.now())
                .threadId(1l)
                .build();
        ThreadDao threadDao = ThreadDao.builder()
                .id(1l)
                .user(userDao)
                .build();
        NotificationDao notificationDao = NotificationDao.builder()
                .id(1l).
                build();

        when(threadReportRepository.findById(anyLong())).thenReturn(Optional.of(threadReportDao));
        when(threadRepository.save(any())).thenReturn(threadDao);
        ResponseEntity<Object> response = service.updateReport(anyLong(),threadReportDto,userDao);
        assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());

}
    @Test
    void updateReport_Exception_Test(){
        UserDao userDao = UserDao.builder()
                .id(1l)
                .roles("")
                .build();
        ThreadReportDao threadReportDao = ThreadReportDao.builder()
                .id(1l)
                .user(userDao)
                .build();

        ThreadReportDto threadReportDto = ThreadReportDto.builder()
                .id(1l)
                .userId(1l)
                .report("unsur kekerasan")
                .reportTime(LocalDateTime.now())
                .threadId(1l)
                .build();
        ThreadDao threadDao = ThreadDao.builder()
                .id(1l)
                .user(userDao)
                .build();
        NotificationDao notificationDao = NotificationDao.builder()
                .id(1l).
                build();

        when(threadReportRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        when(threadRepository.save(any())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> service.updateReport(anyLong(),threadReportDto,userDao));

    }

    @Test
    void deletereport_Failed_Test(){
        ThreadReportDao threadReportDao = ThreadReportDao.builder()
                .id(1l)
                .build();
        UserDao userDao = UserDao.builder()
                .id(1l)
                .build();
        when( threadReportRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> response = service.deleteReport(anyLong(),userDao);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    void deletereport_Error_Test(){
        UserDao userDao = UserDao.builder()
                .id(1l)
                .roles("MODERATOR")
                .build();
        UserDao userDao1 = UserDao.builder()
                .id(2l)
                .build();
        ThreadReportDao threadReportDao = ThreadReportDao.builder()
                .id(1l)
                .user(userDao1)
                .build();

        when( threadReportRepository.findById(anyLong())).thenReturn(Optional.of(threadReportDao));
        ResponseEntity<Object> response = service.deleteReport(anyLong(),userDao);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());
    }
    @Test
    void deletereport_Success_Test() {
        UserDao userDao = UserDao.builder()
                .id(1l)
                .roles("MODERATOR")
                .build();
        UserDao userDao1 = UserDao.builder()
                .id(2l)
                .build();
        ThreadReportDao threadReportDao = ThreadReportDao.builder()
                .id(1l)
                .user(userDao1)
                .build();

        when(threadReportRepository.findById(anyLong())).thenReturn(Optional.of(threadReportDao));
        doNothing().when(threadReportRepository).deleteById(anyLong());
        ResponseEntity<Object> response = service.deleteReport(anyLong(), userDao1);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }

    @Test
    void deletereport_Exception_Test() {
        UserDao userDao = UserDao.builder()
                .id(1l)
                .roles("MODERATOR")
                .build();
        UserDao userDao1 = UserDao.builder()
                .id(2l)
                .build();
        ThreadReportDao threadReportDao = ThreadReportDao.builder()
                .id(1l)
                .user(userDao1)
                .build();

        when(threadReportRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        doNothing().when(threadReportRepository).deleteById(anyLong());
        assertThrows(Exception.class, () ->  service.deleteReport(anyLong(),userDao));
    }
}