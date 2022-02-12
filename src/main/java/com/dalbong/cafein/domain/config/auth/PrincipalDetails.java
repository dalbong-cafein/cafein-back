package com.dalbong.cafein.domain.config.auth;

import com.dalbong.cafein.domain.member.Member;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Data
public class PrincipalDetails implements UserDetails{

    private Member member;
    //private OAuth2UserInfo userInfo;

//    public PrincipalDetails(Member member, OAuth2UserInfo userInfo){
//        this.member = member;
//        this.userInfo = userInfo;
//    }

    public PrincipalDetails(Member member){
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collector = new ArrayList<>();

        member.getRoleSet().forEach(role -> {
            collector.add(() -> {
                return "ROLE_" + role;
            });});

        return collector;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

//    @Override
//    public Map<String, Object> getAttributes() {
//
//        return userInfo.getAttributes();
//    }
//
//    @Override
//    public String getName() {
//        return userInfo.getName();
//    }
}
