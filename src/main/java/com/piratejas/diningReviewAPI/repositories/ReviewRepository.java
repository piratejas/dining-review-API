package com.piratejas.diningReviewAPI.repositories;

import com.piratejas.diningReviewAPI.enums.ReviewStatus;
import com.piratejas.diningReviewAPI.models.Review;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReviewRepository extends CrudRepository<Review, Long> {
    List<Review> findByStatus(ReviewStatus reviewStatus);
    List<Review> findByStatusAndRestaurantId(ReviewStatus reviewStatus, Long restaurantId);
}
