package com.piratejas.diningReviewAPI.services;

import com.piratejas.diningReviewAPI.errors.exceptions.LoginException;
import com.piratejas.diningReviewAPI.errors.exceptions.UsernameConflictException;
import com.piratejas.diningReviewAPI.models.*;
import com.piratejas.diningReviewAPI.repositories.RoleRepository;
import com.piratejas.diningReviewAPI.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Set;

import static com.piratejas.diningReviewAPI.utils.UserUtils.validateNewUser;

@Service
@Transactional
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthenticationService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public void registerUser(RegistrationDTO newUser) {

        try {
            String encodedPassword = passwordEncoder.encode(newUser.getPassword());

            Role userRole = roleRepository.findByAuthority("USER").orElseThrow(() -> new IllegalStateException("Default role not found"));

            Set<Role> authorities = new HashSet<>();
            authorities.add(userRole);

            User user = new User(0L, newUser.getUsername(), encodedPassword, authorities, newUser.getCity(), newUser.getCounty(), newUser.getPostCode(), newUser.getPeanutAllergy(), newUser.getEggAllergy(), newUser.getDairyAllergy());

            validateNewUser(user, userRepository);

            userRepository.save(user);
        } catch (ResponseStatusException e) {
            System.out.println(e);
            throw new UsernameConflictException(e.getReason());
        }
    }

    public ResponseEntity<LoginResponseDTO> loginUser(String username, String password) {

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            String token = tokenService.generateJwt(auth);

            return ResponseEntity.ok(new LoginResponseDTO(username, token));

        } catch(AuthenticationException e) {
            throw new LoginException("Authentication failed");
        }
    }

}
