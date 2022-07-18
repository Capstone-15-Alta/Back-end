package com.capstone15.alterra.controller;

import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dto.ThreadReportDto;
import com.capstone15.alterra.service.ThreadReportService;
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
public class ThreadReportController {

    @Autowired
    private ThreadReportService threadReportService;

    @PostMapping(value = "")
    public ResponseEntity<Object> addReport(@RequestBody ThreadReportDto request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return threadReportService.addReport(request, (UserDao) userDetails);
    }

    @GetMapping(value = "/thread/{id}")
    public ResponseEntity<Object> getByIdThread(@PathVariable(value = "id") Long id) {
        return threadReportService.getReportByIdThread(id);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getById(@PathVariable(value = "id") Long id){
        return threadReportService.getReportThreadById(id);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteReport(@PathVariable(value = "id") Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return threadReportService.deleteReport(id, (UserDao) userDetails);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> updateReport(@PathVariable(value = "id") Long id, @RequestBody ThreadReportDto request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return threadReportService.updateReport(id, request, (UserDao) userDetails);
    }
}
