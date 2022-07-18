package com.capstone15.alterra.service;

import com.capstone15.alterra.domain.dao.CategoryDao;
import com.capstone15.alterra.domain.dto.CategoryDto;
import com.capstone15.alterra.repository.CategoryRepository;
import org.checkerframework.checker.units.qual.C;
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
@SpringBootTest(classes = CategoryService.class)
public class CategoryServiceTest {

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private ModelMapper mapper;

    @Autowired
    private CategoryService service;

    @Test
    void getAllCategory_Success_Test() {
        CategoryDao categoryDao = CategoryDao.builder()
                .id(2L)
                .build();

        when(categoryRepository.findAll()).thenReturn(List.of(categoryDao));

        ResponseEntity<Object> response = service.getAllCategory();
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());

    }

    @Test
    void getAllCategory_Exception_Test() {
        when(categoryRepository.findAll()).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> service.getAllCategory());
    }

    @Test
    void getCategoryById_Failed_Test() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> response = service.getCategoryById(anyLong());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());

    }

    @Test
    void getCategoryById_Success_Test() {
        CategoryDao categoryDao = CategoryDao.builder()
                .id(1L)
                .build();

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(categoryDao));

        ResponseEntity<Object> response = service.getCategoryById(anyLong());
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }


    @Test
    void getCategoryById_Exception_Test() {
        when(categoryRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> service.getCategoryById(1L));
    }

    @Test
    void addCategory_Success_Test() {
        CategoryDao categoryDao = CategoryDao.builder()
                .id(1L)
                .build();
        CategoryDto categoryDto = CategoryDto.builder()
                .id(1L)
                .build();

        when(categoryRepository.save(any())).thenReturn(categoryDao);

        ResponseEntity<Object> response = service.addCategory(categoryDto);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }

    @Test
    void addCategory_Exception_Test() {
        CategoryDto categoryDto = CategoryDto.builder()
                .id(1L)
                .build();

        when(categoryRepository.save(any())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> service.addCategory(categoryDto));
    }

    @Test
    void deleteCategory_failed() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> response = service.deleteCategory(anyLong());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    void deleteCategory_Success_Test() {
        CategoryDao categoryDao = CategoryDao.builder()
                .id(1L)
                .build();

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(categoryDao));
        ResponseEntity<Object> response = service.deleteCategory(anyLong());
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }

    @Test
    void deleteCategory_Exception_Test() {
        when(categoryRepository.findById(any())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> service.deleteCategory(anyLong()));
    }

    @Test
    void updateCategory_Failed_Test() {
        CategoryDao categoryDao = CategoryDao.builder()
                .id(1L)
                .build();

        CategoryDto categoryDto = CategoryDto.builder()
                .id(1L)
                .build();

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(categoryRepository.save(any())).thenReturn(categoryDao);
        ResponseEntity<Object> response = service.updateCategory(anyLong(), categoryDto);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    void updateCategory_Success_Test() {
        CategoryDao categoryDao = CategoryDao.builder()
                .id(1L)
                .build();

        CategoryDto categoryDto = CategoryDto.builder()
                .id(1L)
                .build();

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(categoryDao));
        when(categoryRepository.save(any())).thenReturn(categoryDao);
        ResponseEntity<Object> response = service.updateCategory(anyLong(), categoryDto);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }

    @Test
    void updateCategory_Exception_Test() {
        CategoryDto categoryDto = CategoryDto.builder()
                .id(1L)
                .build();

        when(categoryRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        assertThrows(Exception.class, () -> service.updateCategory(anyLong(), categoryDto));
    }
}