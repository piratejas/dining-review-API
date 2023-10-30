package com.piratejas.diningReviewAPI.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantTest {

    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant();
        restaurant.setName("Testaraunt");
    }

    @Test
    void getName() {
        assertEquals("Testaraunt", restaurant.getName());
    }

    @Test
    void getType() {
    }

    @Test
    void getAddressLine1() {
    }

    @Test
    void getCity() {
    }

    @Test
    void getState() {
    }

    @Test
    void getZipCode() {
    }

    @Test
    void getPhoneNumber() {
    }

    @Test
    void getWebsite() {
    }

    @Test
    void getPeanutScore() {
    }

    @Test
    void getEggScore() {
    }

    @Test
    void getDairyScore() {
    }

    @Test
    void getOverallScore() {
    }

    @Test
    void setId() {
    }

    @Test
    void setName() {
    }

    @Test
    void setType() {
    }

    @Test
    void setAddressLine1() {
    }

    @Test
    void setCity() {
    }

    @Test
    void setState() {
    }

    @Test
    void setZipCode() {
    }

    @Test
    void setPhoneNumber() {
    }

    @Test
    void setWebsite() {
    }

    @Test
    void setPeanutScore() {
    }

    @Test
    void setEggScore() {
    }

    @Test
    void setDairyScore() {
    }

    @Test
    void setOverallScore() {
    }
}