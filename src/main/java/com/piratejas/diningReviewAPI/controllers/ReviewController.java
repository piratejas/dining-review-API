package com.piratejas.diningReviewAPI.controllers;

import com.piratejas.diningReviewAPI.enums.ReviewStatus;
import com.piratejas.diningReviewAPI.models.Restaurant;
import com.piratejas.diningReviewAPI.models.Review;
import com.piratejas.diningReviewAPI.models.User;
import com.piratejas.diningReviewAPI.repositories.RestaurantRepository;
import com.piratejas.diningReviewAPI.repositories.ReviewRepository;
import com.piratejas.diningReviewAPI.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RequestMapping("/reviews")
@RestController
public class ReviewController {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    public ReviewController(ReviewRepository reviewRepository, UserRepository userRepository, RestaurantRepository restaurantRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void addReview(@RequestBody Review review) {
        validateReview(review);

        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(review.getRestaurantId());
        if (optionalRestaurant.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid restaurant Id.");
        }

        review.setStatus(ReviewStatus.PENDING);
        reviewRepository.save(review);
    }

    // Helpers
    private void validateReview(Review review) {
        if (ObjectUtils.isEmpty(review.getSubmittedBy())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User information is missing.");
        }

        if (ObjectUtils.isEmpty(review.getRestaurantId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Restaurant Id is missing.");
        }

        if (ObjectUtils.isEmpty(review.getDairyScore()) && ObjectUtils.isEmpty(review.getEggScore()) && ObjectUtils.isEmpty(review.getPeanutScore())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Review must contain at least one rating.");
        }

        Optional<User> optionalUser = userRepository.findByUsername(review.getSubmittedBy());
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "User is not registered.");
        }
    }
}
