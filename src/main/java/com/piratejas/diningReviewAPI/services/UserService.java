package com.piratejas.diningReviewAPI.services;

import java.util.ArrayList;
import java.util.List;

import com.piratejas.diningReviewAPI.models.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.piratejas.diningReviewAPI.models.User;
import com.piratejas.diningReviewAPI.repositories.UserRepository;
import org.springframework.web.server.ResponseStatusException;

import static com.piratejas.diningReviewAPI.utils.UserUtils.*;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public void addUser(User user) {
        validateNewUser(user, userRepository);
        userRepository.save(user);
    }

    public List<UserDTO> getAllUsers() {
        Iterable<User> allUsers = userRepository.findAll();
        List<UserDTO> allUsersDTO = new ArrayList<>();
        for (User user : allUsers) {
            UserDTO userDTO = convertUserToDTO(user);
            allUsersDTO.add(userDTO);
        }
        return allUsersDTO;
    }

    public User getUserByUsername(String username) {
        validateNameInRequest(username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Username not found."));
        return user;
    }

    public void updateUser(String username, User userUpdate) {
        validateNameInRequest(userUpdate.getUsername());
        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Username not found."));
        patchExistingUser(existingUser, userUpdate);
        userRepository.save(existingUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("In the user details service");

        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found."));
    }

}
