package com.dalbong.cafein.domain.report;

import com.dalbong.cafein.domain.BaseEntity;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.reportCategory.ReportCategory;
import com.dalbong.cafein.domain.review.Review;
import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"review","toMember","fromMember","reportCategory"})
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name="report_uk",
                        columnNames={"review_id", "from_member_id"}
                )
        }
)
@Entity
public class Report extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    //신고된 리뷰가 삭제될 수 있음.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = true)
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_member_id", nullable = false)
    private Member toMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_member_id", nullable = false)
    private Member fromMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_category_id")
    private ReportCategory reportCategory;

    private String content;

    public void changeNullReview(){
        this.review = null;
    }
}
