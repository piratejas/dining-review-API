package com.piratejas.diningReviewAPI.controllers;

import com.piratejas.diningReviewAPI.models.User;
import com.piratejas.diningReviewAPI.models.UserDTO;
import com.piratejas.diningReviewAPI.repositories.UserRepository;
import com.piratejas.diningReviewAPI.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.piratejas.diningReviewAPI.utils.TestUtils.createValidUser;
import static com.piratejas.diningReviewAPI.utils.UserUtils.convertUserToDTO;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = createValidUser();
    }

    @Test
    void testAddUser_ValidUser() {
        doNothing().when(userService).addUser(user);

        assertDoesNotThrow(() -> userController.addUser(user));
        verify(userService, times(1)).addUser(user);
    }

    @Test
    void testAddUser_NameAlreadyInUse() {
        doThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Username is already in use."))
                .when(userService)
                .addUser(user);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userController.addUser(user));

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("Username is already in use.", exception.getReason());
        verify(userService, times(1)).addUser(user);
    }

    @Test
    void testAddUser_MissingUsername() {
        user.setUsername(null);
        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is missing."))
                .when(userService)
                .addUser(user);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userController.addUser(user));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Username is missing.", exception.getReason());
        verify(userService, times(1)).addUser(user);
    }

    @Test
    void testGetAllUsers_OK() {
        List<UserDTO> users = new ArrayList<>();
        users.add(convertUserToDTO(user));

        when(userService.getAllUsers()).thenReturn(users);

        List<UserDTO> result = userController.getAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("User123", result.get(0).getName());
        assertEquals("New York", result.get(0).getCity());
        assertEquals("NY", result.get(0).getCounty());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testGetUser_ValidUsername() {
        when(userService.loadUserByUsername(user.getUsername())).thenReturn(user);

        assertDoesNotThrow(() -> userController.getUser(user.getUsername()));
        verify(userService, times(1)).loadUserByUsername(user.getUsername());
    }

    @Test
    void testGetUser_InvalidUsername() {
        when(userService.loadUserByUsername(user.getUsername())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Username not found."));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userController.getUser(user.getUsername()));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Username not found.", exception.getReason());
        verify(userService, times(1)).loadUserByUsername(user.getUsername());
    }

    @Test
    void testUpdateUser_OK() {
        User userUpdate = createValidUser();
        userUpdate.setCity("Chicago");
        userUpdate.setCounty("IL");
        userUpdate.setPostCode("19019");

        assertDoesNotThrow(() -> userController.updateUser(user.getUsername(), userUpdate));
        verify(userService, times(1)).updateUser(user.getUsername(), userUpdate);
    }

    @Test
    void testUpdateUser_UsernameNotFound() {
        User userUpdate = createValidUser();
        userUpdate.setCity("Chicago");
        userUpdate.setCounty("IL");
        userUpdate.setPostCode("19019");

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Username not found."))
                .when(userService)
                .updateUser(user.getUsername(), userUpdate);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userController.updateUser(user.getUsername(), userUpdate));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Username not found.", exception.getReason());
        verify(userService, times(1)).updateUser(user.getUsername(), userUpdate);
    }

    @Test
    void testUpdateUser_MissingNameInUpdate() {
        User userUpdate = createValidUser();
        userUpdate.setUsername(null);
        userUpdate.setCity("Chicago");
        userUpdate.setCounty("IL");
        userUpdate.setPostCode("19019");

        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is missing."))
                .when(userService)
                .updateUser(user.getUsername(), userUpdate);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userController.updateUser(user.getUsername(), userUpdate));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Username is missing.", exception.getReason());
        verify(userService, times(1)).updateUser(user.getUsername(), userUpdate);
    }
}
