package com.example.movie.service;

import com.example.movie.dto.MemberDto;
import com.example.movie.dto.PasswordChangeDto;
import com.example.movie.entity.Member;

public interface MovieUserService {

    // 회원가입
    String register(MemberDto insertDto) throws IllegalStateException;

    // 닉네임 수정
    String nickNameUpdate(MemberDto upMemberDto);

    // 비밀번호 수정
    void passwordUpdate(PasswordChangeDto pDto) throws IllegalStateException;

    // 회원탈퇴
    void leave(MemberDto leaveMemberDto);

    // dto => entity
    public default Member dtoToEntity(MemberDto memberDto) {
        return Member.builder()
                .email(memberDto.getEmail())
                .nickname(memberDto.getNickname())
                .password(memberDto.getPassword())
                .role(memberDto.getRole())
                .build();
    }

    // entity => dto
    public default MemberDto entityToDto(Member member) {
        return MemberDto.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .password(member.getPassword())
                .role(member.getRole())
                .build();
    }
}
