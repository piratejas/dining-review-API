package com.piratejas.diningReviewAPI.models;

import com.piratejas.diningReviewAPI.enums.CuisineType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "RESTAURANTS")
@Getter
@Setter
@RequiredArgsConstructor
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private CuisineType type;

    private String addressLine1;
    private String city;
    private String state;
    private String zipCode;

    private String phoneNumber;
    private String website;

    private Float peanutScore;
    private Float eggScore;
    private Float dairyScore;
    private Float overallScore;
}
