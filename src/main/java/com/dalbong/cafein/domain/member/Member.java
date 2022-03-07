package com.dalbong.cafein.domain.member;

import com.dalbong.cafein.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
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

    @Column(nullable = false)
    private String oauthId;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(joinColumns = @JoinColumn(name = "member_id"))
    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "role")
    private Set<MemberRole> roleSet = new HashSet<MemberRole>(Arrays.asList(MemberRole.USER));


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





}
