package com.piratejas.diningReviewAPI.controllers;


import com.piratejas.diningReviewAPI.models.Restaurant;
import com.piratejas.diningReviewAPI.models.RestaurantDTO;
import com.piratejas.diningReviewAPI.repositories.RestaurantRepository;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@RequestMapping("/restaurants")
@RestController
public class RestaurantController {
    private final RestaurantRepository restaurantRepository;
    private final Pattern zipCodePattern = Pattern.compile("\\d{5}");
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public RestaurantController(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addRestaurant(@RequestBody Restaurant restaurant) {
        validateNewRestaurant(restaurant);

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

    // Helpers
    private void validateNewRestaurant(Restaurant restaurant) {
        if (ObjectUtils.isEmpty(restaurant.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Restaurant name is missing.");
        }

        validateZipCode(restaurant.getZipCode());

        Optional<Restaurant> existingRestaurant = restaurantRepository.findByNameAndZipCode(restaurant.getName(), restaurant.getZipCode());
        if (existingRestaurant.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Restaurant already exists.");
        }
    }

    private void validateZipCode(String zipcode) {
        if (!zipCodePattern.matcher(zipcode).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid zip code.");
        }
    }

    private RestaurantDTO convertRestaurantToDTO(Restaurant restaurant) {
        RestaurantDTO restaurantDTO = new RestaurantDTO();

        restaurantDTO.setName(restaurant.getName());
        restaurantDTO.setType(restaurant.getType());
        restaurantDTO.setAddressLine1(restaurant.getAddressLine1());
        restaurantDTO.setCity(restaurant.getCity());
        restaurantDTO.setState(restaurant.getState());
        restaurantDTO.setZipCode(restaurant.getZipCode());
        restaurantDTO.setPhoneNumber(restaurant.getPhoneNumber());
        restaurantDTO.setWebsite(restaurant.getWebsite());
        restaurantDTO.setPeanutScore(df.format(restaurant.getPeanutScore()));
        restaurantDTO.setDairyScore(df.format(restaurant.getDairyScore()));
        restaurantDTO.setEggScore(df.format(restaurant.getEggScore()));
        restaurantDTO.setOverallScore(df.format(restaurant.getOverallScore()));

        return restaurantDTO;
    }
}
