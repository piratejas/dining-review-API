package com.piratejas.diningReviewAPI.controllers;


import com.piratejas.diningReviewAPI.models.LoginResponseDTO;
import com.piratejas.diningReviewAPI.models.LoginDTO;
import com.piratejas.diningReviewAPI.models.RegistrationDTO;
import com.piratejas.diningReviewAPI.models.User;
import com.piratejas.diningReviewAPI.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public void registerUser(@RequestBody RegistrationDTO newUser) {
        authenticationService.registerUser(newUser);
    }

    @PostMapping("/login")
    public LoginResponseDTO loginUser(@RequestBody LoginDTO body){
        return authenticationService.loginUser(body.getUsername(), body.getPassword());
    }
}
