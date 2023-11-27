package com.piratejas.diningReviewAPI.controllers;


import com.piratejas.diningReviewAPI.models.User;
import com.piratejas.diningReviewAPI.models.UserDTO;
import com.piratejas.diningReviewAPI.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.piratejas.diningReviewAPI.utils.UserUtils.*;

@RequestMapping("/users")
@RestController
//@CrossOrigin("*")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addUser(@RequestBody User user) {
        validateNewUser(user, userRepository);
        userRepository.save(user);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> getAllUsers() {
        Iterable<User> allUsers = userRepository.findAll();
        List<UserDTO> allUsersDTO = new ArrayList<>();
        for (User user : allUsers) {
            UserDTO userDTO = convertUserToDTO(user);
            allUsersDTO.add(userDTO);
        }
        return allUsersDTO;
    }

    @GetMapping("/{userName}")
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable("userName") String name) {
        validateNameInRequest(name);

        Optional<User> optionalExistingUser = userRepository.findByUsername(name);
        if (optionalExistingUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username not found.");
        }

        return optionalExistingUser.get();
    }

    @PutMapping("/{userName}")
    @ResponseStatus(HttpStatus.OK)
    public void updateUser(@PathVariable("userName") String name, @RequestBody User userUpdate) {
        validateNameInRequest(userUpdate.getUsername());

        Optional<User> optionalExistingUser = userRepository.findByUsername(name);
        if (optionalExistingUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username not found.");
        }

        User existingUser = optionalExistingUser.get();
        patchExistingUser(existingUser, userUpdate);
        userRepository.save(existingUser);
    }
}
