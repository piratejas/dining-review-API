package com.piratejas.diningReviewAPI.controllers;

import static com.piratejas.diningReviewAPI.utils.TestUtils.createValidRestaurant;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.piratejas.diningReviewAPI.enums.CuisineType;
import com.piratejas.diningReviewAPI.models.Restaurant;
import com.piratejas.diningReviewAPI.models.RestaurantDTO;
import com.piratejas.diningReviewAPI.repositories.RestaurantRepository;
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


class RestaurantControllerTest {

    @InjectMocks
    private RestaurantController restaurantController;

    @Mock
    private RestaurantRepository restaurantRepository;

    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        restaurant = createValidRestaurant();
    }

    @Test
    void testAddRestaurant_ValidRestaurant() {
        when(restaurantRepository.findByNameAndZipCode(restaurant.getName(), restaurant.getZipCode()))
                .thenReturn(Optional.empty());

        assertDoesNotThrow(() -> restaurantController.addRestaurant(restaurant));
        verify(restaurantRepository, times(1)).save(restaurant);
    }

    @Test
    void testAddRestaurant_DuplicateRestaurant() {
        when(restaurantRepository.findByNameAndZipCode(restaurant.getName(), restaurant.getZipCode()))
                .thenReturn(Optional.of(restaurant));
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> restaurantController.addRestaurant(restaurant));

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("Restaurant already exists.", exception.getReason());
        verify(restaurantRepository, never()).save(any());
    }

    @Test
    void testAddRestaurant_MissingName() {
        restaurant.setName(null);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> restaurantController.addRestaurant(restaurant));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Restaurant name is missing.", exception.getReason());
        verify(restaurantRepository, never()).save(any());
    }

    @Test
    void testAddRestaurant_InvalidZipCode() {
        restaurant.setZipCode("12345A");
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> restaurantController.addRestaurant(restaurant));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Invalid zip code.", exception.getReason());
        verify(restaurantRepository, never()).save(any());
    }

    @Test
    void testGetAllRestaurants_OK() {
        List<Restaurant> restaurants = new ArrayList<>();
        restaurants.add(restaurant);
        when(restaurantRepository.findAll()).thenReturn(restaurants);

        List<RestaurantDTO> result = restaurantController.getAllRestaurants();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Testaurant", result.get(0).getName());
        assertEquals(CuisineType.THAI, result.get(0).getType());
        verify(restaurantRepository, times(1)).findAll();
    }

    @Test
    void testGetRestaurantById_ValidId() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        assertDoesNotThrow(() -> restaurantController.getRestaurantById(1L));
        verify(restaurantRepository, times(1)).findById(1L);
    }

    @Test
    void testGetRestaurantById_InvalidId() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> restaurantController.getRestaurantById(1L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Restaurant not found.", exception.getReason());
        verify(restaurantRepository, times(1)).findById(1L);
    }

    @Test
    void testGetRestaurantById_ReturnsCorrectDTO() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        RestaurantDTO result = restaurantController.getRestaurantById(1L);

        assertEquals(RestaurantDTO.class, result.getClass());
        assertEquals("Testaurant", result.getName());
        assertEquals(CuisineType.THAI, result.getType());
        verify(restaurantRepository, times(1)).findById(1L);
    }

    @Test
    void testSearchRestaurants_AllergyNotSpecified() {
        String zipCode = "12345";
        String allergy = "invalid-allergy";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> restaurantController.searchRestaurants(zipCode, allergy));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Allergy not specified.", exception.getReason());
        verifyNoInteractions(restaurantRepository);
    }

    @Test
    void testSearchRestaurants_InvalidZipCode() {
        String zipCode = "invalid-zip";
        String allergy = "peanut";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> restaurantController.searchRestaurants(zipCode, allergy));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Invalid zip code.", exception.getReason());
        verifyNoInteractions(restaurantRepository);
    }
}