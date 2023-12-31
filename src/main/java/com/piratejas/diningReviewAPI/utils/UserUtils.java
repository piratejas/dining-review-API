package com.piratejas.diningReviewAPI.utils;

import com.piratejas.diningReviewAPI.errors.exceptions.UsernameConflictException;
import com.piratejas.diningReviewAPI.errors.exceptions.UsernameMissingException;
import com.piratejas.diningReviewAPI.models.User;
import com.piratejas.diningReviewAPI.models.UserDTO;
import com.piratejas.diningReviewAPI.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

public class UserUtils {
    public static void validateNewUser(User user, UserRepository userRepository) {
        validateNameInRequest(user.getUsername());

        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            throw new UsernameConflictException("Username is already in use.");
        }
    }

    public static void validateNameInRequest(String name) {
        if (ObjectUtils.isEmpty(name)) {
            throw new UsernameMissingException("Username is missing.");
        }
    }

    public static void patchExistingUser(User existingUser, User userUpdate) {
        if (!ObjectUtils.isEmpty(userUpdate.getCity())) {
            existingUser.setCity(userUpdate.getCity());
        }
        if (!ObjectUtils.isEmpty(userUpdate.getCounty())) {
            existingUser.setCounty(userUpdate.getCounty());
        }
        if (!ObjectUtils.isEmpty(userUpdate.getPostcode())) {
            existingUser.setPostcode(userUpdate.getPostcode());
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

    public static UserDTO convertUserToDTO(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setName(user.getUsername());
        userDTO.setCity(user.getCity());
        userDTO.setCounty(user.getCounty());

        return userDTO;
    }
}
