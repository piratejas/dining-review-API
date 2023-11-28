package com.piratejas.diningReviewAPI.utils;

import com.piratejas.diningReviewAPI.models.User;
import com.piratejas.diningReviewAPI.models.UserDTO;
import com.piratejas.diningReviewAPI.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static com.piratejas.diningReviewAPI.utils.TestUtils.createValidUser;
import static com.piratejas.diningReviewAPI.utils.UserUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserUtilsTest {

    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository = mock(UserRepository.class);
    }

    @Test
    public void testValidateNewUser_ValidUser() {
        User validUser = createValidUser();
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        validateNewUser(validUser, userRepository);
    }

    @Test
    public void testValidateNewUser_NullName() {
        User user = createValidUser();
        user.setUsername(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> validateNewUser(user, userRepository));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Username is missing.", exception.getReason());
    }

    @Test
    public void testValidateNewUser_DuplicateUsername() {
        User existingUser = createValidUser();
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(existingUser));

        User user = createValidUser();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> validateNewUser(user, userRepository));
        assertSame(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("Username is already in use.", exception.getReason());
    }

    @Test
    public void testPatchExistingUser() {
        User existingUser = createValidUser();
        User userUpdate = createValidUser();
        userUpdate.setUsername("Bob");
        userUpdate.setCity("New City");
        userUpdate.setCounty("NS");
        userUpdate.setPostCode("67890");
        userUpdate.setPeanutAllergy(false);
        userUpdate.setEggAllergy(true);
        userUpdate.setDairyAllergy(false);

        patchExistingUser(existingUser, userUpdate);

        assertEquals("User123", existingUser.getUsername());
        assertEquals("New City", existingUser.getCity());
        assertEquals("NS", existingUser.getCounty());
        assertEquals("67890", existingUser.getPostCode());
        assertFalse(existingUser.getPeanutAllergy());
        assertTrue(existingUser.getEggAllergy());
        assertFalse(existingUser.getDairyAllergy());
    }

    @Test
    public void testConvertUserToDTO() {
        User user = createValidUser();

        UserDTO userDTO = convertUserToDTO(user);

        assertEquals("User123", userDTO.getName());
        assertEquals("New York", userDTO.getCity());
        assertEquals("NY", userDTO.getCounty());
    }

}