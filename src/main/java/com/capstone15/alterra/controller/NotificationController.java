package com.capstone15.alterra.controller;

import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dto.CommentReportDto;
import com.capstone15.alterra.domain.dto.NotificationDto;
import com.capstone15.alterra.service.NotificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/v1/notification", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @ApiOperation(value = "Get all notification ", response = NotificationDto.class )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success get all notification"),
    })
    @GetMapping(value = "")
    public ResponseEntity<Object> getAll(@PageableDefault( page = 0, size = 10) Pageable pageable) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return notificationService.getAllNotification((UserDao) userDetails, pageable);
    }

    @ApiOperation(value = "Read notification ", response = NotificationDto.class )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success read notification"),
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> readById(@PathVariable(value = "id") Long id){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return notificationService.readNotificationById(id, (UserDao) userDetails);
    }

    @ApiOperation(value = "Read all notification ", response = NotificationDto.class )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success read all notification"),
    })
    @GetMapping(value = "/readall")
    public ResponseEntity<Object> readAll(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return notificationService.readAllNotification((UserDao) userDetails);
    }

}
