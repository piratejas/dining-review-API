package com.piratejas.diningReviewAPI.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "USERS")
@Getter
@Setter
@RequiredArgsConstructor
public class User {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CITY")
    private String city;
    @Column(name = "STATE")
    private String state;
    @Column(name = "ZIP_CODE")
    private String zipCode;

    @Column(name = "PEANUT_ALLERGY")
    private Boolean peanutAllergy;
    @Column(name = "EGG_ALLERGY")
    private Boolean eggAllergy;
    @Column(name = "DAIRY_ALLERGY")
    private Boolean dairyAllergy;
}
