package com.capstone15.alterra.controller;

import com.capstone15.alterra.domain.dto.CategoryDto;
import com.capstone15.alterra.domain.dto.payload.UsernamePassword;
import com.capstone15.alterra.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/category", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @ApiOperation(value = "Get all category", response = CategoryDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Get all category"),
    })
    @GetMapping(value = "")
    public ResponseEntity<Object> getAll() {
        return categoryService.getAllCategory();
    }

    @ApiOperation(value = "Get category by id", response = CategoryDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Get category by id"),
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getById(@PathVariable(value = "id") Long id){
        return categoryService.getCategoryById(id);
    }

    @ApiOperation(value = "Add new category", response = CategoryDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Add new category"),
    })
    @PostMapping(value = "")
    public ResponseEntity<Object> addCategory(@RequestBody CategoryDto request) {
        return categoryService.addCategory(request);
    }

    @ApiOperation(value = "Delete category", response = CategoryDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Delete category"),
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteCategory(@PathVariable(value = "id") Long id) {
        return categoryService.deleteCategory(id);
    }

    @ApiOperation(value = "Update category", response = CategoryDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Update category"),
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> updateCategory(@PathVariable(value = "id") Long id, @RequestBody CategoryDto request) {
        return categoryService.updateCategory(id, request);
    }
}
