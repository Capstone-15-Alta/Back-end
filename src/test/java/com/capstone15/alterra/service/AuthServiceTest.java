package com.capstone15.alterra.service;

import com.capstone15.alterra.config.security.JwtTokenProvider;
import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dto.payload.TokenResponse;
import com.capstone15.alterra.domain.dto.payload.UsernamePassword;
import com.capstone15.alterra.domain.dto.payload.UsernamePasswordFGD;
import com.capstone15.alterra.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AuthService.class)

public class AuthServiceTest {
    @MockBean
    private  UserRepository userRepository;

    @MockBean
    private  AuthenticationManager authenticationManager;

    @MockBean
    private  JwtTokenProvider jwtTokenProvider;

    @MockBean
    private  PasswordEncoder passwordEncoder;

    @MockBean
    private ModelMapper mapper;

    @Autowired
    private AuthService service;

    @Test
    void registerSuccess_Test(){
        UsernamePasswordFGD usernamePasswordFGD = UsernamePasswordFGD.builder()
                .id(1L)
                .username("user123")
                .password("Bismillah123")
                .email("email@gmail.com")
                .build();

        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        when(userRepository.save(any())).thenReturn(userDao);
        ResponseEntity<Object> response = service.register(usernamePasswordFGD);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());

    }

    @Test
    void registerUsernameIsNull_Test(){
        UsernamePasswordFGD usernamePasswordFGD = UsernamePasswordFGD.builder()
                .id(1L)
                .password("kikicantik123")
                .build();

        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        when(userRepository.save(any())).thenReturn(userDao);
        ResponseEntity<Object> response = service.register(usernamePasswordFGD);
        assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());

    }

    @Test
    void registerPasswordIsNull_Test(){
        UsernamePasswordFGD usernamePasswordFGD = UsernamePasswordFGD.builder()
                .id(1L)
                .username("Kiki_")
                .build();

        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

    when(userRepository.save(any())).thenReturn(userDao);
    ResponseEntity<Object> response = service.register(usernamePasswordFGD);
    assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
    }

    @Test
    void registerEmailIsNull_Test(){
        UsernamePasswordFGD usernamePasswordFGD = UsernamePasswordFGD.builder()
                .id(1L)
                .username("Kiki_")
                .password("kikicantik123")
                .build();

        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        when(userRepository.save(any())).thenReturn(userDao);
        ResponseEntity<Object> response = service.register(usernamePasswordFGD);
        assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
    }

    @Test
    void registerEmailIsPresent_Test(){
        UsernamePasswordFGD usernamePasswordFGD = UsernamePasswordFGD.builder()
                .id(1L)
                .username("user123")
                .password("Bismillah123")
                .email("user@gmail.com")
                .build();

        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        when(userRepository.findByEmails(any())).thenReturn(Optional.of(userDao));
        when(userRepository.save(any())).thenReturn(userDao);
        ResponseEntity<Object> response = service.register(usernamePasswordFGD);
        assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
    }

    @Test
    void registerUserIsPresent_Test(){
        UsernamePasswordFGD usernamePasswordFGD = UsernamePasswordFGD.builder()
                .id(1L)
                .username("user123")
                .password("Bismillah123")
                .email("user@gmail.com")
                .build();

        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(userDao));
        when(userRepository.save(any())).thenReturn(userDao);
        ResponseEntity<Object> response = service.register(usernamePasswordFGD);
        assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
    }

    @Test
    void registerUsernameLengthError_Test(){
        UsernamePasswordFGD usernamePasswordFGD = UsernamePasswordFGD.builder()
                .id(1L)
                .username("user123")
                .password("Bismillah123")
                .email("user@gmail.com")
                .build();

        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        when(userRepository.save(any())).thenReturn(userDao);
        ResponseEntity<Object> response = service.register(usernamePasswordFGD);
        assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
    }

    @Test
    void registerUsernamePattern_Test(){
        UsernamePasswordFGD usernamePasswordFGD = UsernamePasswordFGD.builder()
                .id(1L)
                .username("user@@@@")
                .password("Bismillah123")
                .email("user@gmail.com")
                .build();

        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        when(userRepository.save(any())).thenReturn(userDao);
        ResponseEntity<Object> response = service.register(usernamePasswordFGD);
        assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
    }

    @Test
    void registerPasswordPattern_Test(){
        UsernamePasswordFGD usernamePasswordFGD = UsernamePasswordFGD.builder()
                .id(1L)
                .username("user123")
                .password("pasword")
                .email("user@gmail.com")
                .build();

        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        when(userRepository.save(any())).thenReturn(userDao);
        ResponseEntity<Object> response = service.register(usernamePasswordFGD);
        assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
    }

    @Test
    void registerEmailPattern_Test(){
        UsernamePasswordFGD usernamePasswordFGD = UsernamePasswordFGD.builder()
                .id(1L)
                .username("user123")
                .password("Bismillah123")
                .email("email.com")
                .build();

        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        when(userRepository.save(any())).thenReturn(userDao);
        ResponseEntity<Object> response = service.register(usernamePasswordFGD);
        assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
    }

    @Test
    void generateToken_Test(){
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        UsernamePassword usernamePassword = new UsernamePassword();
        usernamePassword.setUsername("kiki");
        usernamePassword.setPassword("kiki123");

        when(userRepository.getDistinctTopByUsername(usernamePassword.getUsername())).thenReturn(userDao);
        TokenResponse response =service.generateToken(usernamePassword);
        assertNull(response.getToken());
    }

    @Test
    void generateToken_BadCredentialException_Test(){
        UsernamePassword usernamePassword = new UsernamePassword();
        usernamePassword.setUsername("kiki");
        usernamePassword.setPassword("kiki123");
        when(userRepository.getDistinctTopByUsername(usernamePassword.getUsername())).thenThrow(BadCredentialsException.class);
        assertThrows(RuntimeException.class , () -> service.generateToken(usernamePassword));

    }

    @Test
    void generateToken_Exception_Test(){
        UsernamePassword usernamePassword = new UsernamePassword();
        usernamePassword.setUsername("kiki");
        usernamePassword.setPassword("kiki123");
        when(userRepository.getDistinctTopByUsername(usernamePassword.getUsername())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class , () -> service.generateToken(usernamePassword));

    }

    @Test
    void register_Exception_Test(){
        UsernamePasswordFGD usernamePasswordFGD = UsernamePasswordFGD.builder()
                .id(1L)
                .username("kiki")
                .password("kiki123")
                .email("kiki@gmail.com")
                .build();

        when(userRepository.findByEmails(any())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class ,() -> service.register(usernamePasswordFGD) );
    }

}