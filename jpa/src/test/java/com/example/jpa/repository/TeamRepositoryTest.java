package com.example.jpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.jpa.entity.Team;
import com.example.jpa.entity.TeamMember;

@SpringBootTest
public class TeamRepositoryTest {

    @Autowired // test 는 final 사용불가
    private TeamRepository teamRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Test
    public void insertTest() {
        // 외래키 제약조건 순서 팀 삽입 -> 팀맴버 삽입
        // 팀 정보 삽입
        Team team1 = teamRepository.save(Team.builder().id("team1").name("팀1").build());
        Team team2 = teamRepository.save(Team.builder().id("team2").name("팀2").build());
        Team team3 = teamRepository.save(Team.builder().id("team3").name("팀3").build());

        // 회원 정보 삽입
        // 제약조건 때문에 팀정보를 넣어준다
        teamMemberRepository.save(TeamMember.builder().id("member1").userName("홍길동").team(team1).build());
        teamMemberRepository.save(TeamMember.builder().id("member2").userName("성춘향").team(team2).build());
        teamMemberRepository.save(TeamMember.builder().id("member3").userName("이순신").team(team2).build());
        teamMemberRepository.save(TeamMember.builder().id("member4").userName("정우성").team(team3).build());
    }

    @Test
    public void getRowTest() {
        // select
        // tm1_0.member_id,
        // t1_0.team_id,
        // t1_0.team_name,
        // tm1_0.user_name
        //
        // from
        // team_member tm1_0
        //
        // left join
        // team t1_0
        // on t1_0.team_id=tm1_0.team_team_id
        //
        // where
        // tm1_0.member_id=?
        TeamMember teamMember = teamMemberRepository.findById("member1").get();
        System.out.println(teamMember); // TeamMember(id=member1, userName=홍길동, team=Team(id=team1, name=팀1))
    }
}
