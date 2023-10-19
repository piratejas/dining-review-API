package com.piratejas.diningReviewAPI.repositories;

import com.piratejas.diningReviewAPI.models.Review;
import org.springframework.data.repository.CrudRepository;

public interface ReviewRepository extends CrudRepository<Review, Long> {

}
