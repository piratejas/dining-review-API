package com.piratejas.diningReviewAPI.controllers;

import com.piratejas.diningReviewAPI.models.User;
import com.piratejas.diningReviewAPI.models.UserDTO;
import com.piratejas.diningReviewAPI.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.piratejas.diningReviewAPI.utils.TestUtils.createValidUser;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserRepository userRepository;

    User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = createValidUser();
    }

    @Test
    void testAddUser_ValidUser() {
        when(userRepository.findByName(user.getName())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> userController.addUser(user));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testAddUser_NameAlreadyInUse() {
        when(userRepository.findByName(user.getName())).thenReturn(Optional.of(user));
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userController.addUser(user));

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("Username is already in use.", exception.getReason());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testAddUser_MissingUsername() {
        user.setName(null);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userController.addUser(user));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Username is missing.", exception.getReason());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testGetAllUsers_OK() {
        List<User> users = new ArrayList<>();
        users.add(user);
        when(userRepository.findAll()).thenReturn(users);

        List<UserDTO> result = userController.getAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("User123", result.get(0).getName());
        assertEquals("New York", result.get(0).getCity());
        assertEquals("NY", result.get(0).getState());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUser_ValidUsername() {
        when(userRepository.findByName(user.getName())).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> userController.getUser(user.getName()));
        verify(userRepository, times(1)).findByName(user.getName());
    }

    @Test
    void testGetUser_InvalidUsername() {
        when(userRepository.findByName(user.getName())).thenReturn(Optional.empty());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userController.getUser(user.getName()));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Username not found.", exception.getReason());
        verify(userRepository, times(1)).findByName(user.getName());
    }

    @Test
    void testGetUser_ReturnsCorrectUser() {
        when(userRepository.findByName(user.getName())).thenReturn(Optional.of(user));
        User result = userController.getUser(user.getName());

        assertEquals(User.class, result.getClass());
        assertEquals("User123", result.getName());
        assertEquals("New York", result.getCity());
        assertEquals("NY", result.getState());
        assertEquals("10001", result.getZipCode());
        verify(userRepository, times(1)).findByName(user.getName());
    }

    @Test
    void testUpdateUser_OK() {
        User userUpdate = createValidUser();
        userUpdate.setCity("Chicago");
        userUpdate.setState("IL");
        userUpdate.setCity("19019");
        when(userRepository.findByName(user.getName())).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> userController.updateUser(user.getName(), userUpdate));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateUser_UsernameNotFound() {
        User userUpdate = createValidUser();
        userUpdate.setCity("Chicago");
        userUpdate.setState("IL");
        userUpdate.setCity("19019");
        when(userRepository.findByName(user.getName())).thenReturn(Optional.empty());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userController.updateUser(user.getName(), userUpdate));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Username not found.", exception.getReason());
        verify(userRepository, never()).save(user);
    }

    @Test
    void testUpdateUser_MissingNameInUpdate() {
        User userUpdate = createValidUser();
        userUpdate.setName(null);
        userUpdate.setCity("Chicago");
        userUpdate.setState("IL");
        userUpdate.setCity("19019");
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userController.updateUser(user.getName(), userUpdate));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Username is missing.", exception.getReason());
        verify(userRepository, never()).save(any());
    }


}