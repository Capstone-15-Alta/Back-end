package com.capstone15.alterra.controller;

import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dto.CommentReportDto;
import com.capstone15.alterra.domain.dto.ThreadReportDto;
import com.capstone15.alterra.service.CommentReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/v1/report_comment", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class CommentReportController {

    @Autowired
    private CommentReportService commentReportService;

    @PostMapping(value = "")
    public ResponseEntity<Object> addReport(@RequestBody CommentReportDto request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return commentReportService.addReport(request, (UserDao) userDetails);
    }

    @GetMapping(value = "/comment/{id}")
    public ResponseEntity<Object> getByIdComment(@PathVariable(value = "id") Long id) {
        return commentReportService.getReportByIdComment(id);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getById(@PathVariable(value = "id") Long id){
        return commentReportService.getReportCommentById(id);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteReport(@PathVariable(value = "id") Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return commentReportService.deleteReport(id, (UserDao) userDetails);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> updateReport(@PathVariable(value = "id") Long id, @RequestBody CommentReportDto request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return commentReportService.updateReport(id, request, (UserDao) userDetails);
    }
}
