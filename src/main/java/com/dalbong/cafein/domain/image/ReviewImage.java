package com.dalbong.cafein.domain.image;

import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.review.Review;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@ToString(exclude = {"review"})
@DiscriminatorValue("review")
@Entity
public class ReviewImage extends Image{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reg_member_id")
    private Member regMember;

    protected ReviewImage(){}

    public ReviewImage(Review review, Member regMember, String imageUrl){
        super(imageUrl);
        this.review = review;
        this.regMember = regMember;
        review.getReviewImageList().add(this);
    }
}
