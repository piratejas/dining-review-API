package com.piratejas.diningReviewAPI.controllers;


import com.piratejas.diningReviewAPI.models.RegistrationDTO;
import com.piratejas.diningReviewAPI.models.User;
import com.piratejas.diningReviewAPI.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public User registerUser(@RequestBody User body) {
        return authenticationService.registerUser(body.getUsername(), body.getPassword());
    }
}
