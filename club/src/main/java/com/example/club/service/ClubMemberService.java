package com.example.club.service;

import com.example.club.dto.ClubMemberDto;

// ClubMemberServiceImpl 을 따로 만들어서 implements 해도 되고
// ClubUserDetailService 에다가 implements 해도 된다
public interface ClubMemberService {
    // 회원가입
    String register(ClubMemberDto member);

    // 회원탈퇴

    // 회원수정
}
