package com.piratejas.diningReviewAPI.models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String city;
    private String state;
    private String zipCode;

    private Boolean peanutAllergy;
    private Boolean eggAllergy;
    private Boolean dairyAllergy;
}
