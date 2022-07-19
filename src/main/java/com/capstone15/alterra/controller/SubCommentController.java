package com.capstone15.alterra.controller;

import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dto.CommentDto;
import com.capstone15.alterra.domain.dto.SubCommentDto;
import com.capstone15.alterra.domain.dto.SubCommentDtoResponse;
import com.capstone15.alterra.service.SubCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/v1/sub_comment", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Sub Comment")
public class SubCommentController {

    @Autowired
    private SubCommentService subCommentService;

    @ApiOperation(value = "Add new sub comment", response = SubCommentDto.class )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success add new sub comment"),
    })
    @PostMapping(value = "")
    public ResponseEntity<Object> addSubComment(@RequestBody SubCommentDto request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return subCommentService.addSubComment(request, (UserDao) userDetails);
    }

    @ApiOperation(value = "Get sub comment by id comment", response = SubCommentDtoResponse.class )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success get sub comment by id comment"),
    })
    @GetMapping(value = "/comment/{id}")
    public ResponseEntity<Object> getByIdComment(@PathVariable(value = "id") Long id) {
        return subCommentService.getSubCommentByIdComment(id);
    }

    @ApiOperation(value = "Get sub comment by id", response = SubCommentDtoResponse.class )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Get sub comment by id"),
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getById(@PathVariable(value = "id") Long id){
        return subCommentService.getSubCommentById(id);
    }

    @ApiOperation(value = "Delete sub comment", response = SubCommentDto.class )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Delete sub comment"),
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteSubComment(@PathVariable(value = "id") Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return subCommentService.deleteSubComment(id, (UserDao) userDetails);
    }

    @ApiOperation(value = "Update sub comment", response = SubCommentDto.class )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Update sub comment"),
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> updateSubComment(@PathVariable(value = "id") Long id, @RequestBody SubCommentDto request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return subCommentService.updateSubComment(id, request, (UserDao) userDetails);
    }
}
