package com.piratejas.diningReviewAPI.utils;

import com.piratejas.diningReviewAPI.enums.CuisineType;
import com.piratejas.diningReviewAPI.models.Restaurant;
import com.piratejas.diningReviewAPI.models.RestaurantDTO;
import com.piratejas.diningReviewAPI.models.Review;
import com.piratejas.diningReviewAPI.repositories.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static com.piratejas.diningReviewAPI.utils.RestaurantUtils.*;
import static com.piratejas.diningReviewAPI.utils.TestUtils.createReviewsList;
import static com.piratejas.diningReviewAPI.utils.TestUtils.createValidRestaurant;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RestaurantUtilsTest {

    private RestaurantRepository restaurantRepository;

    @BeforeEach
    public void setUp() {
        restaurantRepository = mock(RestaurantRepository.class);
    }

    @Test
    public void testValidateNewRestaurant_ValidRestaurant() {
        Restaurant validRestaurant = createValidRestaurant();
        when(restaurantRepository.findByNameAndZipCode(anyString(), anyString())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> validateNewRestaurant(validRestaurant, restaurantRepository));
    }

    @Test
    public void testValidateNewRestaurant_NullName() {
        Restaurant restaurant = createValidRestaurant();
        restaurant.setName(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> validateNewRestaurant(restaurant, restaurantRepository));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Restaurant name is missing.", exception.getReason());
    }

    @Test
    public void testValidateNewRestaurant_InvalidZipCode() {
        Restaurant restaurant = createValidRestaurant();
        restaurant.setZipCode("invalid-zip");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> validateNewRestaurant(restaurant, restaurantRepository));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Invalid zip code.", exception.getReason());
    }

    @Test
    public void testValidateNewRestaurant_DuplicateRestaurant() {
        Restaurant existingRestaurant = createValidRestaurant();
        when(restaurantRepository.findByNameAndZipCode(anyString(), anyString())).thenReturn(Optional.of(existingRestaurant));

        Restaurant restaurant = createValidRestaurant();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> validateNewRestaurant(restaurant, restaurantRepository));
        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("Restaurant already exists.", exception.getReason());
    }

    @Test
    public void testValidateZipCode_ValidZipCode() {
        assertDoesNotThrow(() -> validateZipCode("12345"));
    }

    @Test
    public void testValidateZipCode_InvalidZipCode() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> validateZipCode("invalid-zip-code"));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Invalid zip code.", exception.getReason());
    }

    @Test
    public void testConvertRestaurantToDTO() {
        Restaurant restaurant = createValidRestaurant();
        restaurant.setPeanutScore(4.5f);
        restaurant.setDairyScore(3.5f);
        restaurant.setEggScore(4.0f);
        restaurant.setOverallScore(4.0f);

        RestaurantDTO restaurantDTO = convertRestaurantToDTO(restaurant);

        assertEquals("Testaurant", restaurantDTO.getName());
        assertSame(CuisineType.THAI, restaurantDTO.getType());
        assertEquals("123 Main St", restaurantDTO.getAddressLine1());
        assertEquals("Sample City", restaurantDTO.getCity());
        assertEquals("CA", restaurantDTO.getState());
        assertEquals("78910", restaurantDTO.getZipCode());
        assertEquals("123-456-7890", restaurantDTO.getPhoneNumber());
        assertEquals("http://www.testaurant.com", restaurantDTO.getWebsite());
        assertEquals("4.50", restaurantDTO.getPeanutScore());
        assertEquals("3.50", restaurantDTO.getDairyScore());
        assertEquals("4.00", restaurantDTO.getEggScore());
        assertEquals("4.00", restaurantDTO.getOverallScore());
    }

    @Test
    void testUpdateRestaurantScores() {
        Restaurant restaurant = createValidRestaurant();
        List<Review> reviews = createReviewsList();

        Restaurant updatedRestaurant = updateRestaurantScores(restaurant, reviews);

        assertNotNull(updatedRestaurant);
        assertEquals(expectedPeanutScore, updatedRestaurant.getPeanutScore());
        assertEquals(expectedDairyScore, updatedRestaurant.getDairyScore());
        assertEquals(expectedEggScore, updatedRestaurant.getEggScore());
        assertEquals(expectedOverallScore, updatedRestaurant.getOverallScore());
    }

    Float expectedPeanutScore = (4f + 3f + 2f) / 3;
    Float expectedEggScore = (3f + 4f + 3f) / 3;
    Float expectedDairyScore = (4f + 3f + 3f) / 3;
    Float expectedOverallScore = (4f + 3f + 2f + 3f + 4f + 3f + 4f + 3f + 3f) / 9;
}