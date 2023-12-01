package com.piratejas.diningReviewAPI.controllers;


import com.piratejas.diningReviewAPI.models.User;
import com.piratejas.diningReviewAPI.models.UserDTO;
import com.piratejas.diningReviewAPI.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/users")
@RestController
@CrossOrigin("*")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addUser(@RequestBody User user) {
        userService.addUser(user);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userName}")
    @ResponseStatus(HttpStatus.OK)
    public UserDetails getUser(@PathVariable("userName") String name) {
        return userService.loadUserByUsername(name);
    }

    @PutMapping("/{userName}")
    @ResponseStatus(HttpStatus.OK)
    public void updateUser(@PathVariable("userName") String name, @RequestBody User userUpdate) {
        userService.updateUser(name, userUpdate);
    }
}
