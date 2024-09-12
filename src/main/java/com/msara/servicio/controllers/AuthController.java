package com.msara.servicio.controllers;

import com.msara.servicio.controllers.dto.AuthLoginRequest;
import com.msara.servicio.controllers.dto.AuthRegisterRequest;
import com.msara.servicio.controllers.dto.AuthResponse;
import com.msara.servicio.services.impl.UserDetailServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthLoginRequest userRequest) {
        return new ResponseEntity<AuthResponse>(this.userDetailService.loginUser(userRequest), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid AuthRegisterRequest userRequest){
        return new ResponseEntity<AuthResponse>(this.userDetailService.registerUser(userRequest), HttpStatus.CREATED);
    }
}
