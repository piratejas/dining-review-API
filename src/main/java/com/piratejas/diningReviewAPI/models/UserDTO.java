package com.piratejas.diningReviewAPI.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String name;

    private String city;
    private String state;
    private String zipCode;

    private Boolean peanutAllergy;
    private Boolean eggAllergy;
    private Boolean dairyAllergy;
}
