package com.dalbong.cafein.domain.memo;

import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.store.Store;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@ToString(exclude = {"review"})
@DiscriminatorValue("review")
@Entity
public class ReviewMemo extends Memo{

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    public ReviewMemo(Review review, Member writer, String content){
        super(writer, content);
        this.review = review;
    }

}
