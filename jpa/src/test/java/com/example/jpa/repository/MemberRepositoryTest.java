package com.example.jpa.repository;

import java.util.Optional;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.jpa.entity.Member;
import com.example.jpa.entity.RoleType;

@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void insertTest() {
        IntStream.rangeClosed(1, 50).forEach(i -> {
            Member member = Member.builder()
                    .id("user" + i)
                    .userName("user" + i)
                    .age(i)
                    .roleType(RoleType.ADMIN)
                    .description("user" + i)
                    .build();
            memberRepository.save(member);
        });
    }

    @Test
    public void readTest() {
        // 한개조회
        System.out.println(memberRepository.findById("user7"));
        System.out.println("-----------------------");
        // 전체조회
        memberRepository.findAll().forEach(member -> System.out.println(member));
        System.out.println("-----------------------");
        // userName 조회(특정이름 조회), MemberRepository 에 메소드를 추가해서 가능해짐
        memberRepository.findByUserName("user5").forEach(meber -> System.out.println(meber));
    }

    @Test
    public void updateTest() {
        Optional<Member> result = memberRepository.findById("user17");

        result.ifPresent(member -> {
            member.setRoleType(RoleType.USER);
            memberRepository.save(member);
        });
        System.out.println(memberRepository.findById("user17"));
    }

    @Test
    public void deleteTest() {
        Member member = memberRepository.findById("user4").get();
        memberRepository.delete(member);
        System.out.println(memberRepository.findById("user4"));
    }
}
