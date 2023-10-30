package com.piratejas.diningReviewAPI.utils;

import com.piratejas.diningReviewAPI.models.Restaurant;
import com.piratejas.diningReviewAPI.models.Review;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantUtilsTest {

    @Test
    void testUpdateRestaurantScores() {
        Restaurant restaurant = new Restaurant();
        List<Review> reviews = TestUtils.createReviewsList();

        Restaurant updatedRestaurant = RestaurantUtils.updateRestaurantScores(restaurant, reviews);

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