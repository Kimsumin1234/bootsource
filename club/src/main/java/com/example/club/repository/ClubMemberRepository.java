package com.example.club.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;

import com.example.club.entity.ClubMember;

public interface ClubMemberRepository extends JpaRepository<ClubMember, String> {
    // @EntityGraph : LAZY 일때 특정 기능에서만 EAGER 로 처리 가능하게 하는 어노테이션, 반대도가능
    // 회원 찾기 (기준-email, social 회원여부)
    // @EntityGraph 를 사용하면 이 메소드에서만 EAGER 로 처리 가능해짐
    // @Query 를 사용해서 left join 을 하게만듬
    @EntityGraph(attributePaths = { "roleSet" }, type = EntityGraphType.LOAD)
    @Query("select m from ClubMember m where m.email = ?1 and m.fromSocial = ?2")
    Optional<ClubMember> findByEmailAndFromSocial(String email, boolean social);
}
