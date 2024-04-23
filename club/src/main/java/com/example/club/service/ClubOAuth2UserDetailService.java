package com.example.club.service;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.club.constant.ClubMemberRole;
import com.example.club.dto.ClubAuthMemberDto;
import com.example.club.entity.ClubMember;
import com.example.club.repository.ClubMemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
@Service
public class ClubOAuth2UserDetailService extends DefaultOAuth2UserService {
    // 공통인증 로그인 방식

    private final ClubMemberRepository clubMemberRepository;

    private final PasswordEncoder passwordEncoder;

    // load 만 치면 자동완성
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 구글로 공통인증 방식으로 로그인하면 뜨는 정보들
        log.info("========================================");
        log.info("userRequest : {}", userRequest);
        String clientName = userRequest.getClientRegistration().getClientName();
        log.info("clientName : {}", clientName);
        log.info(userRequest.getAdditionalParameters());
        log.info("========================================");
        OAuth2User oAuth2User = super.loadUser(userRequest);
        oAuth2User.getAttributes().forEach((k, v) -> {
            log.info("{} : {}", k, v);
        });
        log.info("========================================");

        // 소셜로그인 을 하면 DB테이블에 저장하는 작업 (회원가입?)
        ClubMember clubMember = saveSocialMember(oAuth2User.getAttribute("email"));
        // entity => dto
        ClubAuthMemberDto clubAuthMemberDto = new ClubAuthMemberDto(clubMember.getEmail(), clubMember.getPassword(),
                clubMember.isFromSocial(), clubMember.getRoleSet().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name())).collect(Collectors.toSet()),
                oAuth2User.getAttributes());
        clubAuthMemberDto.setName(clubMember.getName());

        return clubAuthMemberDto;
    }

    private ClubMember saveSocialMember(String email) {
        Optional<ClubMember> result = clubMemberRepository.findByEmailAndFromSocial(email, true);

        if (result.isPresent()) {
            // 찾은 이메일이 회원이 되있으면 여기서 끝
            return result.get();
        }

        ClubMember clubMember = ClubMember.builder()
                .email(email)
                .name(email)
                .password(passwordEncoder.encode("1111")) // 임의 지정
                .fromSocial(true) // 소셜로그인
                .build();
        clubMember.addMemberRole(ClubMemberRole.USER); // 권한부여
        clubMemberRepository.save(clubMember);
        return clubMember;
    }
}
