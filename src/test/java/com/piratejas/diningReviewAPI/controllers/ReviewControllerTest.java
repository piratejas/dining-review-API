package com.piratejas.diningReviewAPI.controllers;

import static com.piratejas.diningReviewAPI.utils.TestUtils.createValidReview;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

public class ReviewControllerTest {

    @InjectMocks
    private ReviewController reviewController;

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RestaurantRepository restaurantRepository;

    private Review review;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        review = createValidReview();
    }

    @Test
    void testAddReview_ValidReview() {
        when(restaurantRepository.findById(review.getRestaurantId())).thenReturn(Optional.of(new Restaurant()));
        when(userRepository.findByName(review.getSubmittedBy())).thenReturn(Optional.of(new User()));

        assertDoesNotThrow(() -> reviewController.addReview(review));
        verify(reviewRepository, times(1)).save(review);
    }

    @Test
    void testAddReview_InvalidRestaurantId() {
        when(restaurantRepository.findById(review.getRestaurantId())).thenReturn(Optional.empty());
        when(userRepository.findByName(review.getSubmittedBy())).thenReturn(Optional.of(new User()));
        ResponseStatusException e = assertThrows(ResponseStatusException.class, () -> reviewController.addReview(review));

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, e.getStatusCode());
        assertEquals("Invalid restaurant Id.", e.getReason());
        verify(reviewRepository, never()).save(review);
    }

    @Test
    void testAddReview_MissingUserInfo() {
        review.setSubmittedBy(null);
        ResponseStatusException e = assertThrows(ResponseStatusException.class, () -> reviewController.addReview(review));

        assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        assertEquals("User information is missing.", e.getReason());
        verify(reviewRepository, never()).save(review);
    }

    @Test
    void testAddReview_MissingRestaurantId() {
        review.setRestaurantId(null);
        ResponseStatusException e = assertThrows(ResponseStatusException.class, () -> reviewController.addReview(review));

        assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        assertEquals("Restaurant Id is missing.", e.getReason());
        verify(reviewRepository, never()).save(review);
    }

    @Test
    void testAddReview_MissingRatings() {
        review.setDairyScore(null);
        ResponseStatusException e = assertThrows(ResponseStatusException.class, () -> reviewController.addReview(review));

        assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        assertEquals("Review must contain at least one rating.", e.getReason());
        verify(reviewRepository, never()).save(review);
    }

    @Test
    void testAddReview_UserNotRegistered() {
        when(restaurantRepository.findById(review.getRestaurantId())).thenReturn(Optional.of(new Restaurant()));
        when(userRepository.findByName(review.getSubmittedBy())).thenReturn(Optional.empty());
        Throwable e = assertThrows(ResponseStatusException.class, () -> reviewController.addReview(review));

        assertEquals("422 UNPROCESSABLE_ENTITY \"User is not registered.\"", e.getMessage());
        verify(reviewRepository, never()).save(review);
    }
}
