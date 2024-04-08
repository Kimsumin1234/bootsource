package com.example.jpa.repository;

import java.util.stream.LongStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.jpa.entity.Locker;
import com.example.jpa.entity.SportsMember;

@SpringBootTest
public class LockerRepositoryTest {

    @Autowired
    private SportsMemberRepository sportsMemberRepository;

    @Autowired
    private LockerRepository lockerRepository;

    @Test
    public void insertTest() {
        // Locker 부터 먼저 삽입해야한다
        // LongStream.range(1, 4).forEach(i -> {
        // Locker locker = Locker.builder()
        // .name("locker" + i)
        // .build();
        // lockerRepository.save(locker);
        // });

        // SportsMember 삽입
        LongStream.range(4, 7).forEach(i -> {
            SportsMember sportsMember = SportsMember.builder()
                    .name("user" + i)
                    .locker(Locker.builder().id(i).build())
                    .build();
            sportsMemberRepository.save(sportsMember);

        });
    }

    @Test
    public void updateTest() {
        SportsMember sportsMember = sportsMemberRepository.findById(7L).get();
        sportsMember.setName("홍길동");
        sportsMemberRepository.save(sportsMember);
    }

    @Test
    public void readTest() {
        // 회원 조회 후 locker 정보 조회
        // 일대일 관계도 다대일 관계처럼 그냥 조회가 가능하다
        SportsMember sportsMember = sportsMemberRepository.findById(1L).get();
        System.out.println(sportsMember);
        System.out.println("락커명 " + sportsMember.getLocker().getName());
        System.out.println("락커번호 " + sportsMember.getLocker().getId());
    }

    @Test
    public void readTest2() {
        // locker 조회 후 회원 정보 조회
        Locker locker = lockerRepository.findById(2L).get();
        System.out.println(locker);
        System.out.println(locker.getSportsMember());
        System.out.println("회원아이디 " + locker.getSportsMember().getId());
        System.out.println("회원이름 " + locker.getSportsMember().getName());
    }
}
