package com.example.board.dto;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Data;

// Security 로그인은 회원 정보 + 허가(사이트접근 권한)와 관련된 정보 를 담아줘야한다
// MemberDto 에서는 회원정보만 담을수 있기때문에
// 회원 정보 + 허가와 관련된 정보를 담기 위해서 AuthDto 를 만든다
@Data
public class MemberAuthDto extends User {

    private MemberDto memberDto;

    // Collection (부모)
    // Collection 을 상속받은 자식 : List , Set
    // List 를 상속받은 자식 : AllayList ...
    // Set 을 상속받은 자식 : hashset ...
    // Collection<T> : 객체타입 아무거나 T 에 들어올수있다
    // Collection<? extends GrantedAuthority> : GrantedAuthority 를 상속받고 있는 자식만 올수있게
    // 제한을둠 (자식이 누가있는지 확인하는거는 자바API 같은데서 검색)
    public MemberAuthDto(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    // MemberDto 생성자
    public MemberAuthDto(MemberDto memberDto) {
        super(memberDto.getEmail(), memberDto.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + memberDto.getMemberRole())));
        this.memberDto = memberDto;
    }

}
