package com.piratejas.diningReviewAPI.utils;

import com.piratejas.diningReviewAPI.models.Restaurant;
import com.piratejas.diningReviewAPI.models.RestaurantDTO;
import com.piratejas.diningReviewAPI.models.Review;
import com.piratejas.diningReviewAPI.repositories.RestaurantRepository;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class RestaurantUtils {

    private static final Pattern zipCodePattern = Pattern.compile("\\d{5}");
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public static void validateNewRestaurant(Restaurant restaurant, RestaurantRepository restaurantRepository) {
        if (ObjectUtils.isEmpty(restaurant.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Restaurant name is missing.");
        }

        validateZipCode(restaurant.getZipCode());

        Optional<Restaurant> existingRestaurant = restaurantRepository.findByNameAndZipCode(restaurant.getName(), restaurant.getZipCode());
        if (existingRestaurant.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Restaurant already exists.");
        }
    }

    public static void validateZipCode(String zipCode) {
        if (!zipCodePattern.matcher(zipCode).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid zip code.");
        }
    }

    public static RestaurantDTO convertRestaurantToDTO(Restaurant restaurant) {
        RestaurantDTO restaurantDTO = new RestaurantDTO();

        restaurantDTO.setName(restaurant.getName());
        restaurantDTO.setType(restaurant.getType());
        restaurantDTO.setAddressLine1(restaurant.getAddressLine1());
        restaurantDTO.setCity(restaurant.getCity());
        restaurantDTO.setState(restaurant.getState());
        restaurantDTO.setZipCode(restaurant.getZipCode());
        restaurantDTO.setPhoneNumber(restaurant.getPhoneNumber());
        restaurantDTO.setWebsite(restaurant.getWebsite());
        restaurantDTO.setPeanutScore(restaurant.getPeanutScore() != null ? df.format(restaurant.getPeanutScore()) : null);
        restaurantDTO.setDairyScore(restaurant.getDairyScore() != null ? df.format(restaurant.getDairyScore()) : null);
        restaurantDTO.setEggScore(restaurant.getEggScore() != null ? df.format(restaurant.getEggScore()) : null);
        restaurantDTO.setOverallScore(restaurant.getOverallScore() != null ? df.format(restaurant.getOverallScore()) : null);

        return restaurantDTO;
    }

    public static Restaurant updateRestaurantScores(Restaurant restaurant, List<Review> reviews) {

        if (reviews.isEmpty()) {
            return restaurant;
        }

        int peanutSum = 0;
        int peanutCount = 0;
        int dairySum = 0;
        int dairyCount = 0;
        int eggSum = 0;
        int eggCount = 0;

        for (Review r : reviews) {
            if (!ObjectUtils.isEmpty(r.getPeanutScore())) {
                peanutSum += r.getPeanutScore();
                peanutCount++;
            }
            if (!ObjectUtils.isEmpty(r.getDairyScore())) {
                dairySum += r.getDairyScore();
                dairyCount++;
            }
            if (!ObjectUtils.isEmpty(r.getEggScore())) {
                eggSum += r.getEggScore();
                eggCount++;
            }
        }

        int totalCount = peanutCount + dairyCount + eggCount ;
        int totalSum = peanutSum + dairySum + eggSum;

        float overallScore = (float) totalSum / totalCount;
        restaurant.setOverallScore(overallScore);

        if (peanutCount > 0) {
            float peanutScore = (float) peanutSum / peanutCount;
            restaurant.setPeanutScore(peanutScore);
        }

        if (dairyCount > 0) {
            float dairyScore = (float) dairySum / dairyCount;
            restaurant.setDairyScore(dairyScore);
        }

        if (eggCount > 0) {
            float eggScore = (float) eggSum / eggCount;
            restaurant.setEggScore(eggScore);
        }

        return restaurant;
    }
}
