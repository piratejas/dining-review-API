package com.piratejas.diningReviewAPI.models;

import com.piratejas.diningReviewAPI.enums.ReviewStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "REVIEWS")
@Getter
@Setter
@RequiredArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String submittedBy;
    private Long restaurantId;
    private Float peanutScore;
    private Float eggScore;
    private Float dairyScore;
    private String comment;

    @Enumerated(EnumType.STRING)
    private ReviewStatus status;
}
