package com.capstone15.alterra.controller;

import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dto.ThreadLikeDto;
import com.capstone15.alterra.domain.dto.ThreadReportDto;
import com.capstone15.alterra.service.ThreadReportService;
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
@RequestMapping(value = "/v1/report_thread", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Thread Report")
public class ThreadReportController {

    @Autowired
    private ThreadReportService threadReportService;

    @ApiOperation(value = "Report thread", response = ThreadReportDto.class )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Report thread"),
    })
    @PostMapping(value = "")
    public ResponseEntity<Object> addReport(@RequestBody ThreadReportDto request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return threadReportService.addReport(request, (UserDao) userDetails);
    }

    @ApiOperation(value = "Get Report by id thread", response = ThreadReportDto.class )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Get Report by id thread"),
    })
    @GetMapping(value = "/thread/{id}")
    public ResponseEntity<Object> getByIdThread(@PathVariable(value = "id") Long id) {
        return threadReportService.getReportByIdThread(id);
    }

    @ApiOperation(value = "Get Report by id", response = ThreadReportDto.class )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Get Report by id"),
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getById(@PathVariable(value = "id") Long id){
        return threadReportService.getReportThreadById(id);
    }

    @ApiOperation(value = "Delete Report thread", response = ThreadReportDto.class )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Delete Report thread"),
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteReport(@PathVariable(value = "id") Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return threadReportService.deleteReport(id, (UserDao) userDetails);
    }

    @ApiOperation(value = "Update Report thread", response = ThreadReportDto.class )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Update Report thread"),
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> updateReport(@PathVariable(value = "id") Long id, @RequestBody ThreadReportDto request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return threadReportService.updateReport(id, request, (UserDao) userDetails);
    }
}
