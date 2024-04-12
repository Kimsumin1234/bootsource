package com.example.jpa.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.example.jpa.entity.Member2;
import com.example.jpa.entity.Team2;

public interface Member2Repository extends JpaRepository<Member2, Long>, QuerydslPredicateExecutor<Member2> {
    // jpql 사용 시
    // 1. entity 타입 결과 => List<Entity명>
    // 2. 개별 타입 결과 => List<Object[]>

    // findAll() 을 @Query 를 사용해서 직접 만들어 볼수도 있다
    // Sort(data.domain.Sort) : 정렬, 메소드 변수안에 넣어서 사용할수있다
    @Query("select m from Member2 m")
    List<Member2> findByMembers(Sort sort);

    @Query("select m.userName, m.age from Member2 m")
    // ConversionFailedException: Failed to convert from type [java.lang.Object[]] :
    // ==> List<Member2> 이걸 사용하면 에러가 뜬다
    List<Object[]> findByMembers2();

    // 특정 나이보다 많은 회원 조회
    @Query("select m from Member2 m where m.age > ?1")
    List<Member2> findByAgeList(int age);

    // 특정 팀의 회원 조회
    @Query("select m from Member2 m where m.team2 = ?1")
    List<Member2> findByTeamEqual(Team2 team2);

    @Query("select m from Member2 m where m.team2.id = ?1")
    List<Member2> findByTeamIdEqual(Long id);

    // 집계함수
    @Query("select count(m), sum(m.age), avg(m.age), max(m.age), min(m.age) from Member2 m")
    List<Object[]> aggregate();

    // JOIN
    // 쿼리문에 사용한 이름들은 다 엔티티를 기준으로 사용한다 + 별칭 적용

    // from jpql_member m1_0 join jpql_team t1_0 on t1_0.team_id=m1_0.team2_team_id
    // 객체에 @ManyToOne 같이 관계 형성을 해놔서 on 을 안써도 잘 출력이된다
    @Query("select m from Member2 m join m.team2 t where t.name = ?1")
    List<Member2> findByTeamMember(String teamName);

    // select m,t : 2개를 조회하니깐 Object[] 를 사용
    @Query("select m,t from Member2 m join m.team2 t where t.name = ?1")
    List<Object[]> findByTeamMember2(String teamName);

    // 외부조인 을 할경우에 on 을 사용한다
    @Query("select m,t from Member2 m left join m.team2 t on t.name = ?1")
    List<Object[]> findByTeamMember3(String teamName);
}
