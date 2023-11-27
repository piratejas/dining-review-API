package com.piratejas.diningReviewAPI.services;

import com.piratejas.diningReviewAPI.models.Role;
import com.piratejas.diningReviewAPI.models.User;
import com.piratejas.diningReviewAPI.repositories.RoleRepository;
import com.piratejas.diningReviewAPI.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(String username, String password) {

        String encodedPassword = passwordEncoder.encode(password);
        //TODO: register user -> create account with all fields
        Role userRole = roleRepository.findByAuthority("USER").get();

        Set<Role> authorities = new HashSet<>();

        authorities.add(userRole);

        return userRepository.save(new User(0L, username, encodedPassword, authorities));
    }

}
