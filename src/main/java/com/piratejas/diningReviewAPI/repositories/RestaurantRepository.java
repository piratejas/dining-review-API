package com.piratejas.diningReviewAPI.repositories;

import com.piratejas.diningReviewAPI.models.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findByNameAndZipCode(String name, String zipCode);
    Iterable<Restaurant> findByZipCodeAndPeanutScoreNotNullOrderByPeanutScore(String zipCode);
    Iterable<Restaurant> findByZipCodeAndDairyScoreNotNullOrderByDairyScore(String zipCode);
    Iterable<Restaurant> findByZipCodeAndEggScoreNotNullOrderByEggScore(String zipCode);
}
