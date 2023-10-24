package com.piratejas.diningReviewAPI.repositories;

import com.piratejas.diningReviewAPI.models.Restaurant;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RestaurantRepository extends CrudRepository<Restaurant, Long> {
    Optional<Restaurant> findByNameAndZipCode(String name, String zipCode);
    Iterable<Restaurant> findByZipCodeAndPeanutScoreNotNullOrderByPeanutScore(String zipCode);
    Iterable<Restaurant> findByZipCodeAndDairyScoreNotNullOrderByDairyScore(String zipCode);
    Iterable<Restaurant> findByZipCodeAndEggScoreNotNullOrderByEggScore(String zipCode);
}
