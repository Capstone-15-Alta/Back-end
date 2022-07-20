package com.capstone15.alterra.controller;

import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dto.CategoryDto;
import com.capstone15.alterra.domain.dto.CommentDto;
import com.capstone15.alterra.domain.dto.CommentDtoResponse;
import com.capstone15.alterra.service.CommentService;
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
@RequestMapping(value = "/v1/comment", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ApiOperation(value = "Add new comment", response = CommentDto.class )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success add new comment"),
    })
    @PostMapping(value = "")
    public ResponseEntity<Object> addComment(@RequestBody CommentDto request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return commentService.addComment(request, (UserDao) userDetails);
    }

    @ApiOperation(value = "Get comment by id thread", response = CommentDtoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Get comment by id thread"),
    })
    @GetMapping(value = "/thread/{id}")
    public ResponseEntity<Object> getByIdThread(@PathVariable(value = "id") Long id) {
        return commentService.getCommentByIdThread(id);
    }

    @ApiOperation(value = "Get comment by id user", response = CommentDtoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Get comment by id user"),
    })
    @GetMapping(value = "/user/{id}")
    public ResponseEntity<Object> getByIdUser(@PathVariable(value = "id") Long id){
        return commentService.getCommentByIdUser(id);
    }

    @ApiOperation(value = "Get comment by id", response = CommentDtoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Get comment by id"),
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getById(@PathVariable(value = "id") Long id){
        return commentService.getCommentById(id);
    }

    @ApiOperation(value = "Delete comment", response = CommentDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Delete comment"),
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteComment(@PathVariable(value = "id") Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return commentService.deleteComment(id, (UserDao) userDetails);
    }

    @ApiOperation(value = "Update comment", response = CommentDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Update comment"),
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> updateComment(@PathVariable(value = "id") Long id, @RequestBody CommentDto request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return commentService.updateComment(id, request, (UserDao) userDetails);
    }
}
