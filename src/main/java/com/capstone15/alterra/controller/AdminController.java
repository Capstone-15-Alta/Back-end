package com.capstone15.alterra.controller;

import com.capstone15.alterra.domain.dto.CommentReportDto;
import com.capstone15.alterra.domain.dto.ThreadReportDtoResponse;
import com.capstone15.alterra.domain.dto.UserDto;
import com.capstone15.alterra.domain.dto.UserDtoResponse;
import com.capstone15.alterra.domain.dto.payload.UsernamePassword;
import com.capstone15.alterra.domain.dto.payload.UsernamePasswordFGD;
import com.capstone15.alterra.service.CommentReportService;
import com.capstone15.alterra.service.ThreadReportService;
import com.capstone15.alterra.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/v1/admin", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Admin", value = "Admin" )
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private ThreadReportService threadReportService;

    @Autowired
    private CommentReportService commentReportService;

    @ApiOperation(value = "Change role to USER", response = UserDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Change role to USER"),
    })
    @PutMapping(value = "/role/user/{id}")
    public ResponseEntity<Object> changeRoleUser(@PathVariable(value = "id") Long id) {
        return userService.changeRoleUser(id);
    }

    @ApiOperation(value = "Change role to MODERATOR", response = UserDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Change role to MODERATOR"),
    })
    @PutMapping(value = "/role/moderator/{id}")
    public ResponseEntity<Object> changeRoleModerator(@PathVariable(value = "id") Long id) {
        return userService.changeRoleModerator(id);
    }

    @ApiOperation(value = "Delete user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success delete user"),
    })
    @DeleteMapping(value = "/user/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "id") Long id) {
        return userService.deleteUser(id);
    }

    @ApiOperation(value = "Get all report thread", response = ThreadReportDtoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Get all report thread"),
    })
    @GetMapping(value = "/report")
    public ResponseEntity<Object> getAll(@PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC, page = 0, size = 10)  Pageable pageable) {
        return threadReportService.getAllReport(pageable);
    }

    @ApiOperation(value = "Get all report comment", response = CommentReportDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Get all report comment"),
    })
    @GetMapping(value = "/report_comment")
    public ResponseEntity<Object> getAllReportComment(@PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC, page = 0, size = 10)  Pageable pageable) {
        return commentReportService.getAllReport(pageable);
    }

}
