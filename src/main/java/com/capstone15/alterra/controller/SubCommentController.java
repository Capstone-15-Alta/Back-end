package com.capstone15.alterra.controller;

import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dto.CommentDto;
import com.capstone15.alterra.domain.dto.SubCommentDto;
import com.capstone15.alterra.service.SubCommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/v1/sub_comment", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class SubCommentController {

    @Autowired
    private SubCommentService subCommentService;

    @PostMapping(value = "")
    public ResponseEntity<Object> addSubComment(@RequestBody SubCommentDto request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return subCommentService.addSubComment(request, (UserDao) userDetails);
    }

    @GetMapping(value = "/comment/{id}")
    public ResponseEntity<Object> getByIdComment(@PathVariable(value = "id") Long id) {
        return subCommentService.getSubCommentByIdComment(id);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getById(@PathVariable(value = "id") Long id){
        return subCommentService.getSubCommentById(id);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteSubComment(@PathVariable(value = "id") Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return subCommentService.deleteSubComment(id, (UserDao) userDetails);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> updateSubComment(@PathVariable(value = "id") Long id, @RequestBody SubCommentDto request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return subCommentService.updateSubComment(id, request, (UserDao) userDetails);
    }
}
