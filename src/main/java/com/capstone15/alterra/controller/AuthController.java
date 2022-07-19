package com.capstone15.alterra.controller;

import com.capstone15.alterra.constant.AppConstant;
import com.capstone15.alterra.domain.dto.UserDto;
import com.capstone15.alterra.domain.dto.payload.TokenResponse;
import com.capstone15.alterra.domain.dto.payload.UsernamePassword;
import com.capstone15.alterra.domain.dto.payload.UsernamePasswordFGD;
import com.capstone15.alterra.service.AuthService;
import com.capstone15.alterra.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/auth")
@RequiredArgsConstructor
@Api(tags = "Auth")
public class AuthController {

    private final AuthService authService;

    @ApiOperation(value = "Register user", response = UsernamePassword.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Register user"),
    })
    @PostMapping(value="/register")
    public ResponseEntity<Object> register(@RequestBody UsernamePasswordFGD req) {
        return authService.register(req);

    }

    @ApiOperation(value = "Login user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Login user"),
    })
    @PostMapping(value="/login" )
    public ResponseEntity<?> login(@RequestBody UsernamePassword req) {
        TokenResponse token = authService.generateToken(req);
        return ResponseUtil.build(AppConstant.Message.SUCCESS, token, HttpStatus.OK);
    }
}
