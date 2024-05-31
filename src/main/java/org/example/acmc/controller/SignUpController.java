package org.example.acmc.controller;

import org.example.acmc.model.UserEntity;
import org.example.acmc.service.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SignUpController {

    private SignUpService signUpService;


    @Autowired
    public SignUpController(SignUpService signUpService) {
        super();
        this.signUpService = signUpService;
    }

    @PostMapping(value = "/createUser")
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity userEntity) {
        if (userEntity == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<UserEntity>(signUpService.addUser(userEntity), HttpStatus.CREATED);
    }

    @GetMapping(value = "/getUser/{id}")
    public ResponseEntity<UserEntity> getUser(@PathVariable("id") String email) {
        return new ResponseEntity<UserEntity>(signUpService.getUserByEmail(email), HttpStatus.OK);
    }

    @PutMapping(value = "/updateUser/{id}")
    public ResponseEntity<UserEntity> updateUser(@PathVariable("id") String email, @RequestBody UserEntity userEntity) {
        return new ResponseEntity<UserEntity>(signUpService.updateUser(userEntity, email), HttpStatus.OK);
    }

    @DeleteMapping(value = "/deleteUser/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") long userId) {
        signUpService.deleteUser(userId);
        return new ResponseEntity<String>("User deleted!", HttpStatus.OK);
    }
}
