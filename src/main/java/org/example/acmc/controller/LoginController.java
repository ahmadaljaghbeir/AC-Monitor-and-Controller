package org.example.acmc.controller;


import org.example.acmc.model.AuthResponse;
import org.example.acmc.model.UserEntity;
import org.example.acmc.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    private LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        super();
        this.loginService = loginService;
    }

    @PostMapping(value = "/auth")
    public ResponseEntity<String> login(@RequestBody UserEntity userEntity) {
        return new ResponseEntity<String>(loginService.auth(userEntity), HttpStatus.OK);
    }

    @PostMapping(value = "/mfa")
    public ResponseEntity<String> twoFactorAuth(@RequestBody UserEntity userEntity) {
        return new ResponseEntity<String>(loginService.twoFactorAuth(userEntity), HttpStatus.OK);
    }

    @GetMapping(value = "/verify-account")
    public ResponseEntity<String> verify(@RequestParam String email, @RequestParam String otp) {
        return new ResponseEntity<>(loginService.verifyAccount(email, otp), HttpStatus.OK);
    }

    @GetMapping(value = "/verify")
    public ResponseEntity<AuthResponse> verify(@RequestParam String email) {
        return new ResponseEntity<AuthResponse>(loginService.verifyOtp(email), HttpStatus.OK);
    }
}