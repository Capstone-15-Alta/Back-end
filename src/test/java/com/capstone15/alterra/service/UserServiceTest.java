package com.capstone15.alterra.service;

import com.capstone15.alterra.constant.AppConstant;
import com.capstone15.alterra.domain.common.ApiResponse;
import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dto.UserDto;
import com.capstone15.alterra.domain.dto.UserDtoResponse;
import com.capstone15.alterra.domain.dto.payload.TokenResponse;
import com.capstone15.alterra.domain.dto.payload.UsernamePassword;
import com.capstone15.alterra.repository.NotificationRepository;
import com.capstone15.alterra.repository.ThreadRepository;
import com.capstone15.alterra.repository.UserFollowerRepository;
import com.capstone15.alterra.repository.UserRepository;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = UserService.class)
class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ThreadRepository threadRepository;

    @MockBean
    private NotificationRepository notificationRepository;

    @MockBean
    private UserFollowerRepository userFollowerRepository;

    @MockBean
    private ModelMapper mapper;

    @Autowired
    private UserService userService;

    @Test
    void loadUser_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .username("hendro")
                .build();

        when(userRepository.getDistinctTopByUsername(any())).thenReturn(userDao);

        UserDetails userDetails = userService.loadUserByUsername(any());

        assertEquals(userDao.getUsername(), userDetails.getUsername());

    }
    @Test
    void loadUserNotFound_Test() {

        when(userRepository.getDistinctTopByUsername(any())).thenReturn(null);
        assertThrows(Exception.class, () -> userService.loadUserByUsername(any()));

    }

    @Test
    void getAllUserSuccess_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        Page<UserDao> userDaos = new PageImpl<>(List.of(userDao));
        when(userRepository.findAll((Pageable) any())).thenReturn(userDaos);

        ResponseEntity<Object> response = userService.getAllUser(any());

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        Page<UserDtoResponse> page = (Page<UserDtoResponse>) apiResponse.getData();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());
        assertEquals(1, page.getTotalElements());
    }

    @Test
    void getAllUserNotFound_Test() {
        Page<UserDao> userDaos = new PageImpl<>(List.of());
        when(userRepository.findAll((Pageable) any())).thenReturn(userDaos);

        ResponseEntity<Object> response = userService.getAllUser(any());

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.NOT_FOUND, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void getAllUserException_Test() {
        when(userRepository.findAll()).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> userService.getAllUser(any()));
    }

    @Test
    void getUserByIdSuccess_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userDao));

        ResponseEntity<Object> response = userService.getUserById(anyLong());

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void getUserByIdNotFound_Test() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Object> response = userService.getUserById(anyLong());

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.NOT_FOUND, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void getUserByIdException_Test() {
        when(userRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> userService.getUserById(1L));

    }

    @Test
    void changeRoleUserSuccess_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userDao));
        when(mapper.map(any(), eq(UserDto.class))).thenReturn(UserDto.builder()
                .roles("USER")
                .build());

        ResponseEntity<Object> response = userService.changeRoleUser(anyLong());

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void changeRoleUserNotFound_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(mapper.map(any(), eq(UserDto.class))).thenReturn(UserDto.builder()
                .roles("USER")
                .build());

        ResponseEntity<Object> response = userService.changeRoleUser(anyLong());

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.NOT_FOUND, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void changeRoleUserException_Test() {
        when(userRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> userService.changeRoleUser(anyLong()));

    }

    @Test
    void changeRoleModeratorSuccess_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userDao));
        when(mapper.map(any(), eq(UserDto.class))).thenReturn(UserDto.builder()
                .roles("MODERATOR")
                .build());

        ResponseEntity<Object> response = userService.changeRoleModerator(anyLong());

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void changeRoleModeratorNotFound_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(mapper.map(any(), eq(UserDto.class))).thenReturn(UserDto.builder()
                .roles("MODERATOR")
                .build());

        ResponseEntity<Object> response = userService.changeRoleModerator(anyLong());

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.NOT_FOUND, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void changeRoleModeratorException_Test() {
        when(userRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> userService.changeRoleModerator(anyLong()));

    }

    @Test
    void deleteUserSuccess_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userDao));
        doNothing().when(userRepository).delete(any());

        ResponseEntity<Object> response = userService.deleteUser(anyLong());

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void deleteUserNotFound_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        doNothing().when(userRepository).delete(any());

        ResponseEntity<Object> response = userService.deleteUser(anyLong());

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.NOT_FOUND, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void deleteUserException_Test() {
        when(userRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> userService.deleteUser(anyLong()));

    }

    @Test
    void updateUserSuccess_Test() throws IOException {

        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        UserDto userDto = UserDto.builder()
                .id(1L)
                .username("admin")
                .password("123")
                .build();

        UserDtoResponse userDtoResponse = UserDtoResponse.builder()
                .id(1L)
                .username("admin")
                .email("email")
                .build();

        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                "Hello, World!".getBytes()
        );

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userDao));
        when(mapper.map(any(), eq(UserDao.class))).thenReturn(userDao);
        when(mapper.map(any(), eq(UserDto.class))).thenReturn(userDto);
        when(mapper.map(any(), eq(UserDtoResponse.class))).thenReturn(userDtoResponse);
        when(userRepository.save(any())).thenReturn(userDao);

        ResponseEntity<Object> response = userService.updateUserInfo(userDtoResponse,  file,file, userDao);

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        UserDtoResponse userDto1 = (UserDtoResponse) Objects.requireNonNull(apiResponse).getData();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());
        assertEquals(userDto.getUsername(), userDto1.getUsername());

    }

    @Test
    void updateUserNotFound_Test() throws IOException {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        UserDtoResponse userDtoResponse = UserDtoResponse.builder()
                .id(1L)
                .username("admin")
                .email("email")
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = userService.updateUserInfo(userDtoResponse, null, null, userDao);

        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST.value(), responseEntity.getStatusCodeValue());
        assertEquals(AppConstant.Message.NOT_FOUND, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void updateUserDenied_Test() throws IOException {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .username("admin")
                .roles("USER")
                .build();

        UserDao userDao2 = UserDao.builder()
                .id(2L)
                .username("admin")
                .roles("USER")
                .build();

        UserDtoResponse userDtoResponse = UserDtoResponse.builder()
                .id(2L)
                .username("admin")
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userDao2));

        ResponseEntity<Object> responseEntity = userService.updateUserInfo(userDtoResponse, null, null, userDao);

        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCodeValue());
        assertEquals(AppConstant.Message.UNKNOWN_ERROR, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void updateUserException_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();
        when(userRepository.findById(any())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> userService.updateUserInfo(UserDtoResponse.builder()
                .build(),null, null, userDao));

    }

    @Test
    void updateUserPhotoSuccess_Test() throws IOException {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        UserDto userDto = UserDto.builder()
                .id(1L)
                .username("admin")
                .password("123")
                .build();

        UserDtoResponse userDtoResponse = UserDtoResponse.builder()
                .id(1L)
                .username("admin")
                .email("email")
                .build();

        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                "Hello, World!".getBytes()
        );

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userDao));
        when(mapper.map(any(), eq(UserDao.class))).thenReturn(userDao);
        when(mapper.map(any(), eq(UserDto.class))).thenReturn(userDto);
        when(mapper.map(any(), eq(UserDtoResponse.class))).thenReturn(userDtoResponse);
        when(userRepository.save(any())).thenReturn(userDao);

        ResponseEntity<Object> response = userService.updateUserPhoto(file, userDao);

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        UserDtoResponse userDto1 = (UserDtoResponse) Objects.requireNonNull(apiResponse).getData();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());
        assertEquals(userDto.getUsername(), userDto1.getUsername());

    }

    @Test
    void updateUserPhotoNotFound_Test() throws IOException {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = userService.updateUserPhoto( null, userDao);

        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST.value(), responseEntity.getStatusCodeValue());
        assertEquals(AppConstant.Message.NOT_FOUND, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void updateUserPhotoDenied_Test() throws IOException {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .username("admin")
                .roles("USER")
                .build();

        UserDao userDao2 = UserDao.builder()
                .id(2L)
                .username("admin")
                .roles("USER")
                .build();

        UserDtoResponse userDtoResponse = UserDtoResponse.builder()
                .id(2L)
                .username("admin")
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userDao2));

        ResponseEntity<Object> responseEntity = userService.updateUserPhoto( null, userDao);

        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCodeValue());
        assertEquals(AppConstant.Message.UNKNOWN_ERROR, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void updateUserPhotoException_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();
        when(userRepository.findById(any())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> userService.updateUserPhoto( null, userDao));

    }

    @Test
    void updateUserCoverSuccess_Test() throws IOException {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        UserDto userDto = UserDto.builder()
                .id(1L)
                .username("admin")
                .password("123")
                .build();

        UserDtoResponse userDtoResponse = UserDtoResponse.builder()
                .id(1L)
                .username("admin")
                .email("email")
                .build();

        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                "Hello, World!".getBytes()
        );

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userDao));
        when(mapper.map(any(), eq(UserDao.class))).thenReturn(userDao);
        when(mapper.map(any(), eq(UserDto.class))).thenReturn(userDto);
        when(mapper.map(any(), eq(UserDtoResponse.class))).thenReturn(userDtoResponse);
        when(userRepository.save(any())).thenReturn(userDao);

        ResponseEntity<Object> response = userService.updateUserCover(file, userDao);

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        UserDtoResponse userDto1 = (UserDtoResponse) Objects.requireNonNull(apiResponse).getData();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());
        assertEquals(userDto.getUsername(), userDto1.getUsername());

    }

    @Test
    void updateUserCoverNotFound_Test() throws IOException {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = userService.updateUserCover( null, userDao);

        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST.value(), responseEntity.getStatusCodeValue());
        assertEquals(AppConstant.Message.NOT_FOUND, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void updateUserCoverDenied_Test() throws IOException {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .username("admin")
                .roles("USER")
                .build();

        UserDao userDao2 = UserDao.builder()
                .id(2L)
                .username("admin")
                .roles("USER")
                .build();

        UserDtoResponse userDtoResponse = UserDtoResponse.builder()
                .id(2L)
                .username("admin")
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userDao2));

        ResponseEntity<Object> responseEntity = userService.updateUserCover( null, userDao);

        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCodeValue());
        assertEquals(AppConstant.Message.UNKNOWN_ERROR, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void updateUserCoverException_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();
        when(userRepository.findById(any())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> userService.updateUserCover( null, userDao));

    }

    @Test
    void getUserRankingSuccess_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        Page<UserDao> userDaos = new PageImpl<>(List.of(userDao));
        when(userRepository.findAllUserByRanking(any())).thenReturn(userDaos);

        ResponseEntity<Object> response = userService.getUserRankingByFollowers(any());

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void getUserRankingNotFound_Test() {
        Page<UserDao> userDaos = new PageImpl<>(List.of());
        when(userRepository.findAllUserByRanking(any())).thenReturn(userDaos);

        ResponseEntity<Object> response = userService.getUserRankingByFollowers(null);

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.NOT_FOUND, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void getUserRankingException_Test() {
        when(userRepository.findAllUserByRanking(any())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> userService.getUserRankingByFollowers(any()));

    }

    @Test
    void getUserRankingByTotalThreadAndLikeSuccess_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        Page<UserDao> userDaos = new PageImpl<>(List.of(userDao));
        when(userRepository.findUserRanking(any())).thenReturn(userDaos);

        ResponseEntity<Object> response = userService.getUserRankingByTotalThreadAndLike(any());

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void getUserRankingByTotalThreadAndLikeNotFound_Test() {
        Page<UserDao> userDaos = new PageImpl<>(List.of());
        when(userRepository.findUserRanking(any())).thenReturn(userDaos);

        ResponseEntity<Object> response = userService.getUserRankingByTotalThreadAndLike(null);

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.NOT_FOUND, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void getUserRankingByTotalThreadAndLikeException_Test() {
        when(userRepository.findUserRanking(any())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> userService.getUserRankingByTotalThreadAndLike(any()));

    }

    @Test
    void updateResetPasswordTokenException_Test() {
        when(userRepository.findByEmail(any())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> userService.updateResetPasswordToken(any(), any()));

    }

    @Test
    void updateResetPasswordTokenSuccess_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .resetPasswordToken("")
                .build();

        when(userRepository.findByEmail(any())).thenReturn(userDao);
        userService.updateResetPasswordToken("token", any());
    }

    @Test
    void updatePassword_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .password("password")
                .resetPasswordToken("token")
                .build();

        userService.updatePassword( userDao, "password123");
    }

    @Test
    void getByResetPasswordToken_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .password("password")
                .resetPasswordToken("token")
                .build();

        when(userRepository.findByResetPasswordToken(any())).thenReturn(userDao);
        userService.getByResetPasswordToken(any());
    }

    @Test
    void forgotPassword_Test() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        ResponseEntity<Object> response = userService.forgotPassword(request);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());

    }

}