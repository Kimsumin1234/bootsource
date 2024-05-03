package com.example.movie.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.movie.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 1) findBy~ 로 만드는 방법
    // (참고 :
    // https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html)
    // 2) findBy + @Query
    // 3) JPQL

    // 1) findBy~ 로 만드는 방법
    // Optional : email 에 유니크 제약조건을 걸어서 중복불가 상태 1개 만 나온다
    Optional<Member> findByEmail(String email);

    // save() 로 해서 select 두번 해서 update 하는 방법이 있지만
    // 만약에 바로 업데이트 구문으로 하고 싶다면 이런 방법도 있다
    @Modifying
    @Query("update Member m set m.nickname=?1 where m.email=?2")
    void updateNickName(String nickname, String email);
}
