package com.example.board.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.board.constant.MemberRole;
import com.example.board.dto.MemberAuthDto;
import com.example.board.dto.MemberDto;
import com.example.board.entity.Member;
import com.example.board.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class MemberDetailService implements UserDetailsService, MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    // 로그인 담당 메소드
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // id == email(현재 프로젝트에서 email로 사용) == username(security 속성명)
        Optional<Member> result = memberRepository.findById(username);

        if (!result.isPresent()) {
            // DB 에 저장된 이메일이 없으면 메세지 리턴
            throw new UsernameNotFoundException("이메일을 확인해 주세요");
        }

        Member member = result.get();
        // entity => dto
        // Security 로그인은 회원 정보 + 허가(사이트접근 권한)와 관련된 정보 를 담아줘야한다
        MemberDto memberDto = MemberDto.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .name(member.getName())
                .memberRole(member.getMemberRole())
                .build();

        return new MemberAuthDto(memberDto);
    }

    @Override
    public void register(MemberDto insertDto) {
        log.info("회원가입 service 요청 {}", insertDto);

        // try / catch : Exception 날리기위해 사용
        // 이상태는 TERMINAL 창에만 Exception 메세지가 나타난다
        // 이거를 controller 까지 전달을 시켜서 msg 형태로 날리는건 개인이 한번해보시오
        // 방법1
        try {
            // 중복 이메일 검사
            validateDuplicationMember(insertDto.getEmail());
        } catch (Exception e) {
            throw e;
        }
        // 방법2. 빨간줄 뜨면 throw 한다
        // validateDuplicationMember(insertDto.getEmail());

        Member member = Member.builder()
                .email(insertDto.getEmail())
                .password(passwordEncoder.encode(insertDto.getPassword()))
                .name(insertDto.getName())
                .memberRole(MemberRole.MEMBER)
                .build();
        // save() : PK 가 없으면 Insert, 있으면 update 가 자동으로 실행된다
        // 이메일을 중복으로 넣어서 회원가입하면 에러가 안나고 update 가 실행되버린다
        memberRepository.save(member);
    }

    // 중복 확인
    private void validateDuplicationMember(String email) throws IllegalStateException {
        Optional<Member> member = memberRepository.findById(email);

        if (member.isPresent()) {
            // throw : 강제로 Exception 발생
            // IllegalStateException : 값을 잘못 입력했어요 할때 사용한다
            throw new IllegalStateException("이미 가입된 이메일 입니다");
        }
    }

}
