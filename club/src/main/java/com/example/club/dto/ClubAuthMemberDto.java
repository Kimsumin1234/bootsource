package com.example.club.dto;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class ClubAuthMemberDto extends User implements OAuth2User {
    // UserDetails 를 리턴 한다면, 자식 클래스인 User 를 리턴해도되고
    // User 를 상속받은 ClubAuthMemberDto 를 리턴해도 되는 상황이다

    // 로그인이 성공하면 생기는 정보를 담는 Dto 이다

    // db 에서 인증된 정보를 담을 객체
    private String email;
    private String password;
    private String name;
    private boolean fromSocial;

    // 소셜 로그인에서 넘어오는 값을 담을 객체
    // oAuth2User.getAttributes().forEach 로 넘어오는 정보
    private Map<String, Object> attr;

    // User 에서 넘어오는 정보를 받음
    public ClubAuthMemberDto(String username, String password, boolean fromSocial,
            Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities); // 부모객체 에서 넘어오는 값
        this.email = username;
        this.fromSocial = fromSocial;
        this.password = password;
    }

    // ClubAuthMemberDto 에서 넘어오는 정보
    // Map<String, Object> attr 도 초기화 해준다
    public ClubAuthMemberDto(String username, String password, boolean fromSocial,
            Collection<? extends GrantedAuthority> authorities, Map<String, Object> attr) {
        this(username, password, fromSocial, authorities);
        this.attr = attr;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attr;
    }

}
