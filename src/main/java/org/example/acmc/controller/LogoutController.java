package org.example.acmc.controller;

import org.example.acmc.service.LogoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogoutController {

    private LogoutService logoutService;

    @Autowired
    public LogoutController(LogoutService logoutService) {
        super();
        this.logoutService = logoutService;
    }

    @PutMapping("/signOut")
    public ResponseEntity<String> regenerateOtp(@RequestBody String email) {
        return new ResponseEntity<>(logoutService.logout(email), HttpStatus.OK);
    }
}
