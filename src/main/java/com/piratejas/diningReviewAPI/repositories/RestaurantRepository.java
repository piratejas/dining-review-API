package com.piratejas.diningReviewAPI.repositories;

import com.piratejas.diningReviewAPI.models.Restaurant;
import org.springframework.data.repository.CrudRepository;

public interface RestaurantRepository extends CrudRepository<Restaurant, Long> {

}
