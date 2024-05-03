package com.example.movie.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.movie.constant.MemberRole;
import com.example.movie.dto.AuthMemberDto;
import com.example.movie.dto.MemberDto;
import com.example.movie.dto.PasswordChangeDto;
import com.example.movie.entity.Member;
import com.example.movie.repository.MemberRepository;
import com.example.movie.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class MovieUserServiceImpl implements UserDetailsService, MovieUserService {

    private final MemberRepository memberRepository;

    private final ReviewRepository reviewRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 시큐리티에서 사용하는 로그인 메소드
        // 리턴타입이 UserDetails 로 정해져있다
        // UserDetails 를 구현한 User 로 리턴을 해도되고
        // User 를 상속받은 CustomUser 로 리턴을 해도된다

        // 1) User 로 리턴하는 방법 (이거는 mid 로 찾아야한다)
        // 하지만 실제로 로그인은 email 과 password 를 사용한다
        // 그래서 이방법은 사용하기 힘들다
        // Optional<Member> result = memberRepository.findById(null);
        // if (result.isPresent()) {
        // Member member = result.get();
        // }
        // return User.builder()
        // .username(member.getEmail)
        // .password(member.getPassword)
        // .roles(member.getRole().toString())
        // .build();

        // 2) User 를 상속받은 CustomUser 로 리턴하는 방법
        log.info("로그인 요청 {}", username);

        Optional<Member> result = memberRepository.findByEmail(username);

        if (!result.isPresent()) {
            throw new UsernameNotFoundException("Check Email");
        }
        Member member = result.get();

        return new AuthMemberDto(entityToDto(member));
    }

    @Override
    public String register(MemberDto insertDto) throws IllegalStateException {
        log.info("회원가입 service {}", insertDto);

        // 중복검사
        validateDuplicationMember(insertDto.getEmail());

        Member member = Member.builder()
                .email(insertDto.getEmail())
                .password(passwordEncoder.encode(insertDto.getPassword()))
                .role(MemberRole.ADMIN)
                .nickname(insertDto.getNickname())
                .build();
        memberRepository.save(member);

        return member.getEmail();
    }

    // 중복 확인
    private void validateDuplicationMember(String email) throws IllegalStateException {
        Optional<Member> member = memberRepository.findByEmail(email);

        if (member.isPresent()) {
            // throw : 강제로 Exception 발생
            // IllegalStateException : 값을 잘못 입력했어요 할때 사용한다
            throw new IllegalStateException("이미 가입된 이메일 입니다");
        }
    }

    @Transactional
    @Override
    public String nickNameUpdate(MemberDto upMemberDto) {
        log.info("닉네임 수정 service {}", upMemberDto);

        // return memberRepository.save(dtoToEntity(upMemberDto)).getNickname();
        memberRepository.updateNickName(upMemberDto.getNickname(), upMemberDto.getEmail());
        return "닉네임 수정 완료";
    }

    @Override
    public void passwordUpdate(PasswordChangeDto pDto) throws IllegalStateException {
        log.info("비밀번호 수정 service {}", pDto);
        // 현재 이메일과 비밀번호 일치 여부 => true => 비밀번호 변경
        // select => 결과 있다면 => update
        Member member = memberRepository.findByEmail(pDto.getEmail()).get();

        // passwordEncoder.encode(1111) : 암호화
        // passwordEncoder.matches(rawPassword, encodedPassword) : 1111, 암호화된1111 비교

        // 현재비밀번호는 암호화가 된 상태
        // 이미 암호화된걸 복호화를 못시킨다
        // matches() 를 사용하면 암호화된거로 비교를 해준다
        if (!passwordEncoder.matches(pDto.getCurrentPassword(), member.getPassword())) {
            throw new IllegalStateException("현재 비밀번호가 다릅니다.");
        } else {
            member.setPassword(passwordEncoder.encode(pDto.getNewPassword()));
            memberRepository.save(member);
        }
    }

    @Transactional
    @Override
    public void leave(MemberDto leaveMemberDto) {
        // 아이디 와 비밀번호 일치 시
        Member member = memberRepository.findByEmail(leaveMemberDto.getEmail()).get();
        if (!passwordEncoder.matches(leaveMemberDto.getPassword(), member.getPassword())) {
            throw new IllegalStateException("현재 비밀번호가 다릅니다.");

        } else {
            // 회원탈퇴 진행
            reviewRepository.deleteByMember(member);
            memberRepository.delete(member);
        }

    }

}
