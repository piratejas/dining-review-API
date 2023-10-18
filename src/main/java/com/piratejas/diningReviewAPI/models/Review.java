package com.piratejas.diningReviewAPI.models;

import com.piratejas.diningReviewAPI.enums.ReviewStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Review {
    @Id
    @GeneratedValue
    private Long id;

    private String submittedBy;
    private Long restaurantId;
    private Integer peanutScore;
    private Integer eggScore;
    private Integer dairyScore;
    private String comment;

    @Enumerated(EnumType.STRING)
    private ReviewStatus status;
}
