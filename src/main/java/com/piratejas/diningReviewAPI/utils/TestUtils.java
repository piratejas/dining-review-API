package com.piratejas.diningReviewAPI.utils;

import com.piratejas.diningReviewAPI.enums.CuisineType;
import com.piratejas.diningReviewAPI.enums.ReviewStatus;
import com.piratejas.diningReviewAPI.models.Restaurant;
import com.piratejas.diningReviewAPI.models.Review;
import com.piratejas.diningReviewAPI.models.User;

import java.util.ArrayList;
import java.util.List;

public class TestUtils {

    public static Review createReviewWithScores() {
        Review review = new Review();
        review.setRestaurantId(1L);
        review.setPeanutScore(4f);
        review.setEggScore(3f);
        review.setDairyScore(4f);
        review.setStatus(ReviewStatus.PENDING);
        return review;
    }

    public static Restaurant createValidRestaurant() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Testaurant");
        restaurant.setType(CuisineType.THAI);
        restaurant.setAddressLine1("123 Main St");
        restaurant.setCity("Sample City");
        restaurant.setState("CA");
        restaurant.setZipCode("78910");
        restaurant.setPhoneNumber("123-456-7890");
        restaurant.setWebsite("http://www.testaurant.com");
        return restaurant;
    }

    public static Review createValidReview() {
        Review review = new Review();
        review.setSubmittedBy("User123");
        review.setRestaurantId(1L);
        review.setDairyScore(5f);
        return review;
    }

    public static User createValidUser() {
        User user = new User();
        user.setName("User123");
        user.setCity("New York");
        user.setState("NY");
        user.setZipCode("10001");
        user.setPeanutAllergy(false);
        user.setEggAllergy(true);
        user.setDairyAllergy(false);
        return user;
    }

    public static List<Review> createReviewsList() {
        List<Review> reviews = new ArrayList<>();
        Long restaurantId = 1L;

        // Review 1
        Review review1 = new Review();
        review1.setRestaurantId(restaurantId);
        review1.setPeanutScore(4f);
        review1.setEggScore(3f);
        review1.setDairyScore(4f);
        reviews.add(review1);

        // Review 2
        Review review2 = new Review();
        review2.setRestaurantId(restaurantId);
        review2.setPeanutScore(3f);
        review2.setEggScore(4f);
        review2.setDairyScore(3f);
        reviews.add(review2);

        // Review 3
        Review review3 = new Review();
        review3.setRestaurantId(restaurantId);
        review3.setPeanutScore(2f);
        review3.setEggScore(3f);
        review3.setDairyScore(3f);
        reviews.add(review3);

        return reviews;
    }

    public static List<Restaurant> createRestaurantsWithAllergyScores() {
        List<Restaurant> restaurants = new ArrayList<>();

        Restaurant restaurant1 = createValidRestaurant();
        restaurant1.setName("Restaurant 1");
        restaurant1.setType(CuisineType.AMERICAN);
        restaurant1.setPeanutScore(4.5f);
        restaurant1.setDairyScore(3.5f);
        restaurant1.setEggScore(4.0f);
        restaurant1.setOverallScore(4.0f);
        restaurants.add(restaurant1);

        Restaurant restaurant2 = createValidRestaurant();
        restaurant2.setName("Restaurant 2");
        restaurant2.setType(CuisineType.ITALIAN);
        restaurant2.setPeanutScore(4.2f);
        restaurant2.setDairyScore(3.8f);
        restaurant2.setEggScore(4.1f);
        restaurant2.setOverallScore(4.1f);
        restaurants.add(restaurant2);

        Restaurant restaurant3 = createValidRestaurant();
        restaurant2.setName("Restaurant 3");
        restaurant2.setType(CuisineType.LEVANTINE);
        restaurant2.setPeanutScore(4.2f);
        restaurant2.setDairyScore(3.8f);
        restaurant2.setEggScore(4.1f);
        restaurant2.setOverallScore(4.1f);
        restaurants.add(restaurant3);

        return restaurants;
    }
}
