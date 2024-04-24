package com.example.board.service;

import com.example.board.dto.MemberDto;

// MemberDetailService 에 implements 해서 메소드 구현함
public interface MemberService {
    // 회원가입
    public void register(MemberDto insertDto);

    // 회원정보 수정, 회원탈퇴 => default dtoToEntity, entityToDto 도 만들어서 해보기
}
