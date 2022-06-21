package com.dalbong.cafein.domain.member;

import com.dalbong.cafein.domain.BaseEntity;
import com.dalbong.cafein.domain.report.Report;
import com.dalbong.cafein.domain.review.Review;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "roleSet")
@Entity
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false)
    private String username;

    @Column(unique = true)
    private String nickname;

    @Column(nullable = false)
    private String password;

    private String kakaoId;

    private String naverId;

    @Column(nullable = false)
    private String email;

    private String phone;

    @Column(nullable = false)
    private LocalDate birth;

    @Enumerated(EnumType.STRING)
    private GenderType genderType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthProvider mainAuthProvider;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(joinColumns = @JoinColumn(name = "member_id"))
    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "role")
    private Set<MemberRole> roleSet = new HashSet<MemberRole>(Arrays.asList(MemberRole.USER));

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private MemberState state = MemberState.NORMAL;

    private LocalDateTime reportExpiredDateTime;

    private LocalDateTime leaveDateTime;

    public void setKakaoId(String kakaoId){
        this.kakaoId = kakaoId;
    }

    public void setNaverId(String naverId){
        this.naverId = naverId;
    }

    public void changeUsername(String username){
        this.username = username;
    }

    public void changeNickname(String nickname){
        this.nickname = nickname;
    }

    public void changeEmail(String email){
        this.email = email;
    }

    public void changePhone(String phone){
        this.phone = phone;
    }

    public void changeBirth(LocalDate birth){
        this.birth = birth;
    }

    public void changeGender(GenderType genderType){
        this.genderType = genderType;
    }

    public void suspension(int reportCnt){

        switch (reportCnt){
            case 1:
                this.reportExpiredDateTime = LocalDateTime.now().plusDays(1);
                break;
            case 2:
                this.reportExpiredDateTime = LocalDateTime.now().plusDays(3);
                break;
            case 3:
                this.reportExpiredDateTime = LocalDateTime.now().plusDays(7);
                break;
            default:
                this.reportExpiredDateTime = LocalDateTime.now().plusMonths(1);
        }

        this.state = MemberState.SUSPENSION;
    }


    public void leave(){
        this.state = MemberState.LEAVE;
        this.leaveDateTime = LocalDateTime.now();
    }



}
