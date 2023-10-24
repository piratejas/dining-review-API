package com.piratejas.diningReviewAPI.models;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.piratejas.diningReviewAPI.enums.CuisineType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestaurantDTO {
    private String name;

    @Enumerated(EnumType.STRING)
    private CuisineType type;

    private String addressLine1;
    private String city;
    private String state;
    private String zipCode;

    private String phoneNumber;
    private String website;

    private String peanutScore;
    private String eggScore;
    private String dairyScore;
    private String overallScore;
}
