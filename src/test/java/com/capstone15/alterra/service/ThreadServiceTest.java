package com.capstone15.alterra.service;

import com.capstone15.alterra.constant.AppConstant;
import com.capstone15.alterra.domain.common.ApiResponse;
import com.capstone15.alterra.domain.common.ApiResponse2;
import com.capstone15.alterra.domain.dao.CategoryDao;
import com.capstone15.alterra.domain.dao.ThreadDao;
import com.capstone15.alterra.domain.dao.ThreadViewDao;
import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dto.ThreadDto;
import com.capstone15.alterra.domain.dto.ThreadDtoResponse;
import com.capstone15.alterra.domain.dto.UserDto;
import com.capstone15.alterra.repository.CategoryRepository;
import com.capstone15.alterra.repository.ThreadRepository;
import com.capstone15.alterra.repository.ThreadViewRepository;
import com.capstone15.alterra.repository.UserRepository;
import com.capstone15.alterra.util.ResponseUtil;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ThreadService.class)
class ThreadServiceTest {

    @MockBean
    private ThreadRepository threadRepository;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ThreadViewRepository threadViewRepository;

    @MockBean
    private ModelMapper mapper;

    @Autowired
    private ThreadService threadService;


    @Test
    void addThreadSuccess_Test() throws IOException {
        CategoryDao categoryDao = CategoryDao.builder()
                .id(1L)
                .build();

        UserDao userDao = UserDao.builder()
                .id(1L)
                .username("admin")
                .password("pass")
                .build();

        ThreadDao threadDao = ThreadDao.builder()
                .id(1L)
                .build();

        ThreadDto threadDto = ThreadDto.builder()
                .id(1L)
                .title("title")
                .description("description")
                .categoryId(1L)
                .build();

        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                "Hello, World!".getBytes()
        );

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(categoryDao));
        when(mapper.map(any(), eq(ThreadDao.class))).thenReturn(threadDao);
        when(mapper.map(any(), eq(ThreadDto.class))).thenReturn(threadDto);
        when(threadRepository.save(any())).thenReturn(threadDao);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userDao));

        ResponseEntity<Object> response = threadService.addThread(ThreadDto.builder()
                        .title("title")
                        .description("description")
                .categoryId(1L)
                .build(),  file, userDao);

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        ThreadDto threadDto1 = (ThreadDto) Objects.requireNonNull(apiResponse).getData();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());
        assertEquals(threadDto.getTitle(), threadDto1.getTitle());

    }

    @Test
    void addThreadSuccessMultipartfileIsNull_Test() throws IOException {
        CategoryDao categoryDao = CategoryDao.builder()
                .id(1L)
                .build();

        UserDao userDao = UserDao.builder()
                .id(1L)
                .username("admin")
                .password("pass")
                .build();

        ThreadDao threadDao = ThreadDao.builder()
                .id(1L)
                .build();

        ThreadDto threadDto = ThreadDto.builder()
                .id(1L)
                .title("title")
                .description("description")
                .categoryId(1L)
                .build();

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(categoryDao));
        when(mapper.map(any(), eq(ThreadDao.class))).thenReturn(threadDao);
        when(mapper.map(any(), eq(ThreadDto.class))).thenReturn(threadDto);
        when(threadRepository.save(any())).thenReturn(threadDao);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userDao));

        ResponseEntity<Object> response = threadService.addThread(ThreadDto.builder()
                .title("title")
                .description("description")
                .categoryId(1L)
                .build(),  null, userDao);

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        ThreadDto threadDto1 = (ThreadDto) Objects.requireNonNull(apiResponse).getData();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());
        assertEquals(threadDto.getTitle(), threadDto1.getTitle());

    }

    @Test
    void addThreadCategoryIsEmpty_Test() throws IOException {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Object> responseEntity = threadService.addThread(ThreadDto.builder()
                .title("title")
                .description("description")
                .categoryId(1L)
                .build(), null, any());

        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.BAD_REQUEST.value(), responseEntity.getStatusCodeValue());
        assertEquals("Category not found", Objects.requireNonNull(apiResponse).getMessage());
    }

    @Test
    void addThreadDescriptionIsNull_Test() throws IOException {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        ResponseEntity<Object> responseEntity = threadService.addThread(ThreadDto.builder()
                .title("title")
                        .description(null)
                .categoryId(1L)
                .build(), null, userDao);

        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.BAD_REQUEST.value(), responseEntity.getStatusCodeValue());
        assertEquals("description cant null !", Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void addThreadCategoryIsNull_Test() throws IOException {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        ResponseEntity<Object> responseEntity = threadService.addThread(ThreadDto.builder()
                .title("title")
                .description("description")
                .categoryId(null)
                .build(), null, userDao);

        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.BAD_REQUEST.value(), responseEntity.getStatusCodeValue());
        assertEquals("category cant null !", Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void addThreadTitleIsNull_Test() throws IOException {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        ResponseEntity<Object> responseEntity = threadService.addThread(ThreadDto.builder()
                .title(null)
                .description("description")
                .categoryId(1L)
                .build(), null, userDao);

        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        assertEquals(HttpStatus.BAD_REQUEST.value(), responseEntity.getStatusCodeValue());
        assertEquals("title cant null !", Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void addThreadException_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        ThreadDto threadDto = ThreadDto.builder()
                .id(1L)
                .title("title")
                .description("description")
                .categoryId(1L)
                .build();

        when(categoryRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> threadService.addThread(threadDto, null, userDao));
   }

    @Test
    void getAllThreadSuccess_Test() throws IOException {
        CategoryDao categoryDao = CategoryDao.builder()
                .id(1L)
                .build();

        ThreadDao threadDao = ThreadDao.builder()
                .id(1L)
                .build();

        Page<ThreadDao> threadDaos = new PageImpl<>(List.of(threadDao));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(categoryDao));
        when(threadRepository.findAllBy(any())).thenReturn(threadDaos);

        when(mapper.map(any(), eq(ThreadDtoResponse.class))).thenReturn(ThreadDtoResponse.builder()
                .id(1L)
                .title("title")
                .description("description")
                .category(categoryDao)
                .build());

        ResponseEntity<Object> response = threadService.getAllThread(any());

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        Page<ThreadDto> list = (Page<ThreadDto>) apiResponse.getData();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());
        assertEquals(1, list.getTotalElements());
    }

    @Test
    void getAllThreadNotFound_Test() {
        Page<ThreadDao> threadDaos = new PageImpl<>(List.of());
        when(threadRepository.findAllBy( any())).thenReturn(threadDaos);

        ResponseEntity<Object> response = threadService.getAllThread(any());

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.NOT_FOUND, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void getAllThreadException_Test() {
        when(threadRepository.findAllBy(any())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> threadService.getAllThread(any()));
    }

    @Test
    void getThreadByIdSuccess_Test() {

        ThreadViewDao threadViewDao = ThreadViewDao.builder()
                .threadId(1L)
                .views(1)
                .build();

        ThreadDao threadDao = ThreadDao.builder()
                .id(1L)
                .view(threadViewDao)
                .build();

        ThreadDto threadDto = ThreadDto.builder()
                .id(1L)
                .title("title")
                .description("description")
                .categoryId(1L)
                .build();


        when(threadRepository.findById(anyLong())).thenReturn(Optional.of(threadDao));
        when(threadViewRepository.findById(anyLong())).thenReturn(Optional.of(threadViewDao));
        when(mapper.map(any(), eq(ThreadDto.class))).thenReturn(threadDto);
        ResponseEntity<Object> response = threadService.getThreadById(1L);

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void getThreadByIdNotFound_Test() {

        when(threadRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> response = threadService.getThreadById(1L);

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.NOT_FOUND, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void getThreadByIdException_Test() {
        when(threadRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> threadService.getThreadById(1L));

    }

    @Test
    void getThreadByIdUserSuccess_Test() {
        ThreadDao threadDao = ThreadDao.builder()
                .id(1L)
                .title("title")
                .build();

        UserDao userDao = UserDao.builder()
                .id(1L)
                .threads(List.of(threadDao))
                .build();

        ThreadDtoResponse threadDtoResponse = ThreadDtoResponse.builder()
                .id(1L)
                .title("title")
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userDao));
        when(mapper.map(any(), eq(ThreadDao.class))).thenReturn(threadDao);
        when(mapper.map(any(), eq(ThreadDtoResponse.class))).thenReturn(threadDtoResponse);

        ResponseEntity<Object> response = threadService.getThreadByIdUser(1L,PageRequest.of(0, 2));

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());



    }

    @Test
    void getThreadByIdUserNotFound_Test() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> response = threadService.getThreadByIdUser(1L, any());

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.NOT_FOUND, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void getThreadByIdUserException_Test() {
        when(userRepository.findById(any())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> threadService.getThreadByIdUser(any(), any()));

    }



    @Test
    void getThreadByTitleSuccess_Test() {
        ThreadDao threadDao = ThreadDao.builder()
                .id(1L)
                .build();

        Page<ThreadDao> threadDaos = new PageImpl<>(List.of(threadDao));

        when(threadRepository.findAllThreadByTitle(any(), any())).thenReturn(threadDaos);
        when(mapper.map(any(), eq(ThreadDto.class))).thenReturn(ThreadDto.builder()
                .id(1L)
                .title("title")
                .build());

        ResponseEntity<Object> response = threadService.searchThreadByTitle(any(), any());

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void getThreadByTitleNotFound_Test() {
        Page<ThreadDao> threadDaos = new PageImpl<>(List.of());
        when(threadRepository.findAllThreadByTitle(any(), any())).thenReturn(threadDaos);
        ResponseEntity<Object> response = threadService.searchThreadByTitle(any(), any());

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.NOT_FOUND, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void getThreadByTitleException_Test() {
        when(threadRepository.findAllThreadByTitle(any(), any())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> threadService.searchThreadByTitle(any(), any()));

    }

    @Test
    void getThreadByCategorySuccess_Test() {
        ThreadDao threadDao = ThreadDao.builder()
                .id(1L)
                .build();

        CategoryDao categoryDao = CategoryDao.builder()
                .id(1L)
                .build();

        Page<ThreadDao> threadDaos = new PageImpl<>(List.of(threadDao));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(categoryDao));
        when(threadRepository.findThreadDaoByCategoryCategoryNameContainingIgnoreCase(any(), any())).thenReturn(threadDaos);
        when(mapper.map(any(), eq(ThreadDtoResponse.class))).thenReturn(ThreadDtoResponse.builder()
                .id(1L)
                .title("title")
                .build());

        ResponseEntity<Object> response = threadService.searchThreadByCategoryName(any(), any());

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void getThreadByCategoryIsEmpty_Test() {
        Page<ThreadDao> threadDaos = new PageImpl<>(List.of());
        when(threadRepository.findThreadDaoByCategoryCategoryNameContainingIgnoreCase(any(), any())).thenReturn(threadDaos);
        when(threadRepository.findAllBy(any())).thenReturn(threadDaos);

        ResponseEntity<Object> response = threadService.searchThreadByCategoryName(any(),any());

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void getThreadByCategoryException_Test() {
        when(threadRepository.findThreadDaoByCategoryCategoryNameContainingIgnoreCase(any(), any())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> threadService.searchThreadByCategoryName(any(), any()));

    }

    @Test
    void getTrendingThreadSuccess_Test() {
        ThreadDao threadDao = ThreadDao.builder()
                .id(1L)
                .build();
        Page<ThreadDao> threadDaos = new PageImpl<>(List.of(threadDao));
        when(threadRepository.findAllPopularThread(any())).thenReturn(threadDaos);

        ResponseEntity<Object> response = threadService.searchTrendingThread(any());

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void getTrendingThreadNotFound_Test() {
        Page<ThreadDao> threadDaos = new PageImpl<>(List.of());
        when(threadRepository.findAllPopularThread(any())).thenReturn(threadDaos);

        ResponseEntity<Object> response = threadService.searchTrendingThread(null);

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.NOT_FOUND, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void getTrendingThreadException_Test() {
        when(threadRepository.findAllPopularThread(any())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> threadService.searchTrendingThread(any()));

    }

    @Test
    void getThreadWithPaginateSuccess_Test() {
        ThreadDao threadDao = ThreadDao.builder()
                .id(1L)
                .build();

        Page<ThreadDao> threadDaos = new PageImpl<>(List.of(threadDao));
        when(threadRepository.findAllBy(any())).thenReturn(threadDaos);
        ResponseEntity<Object> response = threadService.getAllThreadWithPaginate(any());

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void getThreadWithPaginateException_Test() {
        when(threadRepository.findAllBy(any())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> threadService.getAllThreadWithPaginate(any()));

    }

    @Test
    void deleteThreadSuccess_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        ThreadViewDao threadViewDao = ThreadViewDao.builder()
                .threadId(1L)
                .build();

        when(threadRepository.findById(anyLong())).thenReturn(Optional.of(ThreadDao.builder()
                .id(1L)
                .user(userDao)
                .view(threadViewDao)
                .build()));
        doNothing().when(threadRepository).delete(any());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userDao));

        ResponseEntity<Object> response = threadService.deleteThread(anyLong(), userDao);

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void deleteThreadNotFound_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        when(threadRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Object> response = threadService.deleteThread(anyLong(), userDao);

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.NOT_FOUND, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void deleteThreadDenied_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .username("user")
                .roles("USER")
                .build();

        ThreadDao threadDao = ThreadDao.builder()
                .user(UserDao.builder().id(2L).build())
                .build();

        when(threadRepository.findById(anyLong())).thenReturn(Optional.of(threadDao));

        ResponseEntity<Object> response = threadService.deleteThread(anyLong(), userDao);

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.UNKNOWN_ERROR, Objects.requireNonNull(apiResponse).getMessage());

    }

    @Test
    void deleteThreadException_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();
        when(threadRepository.findById(any())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> threadService.deleteThread(anyLong(), userDao));

    }

    @Test
    void updateThreadSuccess_Test() throws IOException {

        UserDao userDao = UserDao.builder()
                .id(1L)
                .username("admin")
                .password("pass")
                .build();

        ThreadDao threadDao = ThreadDao.builder()
                .id(1L)
                .user(userDao)
                .build();

        ThreadDto threadDto = ThreadDto.builder()
                .id(1L)
                .title("title")
                .description("description")
                .categoryId(1L)
                .build();

        ThreadDtoResponse threadDtoResponse = ThreadDtoResponse.builder()
                .id(1L)
                .title("title")
                .description("description")
                .build();

        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                "Hello, World!".getBytes()
        );

        when(threadRepository.findById(anyLong())).thenReturn(Optional.of(threadDao));
        when(mapper.map(any(), eq(ThreadDao.class))).thenReturn(threadDao);
        when(mapper.map(any(), eq(ThreadDto.class))).thenReturn(threadDto);
        when(mapper.map(any(), eq(ThreadDtoResponse.class))).thenReturn(threadDtoResponse);
        when(threadRepository.save(any())).thenReturn(threadDao);

        ResponseEntity<Object> response = threadService.updateThread(1L, ThreadDto.builder()
                .title("title")
                .description("description")
                .categoryId(1L)
                .build(),  file, userDao);

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        ThreadDtoResponse threadDto1 = (ThreadDtoResponse) Objects.requireNonNull(apiResponse).getData();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());
        assertEquals(threadDto.getTitle(), threadDto1.getTitle());

    }

    @Test
    void updateThreadMultipartfileIsNull_Test() throws IOException {

        UserDao userDao = UserDao.builder()
                .id(1L)
                .username("admin")
                .password("pass")
                .build();

        ThreadDao threadDao = ThreadDao.builder()
                .id(1L)
                .user(userDao)
                .build();

        ThreadDto threadDto = ThreadDto.builder()
                .id(1L)
                .title("title")
                .description("description")
                .categoryId(1L)
                .build();

        ThreadDtoResponse threadDtoResponse = ThreadDtoResponse.builder()
                .id(1L)
                .title("title")
                .description("description")
                .build();

        when(threadRepository.findById(anyLong())).thenReturn(Optional.of(threadDao));
        when(mapper.map(any(), eq(ThreadDao.class))).thenReturn(threadDao);
        when(mapper.map(any(), eq(ThreadDto.class))).thenReturn(threadDto);
        when(mapper.map(any(), eq(ThreadDtoResponse.class))).thenReturn(threadDtoResponse);
        when(threadRepository.save(any())).thenReturn(threadDao);

        ResponseEntity<Object> response = threadService.updateThread(1L, ThreadDto.builder()
                .title("title")
                .description("description")
                .categoryId(1L)
                .build(),  null, userDao);

        ApiResponse apiResponse = (ApiResponse) response.getBody();

        ThreadDtoResponse threadDto1 = (ThreadDtoResponse) Objects.requireNonNull(apiResponse).getData();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(AppConstant.Message.SUCCESS, Objects.requireNonNull(apiResponse).getMessage());
        assertEquals(threadDto.getTitle(), threadDto1.getTitle());

    }

    @Test
    void updateThreadIsEmpty_Test() throws IOException {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();

        when(threadRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Object> responseEntity = threadService.updateThread(anyLong(), ThreadDto.builder()
                .title("title")
                .description("description")
                .categoryId(1L)
                .build(), null, userDao);

        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST.value(), responseEntity.getStatusCodeValue());
        assertEquals(AppConstant.Message.NOT_FOUND, Objects.requireNonNull(apiResponse).getMessage());
    }

    @Test
    void updateThreadDenied_Test() throws IOException {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .username("user")
                .roles("USER")
                .build();

        ThreadDao threadDao = ThreadDao.builder()
                .user(UserDao.builder().id(2L).build())
                .build();

        when(threadRepository.findById(anyLong())).thenReturn(Optional.of(threadDao));

        ResponseEntity<Object> responseEntity = threadService.updateThread(anyLong(), ThreadDto.builder()
                .title("title")
                .description("description")
                .categoryId(1L)
                .build(), null, userDao);

        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCodeValue());
        assertEquals(AppConstant.Message.UNKNOWN_ERROR, Objects.requireNonNull(apiResponse).getMessage());
    }

    @Test
    void updateThreadException_Test() {
        UserDao userDao = UserDao.builder()
                .id(1L)
                .build();
        when(threadRepository.findById(any())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> threadService.updateThread(anyLong(),ThreadDto.builder()
                .id(1L)
                .title("title")
                .description("description")
                .categoryId(1L)
                .build(),null, userDao));

    }

}