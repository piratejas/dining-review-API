package com.piratejas.diningReviewAPI.models;

import lombok.Data;

@Data
public class RegistrationDTO {
    private String username;
    private String password;
    private String city;
    private String county;
    private String postcode;
    private Boolean peanutAllergy;
    private Boolean eggAllergy;
    private Boolean dairyAllergy;
}
