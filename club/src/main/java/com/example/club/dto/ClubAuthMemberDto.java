package com.example.club.dto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class ClubAuthMemberDto extends User {
    // UserDetails 를 리턴 한다면, 자식 클래스인 User 를 리턴해도되고
    // User 를 상속받은 ClubAuthMemberDto 를 리턴해도 되는 상황이다

    // db 에서 인증된 정보를 담을 객체
    private String email;
    private String name;
    private boolean fromSocial;

    public ClubAuthMemberDto(String username, String password, boolean fromSocial,
            Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.email = username;
        this.fromSocial = fromSocial;
    }

}
