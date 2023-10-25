package com.piratejas.diningReviewAPI.controllers;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.piratejas.diningReviewAPI.models.Restaurant;
import com.piratejas.diningReviewAPI.models.Review;
import com.piratejas.diningReviewAPI.models.User;
import com.piratejas.diningReviewAPI.repositories.RestaurantRepository;
import com.piratejas.diningReviewAPI.repositories.ReviewRepository;
import com.piratejas.diningReviewAPI.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

public class ReviewControllerTest {
    private ReviewController reviewController;
    private ReviewRepository reviewRepository;
    private UserRepository userRepository;
    private RestaurantRepository restaurantRepository;

    @BeforeEach
    public void setUp() {
        reviewRepository = mock(ReviewRepository.class);
        userRepository = mock(UserRepository.class);
        restaurantRepository = mock(RestaurantRepository.class);
        reviewController = new ReviewController(reviewRepository, userRepository, restaurantRepository);
    }

    @Test
    public void testAddReview_ValidReview() {
        Review review = createValidReview();
        when(restaurantRepository.findById(review.getRestaurantId())).thenReturn(Optional.of(new Restaurant()));
        when(userRepository.findByName(review.getSubmittedBy())).thenReturn(Optional.of(new User()));

        assertDoesNotThrow(() -> reviewController.addReview(review));
        verify(reviewRepository, times(1)).save(review);
    }

    @Test
    public void testAddReview_InvalidRestaurantId() {
        Review review = createValidReview();
        when(restaurantRepository.findById(review.getRestaurantId())).thenReturn(Optional.empty());
        when(userRepository.findByName(review.getSubmittedBy())).thenReturn(Optional.of(new User()));
        Throwable e = assertThrows(ResponseStatusException.class, () -> reviewController.addReview(review));

        assertEquals("422 UNPROCESSABLE_ENTITY \"Invalid restaurant Id.\"", e.getMessage());
        verify(reviewRepository, never()).save(review);
    }

    @Test
    public void testAddReview_MissingUserInfo() {
        Review review = new Review();
        review.setRestaurantId(1L);

        assertThrows(ResponseStatusException.class, () -> reviewController.addReview(review));
        verify(reviewRepository, never()).save(review);
    }

    @Test
    public void testAddReview_MissingRatings() {
        Review review = new Review();
        review.setRestaurantId(1L);
        review.setSubmittedBy("User123");

        assertThrows(ResponseStatusException.class, () -> reviewController.addReview(review));
        verify(reviewRepository, never()).save(review);
    }

    @Test
    public void testAddReview_UserNotRegistered() {
        Review review = createValidReview();
        when(restaurantRepository.findById(review.getRestaurantId())).thenReturn(Optional.of(new Restaurant()));
        when(userRepository.findByName(review.getSubmittedBy())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> reviewController.addReview(review));
        verify(reviewRepository, never()).save(review);
    }

    private Review createValidReview() {
        Review review = new Review();
        review.setSubmittedBy("User123");
        review.setRestaurantId(1L);
        review.setDairyScore(5);
        return review;
    }
}
