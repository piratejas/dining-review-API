package com.piratejas.diningReviewAPI.controllers;


import com.piratejas.diningReviewAPI.models.User;
import com.piratejas.diningReviewAPI.models.UserDTO;
import com.piratejas.diningReviewAPI.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestMapping("/users")
@RestController
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addUser(@RequestBody User user) {
        validateNewUser(user);
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
    public UserDTO getUser(@PathVariable("userName") String name) {
        validateNameInRequest(name);

        Optional<User> optionalExistingUser = userRepository.findByName(name);
        if (optionalExistingUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username not found.");
        }

        User existingUser = optionalExistingUser.get();

        return convertUserToDTO(existingUser);
    }

    @PutMapping("/{userName}")
    @ResponseStatus(HttpStatus.OK)
    public void updateUser(@PathVariable("userName") String name, @RequestBody User userUpdate) {
        validateNameInRequest(userUpdate.getName());

        Optional<User> optionalExistingUser = userRepository.findByName(name);
        if (optionalExistingUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username not found.");
        }

        User existingUser = optionalExistingUser.get();
        patchExistingUser(existingUser, userUpdate);
        userRepository.save(existingUser);
    }

    // Helper functions
    private void validateNewUser(User user) {
        validateNameInRequest(user.getName());

        // Check if username already exists
        Optional<User> existingUser = userRepository.findByName(user.getName());
        if (existingUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already in use.");
        }
    }

    private void validateNameInRequest(String name) {
        if (ObjectUtils.isEmpty(name)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is missing.");
        }
    }

    private void patchExistingUser(User existingUser, User userUpdate) {
        if (ObjectUtils.isEmpty(userUpdate.getName())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Username is missing.");
        }
        if (!ObjectUtils.isEmpty(userUpdate.getCity())) {
            existingUser.setCity(userUpdate.getCity());
        }
        if (!ObjectUtils.isEmpty(userUpdate.getState())) {
            existingUser.setState(userUpdate.getState());
        }
        if (!ObjectUtils.isEmpty(userUpdate.getZipCode())) {
            existingUser.setZipCode(userUpdate.getZipCode());
        }
        if (!ObjectUtils.isEmpty(userUpdate.getEggAllergy())) {
            existingUser.setEggAllergy(userUpdate.getEggAllergy());
        }
        if (!ObjectUtils.isEmpty(userUpdate.getDairyAllergy())) {
            existingUser.setDairyAllergy(userUpdate.getDairyAllergy());
        }
        if (!ObjectUtils.isEmpty(userUpdate.getPeanutAllergy())) {
            existingUser.setPeanutAllergy(userUpdate.getPeanutAllergy());
        }
    }

    private UserDTO convertUserToDTO(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setName(user.getName());
        userDTO.setCity(user.getCity());
        userDTO.setState(user.getState());

        return userDTO;
    }
}
