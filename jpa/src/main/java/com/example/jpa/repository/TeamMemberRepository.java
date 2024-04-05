package com.example.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.jpa.entity.TeamMember;

public interface TeamMemberRepository extends JpaRepository<TeamMember, String> {

    // sql 쿼리 가 아님, 객체지향 쿼리이다(객체를 기준으로 작성해야 함)
    // Query 구문을 직접 작성하고 싶을때 이방법을 사용한다
    // @Query("select m,t from 다쪽에해당하는객체 별칭 join 별칭으로접근가능 별칭 where ?1(1번 ? 이다)")
    @Query("select m,t from TeamMember m join m.team t where t.name=?1")
    public List<TeamMember> findByMemberEqualTeam(String teamName);
}
