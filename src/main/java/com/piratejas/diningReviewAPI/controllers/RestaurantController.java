package com.piratejas.diningReviewAPI.controllers;


import com.piratejas.diningReviewAPI.models.Restaurant;
import com.piratejas.diningReviewAPI.models.RestaurantDTO;
import com.piratejas.diningReviewAPI.repositories.RestaurantRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.piratejas.diningReviewAPI.utils.RestaurantUtils.*;

@RequestMapping("/restaurants")
@RestController
public class RestaurantController {
    private final RestaurantRepository restaurantRepository;

    public RestaurantController(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addRestaurant(@RequestBody Restaurant restaurant) {
        validateNewRestaurant(restaurant, restaurantRepository);
        restaurantRepository.save(restaurant);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RestaurantDTO> getAllRestaurants() {
        Iterable<Restaurant> allRestaurants = restaurantRepository.findAll();
        List<RestaurantDTO> allRestaurantsDTO = new ArrayList<>();
        for (Restaurant restaurant : allRestaurants) {
            RestaurantDTO restaurantDTO = convertRestaurantToDTO(restaurant);
            allRestaurantsDTO.add(restaurantDTO);
        }
        return allRestaurantsDTO;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RestaurantDTO getRestaurantById(@PathVariable Long id) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(id);
        if (optionalRestaurant.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found.");
        }

        Restaurant restaurant = optionalRestaurant.get();

        return convertRestaurantToDTO(restaurant);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<RestaurantDTO> searchRestaurants(@RequestParam(name = "zipCode") String zipCode, @RequestParam(name = "allergy") String allergy) {
        validateZipCode(zipCode);

        Iterable<Restaurant> restaurants;
        if (allergy.equalsIgnoreCase("peanut")) {
            restaurants = restaurantRepository.findByZipCodeAndPeanutScoreNotNullOrderByPeanutScore(zipCode);
        } else if (allergy.equalsIgnoreCase("dairy")) {
            restaurants = restaurantRepository.findByZipCodeAndDairyScoreNotNullOrderByDairyScore(zipCode);
        } else if (allergy.equalsIgnoreCase("egg")) {
            restaurants = restaurantRepository.findByZipCodeAndEggScoreNotNullOrderByEggScore(zipCode);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Allergy not specified.");
        }

        List<RestaurantDTO> allRestaurantsDTO = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
            RestaurantDTO restaurantDTO = convertRestaurantToDTO(restaurant);
            allRestaurantsDTO.add(restaurantDTO);
        }
        return allRestaurantsDTO;
    }
}
