package com.example.club.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class ClubUserDetailService implements UserDetailsService {

    // UserDetailsService 는 이미 구현되있는 클래스
    // UserDetails 는 인터페이스 클래스 (security 프로젝트 /auth 로 확인했던 정보들의 메소드가 있음)
    // User 는 UserDetails 를 구현한 클래스

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 로그인 담당 메소드
        // username : 회원 아이디
        log.info("로그인 요청 {}", username);
        return null;
    }

}
