package com.piratejas.diningReviewAPI.repositories;

import com.piratejas.diningReviewAPI.enums.ReviewStatus;
import com.piratejas.diningReviewAPI.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByStatus(ReviewStatus reviewStatus);
    List<Review> findByStatusAndRestaurantId(ReviewStatus reviewStatus, Long restaurantId);
}
