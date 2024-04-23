package com.example.club.service;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.club.constant.ClubMemberRole;
import com.example.club.dto.ClubAuthMemberDto;
import com.example.club.dto.ClubMemberDto;
import com.example.club.entity.ClubMember;
import com.example.club.repository.ClubMemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
@Service
public class ClubUserDetailService implements UserDetailsService, ClubMemberService {

    // UserDetailsService 는 이미 구현되있는 클래스
    // UserDetails 는 인터페이스 클래스 (security 프로젝트 /auth 로 확인했던 정보들의 메소드가 있음)
    // User 는 UserDetails 를 구현한 클래스

    private final ClubMemberRepository clubMemberRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 로그인 담당 메소드
        // username : 회원 아이디
        log.info("로그인 요청 {}", username);

        Optional<ClubMember> result = clubMemberRepository.findByEmailAndFromSocial(username, false);

        // 해당하는 아이디가 있는지 확인하는 작업
        if (!result.isPresent())
            // 없으면 Exception 메세지
            throw new UsernameNotFoundException("이메일 혹은 social 확인");
        // 있으면 가져오기
        ClubMember clubMember = result.get();
        log.info("============================");
        log.info("clubMember {}", clubMember);
        log.info("============================");

        // entity => dto
        // spring security 정보가 들어있어서 기존 entityToDto 방식과 조금 다르다
        ClubAuthMemberDto clubAuthMemberDto = new ClubAuthMemberDto(clubMember.getEmail(), clubMember.getPassword(),
                clubMember.isFromSocial(), clubMember.getRoleSet().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name())).collect(Collectors.toSet()));
        clubAuthMemberDto.setName(clubMember.getName());

        return clubAuthMemberDto;
    }

    @Override
    public String register(ClubMemberDto member) {
        // dto => entity 변경
        // dto 원본 비밀번호 => 암호화
        ClubMember clubMember = ClubMember.builder()
                .email(member.getEmail())
                .name(member.getName())
                .fromSocial(member.isFromSocial())
                .password(passwordEncoder.encode(member.getPassword()))
                .build();
        // role 부여
        clubMember.addMemberRole(ClubMemberRole.USER);
        return clubMemberRepository.save(clubMember).getEmail();
    }

}
