package com.dalbong.cafein.domain.loginHistory;

import com.dalbong.cafein.domain.member.AuthProvider;
import com.dalbong.cafein.domain.member.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "member")
@Entity
public class LoginHistory {

    //TODO 카멜 표기법으로 변경 필요
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long login_history_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    @Column(nullable = false)
    private String ip;

    @Builder.Default
    @Column(nullable = false)
    private final LocalDateTime loginDateTime = LocalDateTime.now();
}
