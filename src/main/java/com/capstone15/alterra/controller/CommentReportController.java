package com.capstone15.alterra.controller;

import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dto.CommentLikeDto;
import com.capstone15.alterra.domain.dto.CommentReportDto;
import com.capstone15.alterra.service.CommentReportService;
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
@RequestMapping(value = "/v1/report_comment", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Comment Report")
public class CommentReportController {

    @Autowired
    private CommentReportService commentReportService;

    @ApiOperation(value = "Report comment ", response = CommentReportDto.class )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Report comment"),
    })
    @PostMapping(value = "")
    public ResponseEntity<Object> addReport(@RequestBody CommentReportDto request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return commentReportService.addReport(request, (UserDao) userDetails);
    }

    @ApiOperation(value = "Get Report by id comment ", response = CommentReportDto.class )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Get Report by id comment"),
    })
    @GetMapping(value = "/comment/{id}")
    public ResponseEntity<Object> getByIdComment(@PathVariable(value = "id") Long id) {
        return commentReportService.getReportByIdComment(id);
    }

    @ApiOperation(value = "Get Report by id ", response = CommentReportDto.class )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Get Report by id"),
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getById(@PathVariable(value = "id") Long id){
        return commentReportService.getReportCommentById(id);
    }

    @ApiOperation(value = "Delete Report comment ", response = CommentReportDto.class )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Delete Report comment"),
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteReport(@PathVariable(value = "id") Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return commentReportService.deleteReport(id, (UserDao) userDetails);
    }

    @ApiOperation(value = "Update Report comment ", response = CommentReportDto.class )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Update Report comment"),
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> updateReport(@PathVariable(value = "id") Long id, @RequestBody CommentReportDto request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return commentReportService.updateReport(id, request, (UserDao) userDetails);
    }
}
