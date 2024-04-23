package com.example.club.repository;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.example.club.constant.ClubMemberRole;
import com.example.club.entity.ClubMember;

@SpringBootTest
public class ClubRepositoryTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClubMemberRepository clubMemberRepository;

    @Test
    public void testInsert() {
        // 1~80 : USER, 81~90 : USER,MANAGER, 91~100 : USER,MANAGER,ADMIN
        IntStream.rangeClosed(1, 100).forEach(i -> {
            ClubMember clubMember = ClubMember.builder()
                    .email("user" + i + "@soldesk.co.kr")
                    .name("사용자" + i)
                    .fromSocial(false)
                    .password(passwordEncoder.encode("1111"))
                    .build();
            clubMember.addMemberRole(ClubMemberRole.USER);
            if (i > 80) {
                clubMember.addMemberRole(ClubMemberRole.MANAGER);
            }
            if (i > 90) {
                clubMember.addMemberRole(ClubMemberRole.ADMIN);
            }
            clubMemberRepository.save(clubMember);
        });
    }

    // FetchType
    // 1) EAGER : left outer join 이 기본으로 실행됨
    // 2) LAZY : select 두 번으로 실행

    // 웹 개발시 EAGER 를 항상 사용하지 말자 => 처음부터 사용하지 않는 정보들을 가지고 올 필요가 없다
    // @OneToOne, @ManyToOne : FetchType.EAGER 인 것들은 LAZY 로 변경
    // 특정 상황에서는 EAGER 로 할 필요가 있기 때문에 그걸 알려주기 위해서 @EntityGraph 를 사용

    // @Transactional => @EntityGraph 를 사용해서 에러메세지 해결
    @Test
    public void testFindFromSocial() {
        System.out.println(clubMemberRepository.findByEmailAndFromSocial("user95@soldesk.co.kr", false));
    }
}
