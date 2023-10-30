package com.piratejas.diningReviewAPI.controllers;

import com.piratejas.diningReviewAPI.enums.ReviewStatus;
import com.piratejas.diningReviewAPI.models.AdminReviewAction;
import com.piratejas.diningReviewAPI.models.Restaurant;
import com.piratejas.diningReviewAPI.models.Review;
import com.piratejas.diningReviewAPI.repositories.RestaurantRepository;
import com.piratejas.diningReviewAPI.repositories.ReviewRepository;
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

import static com.piratejas.diningReviewAPI.utils.TestUtils.createReviewWithScores;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AdminControllerTest {

    @InjectMocks
    private AdminController adminController;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    AdminReviewAction adminReviewAction;
    Review review;
    Long reviewId = 1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adminReviewAction = new AdminReviewAction();
        review = createReviewWithScores();
    }

    @Test
    void testGetPendingReviews_OK() {
        List<Review> expectedReviews = new ArrayList<>();
        Review review1 = createReviewWithScores();
        expectedReviews.add(review1);
        Review review2 = createReviewWithScores();
        expectedReviews.add(review2);

        when(reviewRepository.findByStatus(ReviewStatus.PENDING)).thenReturn(expectedReviews);

        List<Review> actualReviews = adminController.getPendingReviews();

        assertEquals(expectedReviews, actualReviews);
        verify(reviewRepository, times(1)).findByStatus(any());
    }

    @Test
    void testApproveReview_Approve() {
        adminReviewAction.setApproveReview(true);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(restaurantRepository.findById(review.getRestaurantId())).thenReturn(Optional.of(new Restaurant()));

        adminController.approveReview(reviewId, adminReviewAction);

        assertEquals(ReviewStatus.APPROVED, review.getStatus());
        verify(reviewRepository, times(1)).save(review);
        verify(restaurantRepository, times(1)).save(any());
    }

    @Test
    void testApproveReview_Reject() {
        adminReviewAction.setApproveReview(false);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(restaurantRepository.findById(review.getRestaurantId())).thenReturn(Optional.of(new Restaurant()));

        adminController.approveReview(reviewId, adminReviewAction);

        assertEquals(ReviewStatus.REJECTED, review.getStatus());
        verify(reviewRepository, times(1)).save(review);
        verify(restaurantRepository, never()).save(any());
    }

    @Test
    void testApproveReview_ReviewNotFound() {
        adminReviewAction.setApproveReview(true);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());
        ResponseStatusException e = assertThrows(ResponseStatusException.class, () -> adminController.approveReview(reviewId, adminReviewAction));

        assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        assertEquals("Review does not exist.", e.getReason());
        verify(reviewRepository, never()).save(any());
        verify(restaurantRepository, never()).save(any());
    }

    @Test
    void testApproveReview_InvalidRestaurantId() {
        adminReviewAction.setApproveReview(true);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(restaurantRepository.findById(review.getRestaurantId())).thenReturn(Optional.empty());
        ResponseStatusException e = assertThrows(ResponseStatusException.class, () -> adminController.approveReview(reviewId, adminReviewAction));

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, e.getStatusCode());
        assertEquals("Invalid restaurant Id.", e.getReason());
        verify(reviewRepository, never()).save(any());
        verify(restaurantRepository, never()).save(any());
    }
}