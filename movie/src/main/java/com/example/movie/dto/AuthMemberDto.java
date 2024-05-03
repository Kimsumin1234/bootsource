package com.example.movie.dto;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class AuthMemberDto extends User {

    private MemberDto memberDto;

    // CustomUser : User 를 상속받은후 퀵픽스 누르고 두번째거를 클릭함
    public AuthMemberDto(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);

    }

    // 이거는 직접 적어서 만듬
    public AuthMemberDto(MemberDto memberDto) {
        // super(username, password, authorities);
        super(memberDto.getEmail(), memberDto.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + memberDto.getRole())));
        this.memberDto = memberDto;
    }

}
