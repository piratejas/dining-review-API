package com.piratejas.diningReviewAPI.controllers;

import com.piratejas.diningReviewAPI.enums.ReviewStatus;
import com.piratejas.diningReviewAPI.models.AdminReviewAction;
import com.piratejas.diningReviewAPI.models.Restaurant;
import com.piratejas.diningReviewAPI.models.Review;
import com.piratejas.diningReviewAPI.repositories.RestaurantRepository;
import com.piratejas.diningReviewAPI.repositories.ReviewRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static com.piratejas.diningReviewAPI.utils.RestaurantUtils.updateRestaurantScores;

@RequestMapping("/admin")
@RestController
public class AdminController {
    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;

    public AdminController(ReviewRepository reviewRepository, RestaurantRepository restaurantRepository) {
        this.reviewRepository = reviewRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping("/reviews")
    public List<Review> getPendingReviews() {
        return reviewRepository.findByStatus(ReviewStatus.PENDING);
    }

    @PutMapping("/reviews/{reviewId}")
    public void approveReview(@PathVariable Long reviewId, @RequestBody AdminReviewAction adminReviewAction) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);

        if (optionalReview.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Review does not exist.");
        }

        Review review = optionalReview.get();

        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(review.getRestaurantId());
        if (optionalRestaurant.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid restaurant Id.");
        }

        Restaurant restaurant = optionalRestaurant.get();
        List<Review> reviews = reviewRepository.findByStatusAndRestaurantId(ReviewStatus.APPROVED, restaurant.getId());


        if (adminReviewAction.getApproveReview()) {
            review.setStatus(ReviewStatus.APPROVED);
            Restaurant updatedRestaurant =  updateRestaurantScores(restaurant, reviews);
            restaurantRepository.save(updatedRestaurant);
        } else {
            review.setStatus(ReviewStatus.REJECTED);
        }

        reviewRepository.save(review);
    }
}
