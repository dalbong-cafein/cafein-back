package com.dalbong.cafein.domain.sticker;

import com.dalbong.cafein.domain.review.Review;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@ToString(exclude = {"review"})
@DiscriminatorValue("review")
@Entity
public class ReviewSticker extends Sticker{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = true)
    private Review review;


}
