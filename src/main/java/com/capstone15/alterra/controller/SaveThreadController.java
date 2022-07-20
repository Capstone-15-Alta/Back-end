package com.capstone15.alterra.controller;

import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dto.NotificationDto;
import com.capstone15.alterra.domain.dto.SaveThreadDto;
import com.capstone15.alterra.domain.dto.SaveThreadDtoResponse;
import com.capstone15.alterra.domain.dto.SaveThreadDtoResponse4;
import com.capstone15.alterra.service.SaveThreadService;
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
@RequestMapping(value = "/v1/save", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Thread Save")
public class SaveThreadController {

    @Autowired
    private SaveThreadService saveThreadService;

    @ApiOperation(value = "Save thread", response = SaveThreadDto.class )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Save thread"),
    })
    @PutMapping(value = "/thread/{id}")
    public ResponseEntity<Object> saveThread(@PathVariable(value = "id") Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return saveThreadService.saveThread(id, (UserDao) userDetails);
    }

    @ApiOperation(value = "Get save thread by id user ", response = SaveThreadDtoResponse4.class )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Get save thread by id user"),
    })
    @GetMapping(value = "/user/{id}")
    public ResponseEntity<Object> getByIdUser(@PathVariable(value = "id") Long id) {
        return saveThreadService.getSaveThreadByIdUser(id);
    }

}
